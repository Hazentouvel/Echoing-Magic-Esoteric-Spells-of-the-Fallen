package net.hazen.echoing_magic;

import com.ratrod.archaion.registry.ACCreativeModeTabs;
import net.hazen.echoing_magic.Compat.GTBCGeomancyPlus.GGArmorCompat;
import net.hazen.echoing_magic.Compat.GTBCGeomancyPlus.GGSpellCompat;
import net.hazen.echoing_magic.Events.EMScrollModelEvents;
import net.hazen.echoing_magic.Registries.EMEntityRegistry;
import net.hazen.echoing_magic.Registries.EMItemRegistry;
import net.hazen.echoing_magic.Spells.EMSpellRegistries;
import net.hazen.echoing_magic.Utils.Armor.EMArmorMaterials;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(EchoingMagic.MOD_ID)
public class EchoingMagic {
    public static final String MOD_ID = "echoing_magic";
    public static final Logger LOGGER = LogUtils.getLogger();


    public EchoingMagic(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        /*
        *** Mod Compat
         */

        GGArmorCompat.init();
        GGSpellCompat.init();

        EMItemRegistry.register(modEventBus);
        EMArmorMaterials.register(modEventBus);
        EMEntityRegistry.register(modEventBus);
        EMSpellRegistries.register(modEventBus);
        EMScrollModelEvents.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, EMConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == ACCreativeModeTabs.ARCHAION_TAB.getKey()) {

            /*
            *** Armor
             */

            // Reinforced Juggernaut
            event.accept(EMItemRegistry.REINFORCED_JUGGERNAUT_HELMET.get());
            event.accept(EMItemRegistry.REINFORCED_JUGGERNAUT_CHESTPLATE.get());
            event.accept(EMItemRegistry.REINFORCED_JUGGERNAUT_LEGGINGS.get());
            event.accept(EMItemRegistry.REINFORCED_JUGGERNAUT_BOOTS.get());

            /*
            *** Weapons
             */

            // Echoed Demolisher

            // Echoed Staff

            /*
            *** Curios
             */

            // Impact Augment
            event.accept(EMItemRegistry.IMPACT_AUGMENT.get());

            /*
            *** Materials
             */

            event.accept(EMItemRegistry.ECHOING_ESSENCE.get());
            event.accept(EMItemRegistry.ECHOED_MANUSCRIPT.get());
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid = EchoingMagic.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ClientModEvents {
        @SubscribeEvent
        static void onClientSetup(FMLClientSetupEvent event) {

        }
    }

    public static ResourceLocation id(@NotNull String path)
    {
        return ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, path);
    }
}
