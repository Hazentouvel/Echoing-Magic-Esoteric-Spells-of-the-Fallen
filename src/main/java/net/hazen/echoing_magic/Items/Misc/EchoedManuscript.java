package net.hazen.echoing_magic.Items.Misc;

import io.redspace.ironsspellbooks.api.util.Utils;

import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.hazen.hazentouvelib.Rarities.HLRarities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EchoedManuscript extends Item {

    public EchoedManuscript() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(HLRarities.COSMIC_RARITY.getValue())
                .fireResistant()
        );
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack,
                                @NotNull TooltipContext context,
                                @NotNull List<Component> lines,
                                @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, lines, flag);
        lines.add(Component.translatable("item.echoing_magic.echoed_manuscript.description")
                .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
    }
}