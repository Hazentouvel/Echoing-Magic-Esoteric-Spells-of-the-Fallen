package net.hazen.echoing_magic.Events;

import net.hazen.echoing_magic.Utils.ScrollTextureRegistry;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ModelEvent;

public final class EMScrollModelEvents {

    private EMScrollModelEvents() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(
                EMScrollModelEvents::registerAdditionalModels
        );
    }

    private static void registerAdditionalModels(
            ModelEvent.RegisterAdditional event
    ) {
        ScrollTextureRegistry.register();

        for (ResourceLocation modelLocation :
                ScrollTextureRegistry.getRegisteredModels().values()) {

            event.register(ModelResourceLocation.standalone(modelLocation));
        }
    }

}