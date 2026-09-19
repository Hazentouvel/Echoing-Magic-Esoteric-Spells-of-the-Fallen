package net.hazen.echoing_magic.Client;

import com.mojang.datafixers.util.Either;
import net.hazen.echoing_magic.Utils.EMSpellTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT)
public class EMClientEvents {

    @SubscribeEvent
    public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();
        String marker = "\u2995";

        for (final int[] i = {0}; i[0] < elements.size(); i[0]++) {
            var element = elements.get(i[0]);

            // We only care about the "Left" side (the raw text/Components)
            element.left().ifPresent(text -> {
                String rawText = text.getString();

                if (rawText.contains(marker)) {
                    // 1. Extract the text between the symbols
                    int start = rawText.indexOf(marker);
                    int end = rawText.lastIndexOf(marker);

                    if (start != end) {
                        String prefix = rawText.substring(0, start);
                        String emPart = rawText.substring(start + 1, end);
                        String suffix = rawText.substring(end + 1);

                        // 2. Clear the current line
                        elements.remove(i[0]);

                        // 3. Reconstruct the line
                        if (!prefix.isEmpty()) {
                            elements.add(i[0], Either.left(Component.literal(prefix)));
                            i[0]++;
                        }

                        // Inject our custom Lightning Component
                        elements.add(i[0], Either.right(new EMSpellTooltip.EMSpellTooltipData(Component.literal(emPart))));
                        i[0]++;

                        if (!suffix.isEmpty()) {
                            elements.add(i[0], Either.left(Component.literal(suffix)));
                        }
                    }
                }
            });
        }
    }


}