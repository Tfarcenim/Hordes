package tfar.hordes.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.CustomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import tfar.hordes.HordeSpawner;

import java.util.ArrayList;
import java.util.List;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @ModifyVariable(method = "createLevels",at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;get(Lnet/minecraft/resources/ResourceKey;)Ljava/lang/Object;"))
    private List<CustomSpawner> addCustomSpawners(List<CustomSpawner> instance) {
        List<CustomSpawner> copy = new ArrayList<>(instance);
        copy.add(new HordeSpawner());
        return copy;
    }
}
