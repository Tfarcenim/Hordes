package tfar.hordes;

import net.minecraft.world.level.GameRules;

public class ModGameRules {

    public static final GameRules.Key<GameRules.BooleanValue> RULE_ACTIVE = GameRules.register(name("active"), GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));
    public static final GameRules.Key<GameRules.IntegerValue> RULE_SPAWN_RATE = GameRules.register(name("spawn_rate"), GameRules.Category.SPAWNING, GameRules.IntegerValue.create(12000));
    public static final GameRules.Key<GameRules.IntegerValue> RULE_MIN_DISTANCE = GameRules.register(name("min_distance"), GameRules.Category.SPAWNING, GameRules.IntegerValue.create(60));

    public static final GameRules.Key<GameRules.IntegerValue> RULE_MIN_SIZE = GameRules.register(name("min_size"), GameRules.Category.SPAWNING, GameRules.IntegerValue.create(10));
    public static final GameRules.Key<GameRules.IntegerValue> RULE_MAX_SIZE = GameRules.register(name("max_size"), GameRules.Category.SPAWNING, GameRules.IntegerValue.create(50));

    public static final GameRules.Key<GameRules.IntegerValue> RULE_SPAWN_CHANCE = GameRules.register(name("spawn_chance"), GameRules.Category.SPAWNING, GameRules.IntegerValue.create(100));
    public static final GameRules.Key<GameRules.BooleanValue> RULE_NIGHT_SPAWN = GameRules.register(name("night_spawn"), GameRules.Category.SPAWNING, GameRules.BooleanValue.create(false));


    public static String name(String path) {
        return Hordes.id(path).toString();
    }

    public static void peek(){}

}
