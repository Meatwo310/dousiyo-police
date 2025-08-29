package io.github.meatwo310.dousiyopolice;

import io.github.meatwo310.dousiyopolice.config.ServerConfig;
import io.github.meatwo310.dousiyopolice.item.DPCreativeModeTabs;
import io.github.meatwo310.dousiyopolice.item.DPItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DousiyoPolice.MODID)
public class DousiyoPolice {
    public static final String MODID = "dousiyopolice";

    public DousiyoPolice(FMLJavaModLoadingContext context) {
        var bus = context.getModEventBus();

        DPCreativeModeTabs.register(bus);
        DPItems.register(bus);

        context.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }
}
