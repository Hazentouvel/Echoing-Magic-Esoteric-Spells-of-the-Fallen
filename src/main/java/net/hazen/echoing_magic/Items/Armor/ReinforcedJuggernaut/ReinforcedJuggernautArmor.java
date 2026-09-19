package net.hazen.echoing_magic.Items.Armor.ReinforcedJuggernaut;

import io.redspace.ironslib.registry.IronsLibRegistries;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.item.armor.IDisableHat;
import io.redspace.ironsspellbooks.item.armor.IDisableJacket;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.hazen.echoing_magic.Compat.GTBCGeomancyPlus.GGArmorCompat;
import net.hazen.echoing_magic.EMConfig;
import net.hazen.echoing_magic.Utils.Armor.ImbuableEMArmorItem;
import net.hazen.hazentouvelib.Rarities.HLRarities;
import net.hazen.hazentouvelib.Registries.HLAttributeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.List;

import java.awt.*;

public class ReinforcedJuggernautArmor extends ImbuableEMArmorItem implements IDisableJacket, IDisableHat {
    public static final int COOLDOWN_TICKS = 200;


    public ReinforcedJuggernautArmor(Type type, Properties settings) {
        //super(EMArmorMaterials.REINFORCE_JUGGERNAUT_MATERIAL, type,
        super(ArmorMaterials.NETHERITE, type,
                settings
                        .stacksTo(1)
                        .rarity(HLRarities.COSMIC_RARITY.getValue())
                        .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
                        .fireResistant()
                ,
                new AttributeContainer(IronsLibRegistries.AttributeRegistry.CRIT_DAMAGE, .1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(HLAttributeRegistry.COSMIC_SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.SPELL_POWER, .05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE)
        );
    }



    public List<ItemAttributeModifiers.Entry> createExtraAttributes() {
        var group = EquipmentSlotGroup.bySlot(getEquipmentSlot());
        ItemAttributeModifiers.Builder attributes = ItemAttributeModifiers.builder();
        GGArmorCompat.addGeomancySpellPowerPure(attributes, group);
        return attributes.build().modifiers();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Component getName(ItemStack stack) {
        String baseName = super.getName(stack).getString();
        MutableComponent customName = Component.empty();

        long timeMs = System.currentTimeMillis();
        final int AQUA = 0xc8fffe;
        final int BLUE = 0x00a5e7;

        for (int i = 0; i < baseName.length(); i++) {
            float colorPhase = (float) (Math.sin((timeMs * 0.005) + (i * 0.3f)) + 1.0f) / 2.0f;
            int baseColor = interpolateColor(AQUA, BLUE, colorPhase);
            float glintSwell = (float) Math.max(0, Math.sin((timeMs * 0.012) + (i * 0.5f)) - 0.7f) * 3.0f;
            int finalColor = applyGlint(baseColor, glintSwell);
            customName = customName.append(Component.literal(String.valueOf(baseName.charAt(i))).withStyle(style -> style.withColor(TextColor.fromRgb(finalColor & 0xFFFFFF))));
        }

        return customName;
    }

    private int interpolateColor(int c1, int c2, float ratio) {
        int r = (int) (((c1 >> 16) & 0xFF) * (1 - ratio)
                + ((c2 >> 16) & 0xFF) * ratio);

        int g = (int) (((c1 >> 8) & 0xFF) * (1 - ratio)
                + ((c2 >> 8) & 0xFF) * ratio);

        int b = (int) ((c1 & 0xFF) * (1 - ratio)
                + (c2 & 0xFF) * ratio);

        return (255 << 24) | (r << 16) | (g << 8) | b;
    }

    private int applyGlint(int color, float glint) {
        int r = Math.min(255, (int) (((color >> 16) & 0xFF) + (255 * glint)));
        int g = Math.min(255, (int) (((color >> 8) & 0xFF) + (255 * glint)));
        int b = Math.min(255, (int) ((color & 0xFF) + (255 * glint)));
        return (255 << 24) | (r << 16) | (g << 8) | b;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack,
                                @NotNull TooltipContext context,
                                @NotNull List<Component> lines,
                                @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, lines, flag);

        lines.add(Component.translatable("tooltip.irons_spellbooks.passive_ability_no_cooldown", new Object[]{Component.literal(Utils.timeFromTicks((float)Utils.applyCooldownReduction(20, MinecraftInstanceHelper.getPlayer()), 1)).withStyle(ChatFormatting.AQUA)}).withStyle(ChatFormatting.DARK_PURPLE));
        lines.add(Component.translatable("item.echoing_magic.set_bonus.description"));
        lines.add(Component.translatable("item.echoing_magic.reinforced_juggernaut_explosion_resist.description")
                .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
        lines.add(Component.translatable("item.echoing_magic.reinforced_juggernaut_set_bonus.description")
                .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
    }



    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new ReinforcedJuggernautArmorRenderer(new ReinforcedJuggernautArmorModel());
    }
}