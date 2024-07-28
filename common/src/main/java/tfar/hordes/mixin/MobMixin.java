package tfar.hordes.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.hordes.MobDuck;

@Mixin(Mob.class)
public class MobMixin implements MobDuck {

    @Unique boolean horde;

    @Override
    public boolean isFromHorde() {
        return horde;
    }

    @Override
    public void setFromHorde(boolean fromHorde) {
        horde = fromHorde;
    }

    @Inject(method = "readAdditionalSaveData",at = @At("RETURN"))
    private void loadExtraData(CompoundTag tag, CallbackInfo ci) {
        horde = tag.getBoolean("horde");
    }

    @Inject(method = "addAdditionalSaveData",at = @At("RETURN"))
    private void saveExtraData(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("horde",horde);
    }

    @Inject(method = "isSunBurnTick",at = @At("HEAD"),cancellable = true)
    private void sunScreen(CallbackInfoReturnable<Boolean> cir) {
        if (horde) cir.setReturnValue(false);
    }
}
