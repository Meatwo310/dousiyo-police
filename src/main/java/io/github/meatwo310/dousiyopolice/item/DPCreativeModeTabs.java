package io.github.meatwo310.dousiyopolice.item;

import io.github.meatwo310.dousiyopolice.DousiyoPolice;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DPCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DousiyoPolice.MODID);

    public static final RegistryObject<CreativeModeTab> DOUSIYO_POLICE_TAB = TABS.register(
            DousiyoPolice.MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.%s.main".formatted(DousiyoPolice.MODID)))
                    .icon(() -> new ItemStack(DPItems.HANDCUFFS.get()))
                    .displayItems(DPCreativeModeTabs::getDisplayItems)
                    .build()
    );

    private static void getDisplayItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
        DPItems.ITEMS.getEntries().stream()
                .map(RegistryObject::get)
                .forEach(output::accept);
    }

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
