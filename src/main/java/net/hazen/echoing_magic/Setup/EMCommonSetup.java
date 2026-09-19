package net.hazen.echoing_magic.Setup;

import net.hazen.echoing_magic.Entities.Mobs.Summons.Brave.SummonBrave;
import net.hazen.echoing_magic.Entities.Mobs.Summons.Grimoray.ConjureGrimoray;
import net.hazen.echoing_magic.Registries.EMEntityRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber
public class EMCommonSetup {

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(EMEntityRegistry.CONJURE_GRIMORAY.get(), ConjureGrimoray.createAttributes().build());
        event.put(EMEntityRegistry.SUMMON_BRAVE.get(), SummonBrave.createAttributes().build());
    }
}