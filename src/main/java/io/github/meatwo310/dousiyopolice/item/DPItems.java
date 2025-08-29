package io.github.meatwo310.dousiyopolice.item;

import io.github.meatwo310.dousiyopolice.DousiyoPolice;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DPItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DousiyoPolice.MODID);

    public static final RegistryObject<Handcuffs> HANDCUFFS =
            ITEMS.register("handcuffs", Handcuffs::new);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
