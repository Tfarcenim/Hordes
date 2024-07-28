package tfar.hordes;

import net.minecraftforge.fml.common.Mod;

@Mod(Hordes.MOD_ID)
public class HordesForge {
    
    public HordesForge() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        Hordes.init();
        
    }
}