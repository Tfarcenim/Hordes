package tfar.hordes;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

public class HordeSpawner implements CustomSpawner {
    static final int ATTEMPTS = 10;

    private final RandomSource random = RandomSource.create();
    private int tickDelay = -1;

    @Override
    public int tick(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies) {
        if (!spawnEnemies) return 0;
        if (!level.getGameRules().getBoolean(ModGameRules.RULE_ACTIVE)) return 0;

        if (tickDelay == -1) {
            tickDelay = level.getGameRules().getInt(ModGameRules.RULE_SPAWN_RATE);
        }

        if (tickDelay > 0) {
            tickDelay--;
        }

        if (tickDelay == 0) {

            if (!(level.isDay() || level.getGameRules().getBoolean(ModGameRules.RULE_NIGHT_SPAWN))) {
                tickDelay = -1;
                return 0;
            }

            int spawnChance = Math.max(1,level.getGameRules().getInt(ModGameRules.RULE_SPAWN_CHANCE));
            if (spawnChance < random.nextInt(100) || spawn(level)) {
                tickDelay = -1;
                return 1;
            }
        }
        return 0;
    }


    private boolean spawn(ServerLevel serverLevel) {
        Player player = serverLevel.getRandomPlayer();
        if (player == null) {
            return true;
        } else if (this.random.nextInt(10) != 0) {
            return false;
        } else {
            int currentAttempt = 0;
            while (currentAttempt < ATTEMPTS) {
                currentAttempt++;
                ChunkPos playerPos = new ChunkPos(player.blockPosition());
                int r = 7;
                int chunkX = random.nextInt(2 * r + 1) - r;
                int chunkZ = random.nextInt(2 * r + 1) - r;
                ChunkPos hordePos = new ChunkPos(playerPos.x + chunkX, playerPos.z + chunkZ);


                int pX = hordePos.getMinBlockX() + random.nextInt(16);
                int pZ = hordePos.getMinBlockZ() + random.nextInt(16);
                BlockPos spawnPoint = new BlockPos(pX, serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, pX, pZ), pZ);

                double distSq = player.blockPosition().distSqr(spawnPoint);
                double min_dist = serverLevel.getGameRules().getInt(ModGameRules.RULE_MIN_DISTANCE);

                if (distSq < min_dist * min_dist) {
                    continue;
                }

                if (distSq > 128 * 128) {
                    continue;
                }

                int min = Math.max(0,serverLevel.getGameRules().getInt(ModGameRules.RULE_MIN_SIZE));

                int max = Math.max(min,serverLevel.getGameRules().getInt(ModGameRules.RULE_MAX_SIZE)) + 1;

                int size = min + random.nextInt(max - min);

                boolean flag = false;
                for (int i = 0; i < size; i++) {
                    int x = spawnPoint.getX() + random.nextInt(15) - 7;
                    int z = spawnPoint.getZ() + random.nextInt(15) - 7;
                    if (isChunkLoaded(serverLevel, new ChunkPos(x >> 4,z >> 4))) {
                        BlockPos randPos = new BlockPos(x, serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z), z);
                        EntityType<Zombie> type = EntityType.ZOMBIE;
                        if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, serverLevel, randPos, type)) {
                            Zombie zombie = type.spawn(serverLevel, randPos, MobSpawnType.EVENT);
                            ((MobDuck) zombie).setFromHorde(true);
                            flag = true;
                        }
                    }
                }
                if (flag) {
                  //  System.out.println("Summoned horde at: " + spawnPoint);
                 //   player.teleportTo(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
                    break;
                }
            }
            return true;
        }
    }

    private static boolean isChunkLoaded(ServerLevel level, ChunkPos pos) {
        LevelChunk $$3 = level.getChunkSource().getChunkNow(pos.x, pos.z);
        if ($$3 == null) {
            return false;
        } else {
            return $$3.getFullStatus() == FullChunkStatus.ENTITY_TICKING && level.areEntitiesLoaded(pos.toLong());
        }
    }

    private static boolean isChunkLoaded(ServerLevel level, BlockPos pos) {
        ChunkPos $$2 = new ChunkPos(pos);
        return isChunkLoaded(level,$$2);
    }
}
//# Can hordes also spawn at night? These "horde" zombies will not catch fire in sunlight
//HordeSpawnAtNight = False
//# How many blocks away from the player will the horde spawn?
//HordeDistanceAway = 60
//# Determine if a horde should be spawned, in seconds
//CheckForHordeInterval = 12000
//# Determine percent change of horde being spawned, 1-100
//HordeSpawnChance =  25
//# Determine minimum size of horde, 0-100
//HordeMinimumSize = 0
//# determine maximum size of horde, 0-100 but has to be greater than or equal to the minimum size
//HordeMaximumSize = 10