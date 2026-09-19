package net.hazen.echoing_magic.Items.Curios;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;

import net.acetheeldritchking.aces_spell_utils.items.curios.FlatCooldownPassiveAbilityCurio;
import net.hazen.echoing_magic.EMConfig;
import net.hazen.hazentouvelib.Rarities.HLRarities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.common.EventBusSubscriber;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

public class ImpactAugmentItem extends FlatCooldownPassiveAbilityCurio {
    private static final Component VANITY_DESCRIPTION;

    public static int COOLDOWN;

    protected int getCooldownTicks() {
        return EMConfig.impactAugmentCooldown * 20;
    }

    static {
        COOLDOWN = EMConfig.impactAugmentCooldown * 20;
    }

    public ImpactAugmentItem() {
        super(new Item.Properties()
                        .stacksTo(1)
                        .rarity(HLRarities.COSMIC_RARITY.getValue())
                        .fireResistant()
                , Curios.NECKLACE_SLOT);
    }

    private void handleCurse(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity != null && !slotContext.entity().level.isClientSide) {
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent((handler) -> {
                ItemStack equippedStack = handler.getEquippedCurios().getStackInSlot(slotContext.index());
                if (ItemStack.matches(stack, equippedStack)) {
                    handler.setEquippedCurio(slotContext.identifier(), slotContext.index(), ItemStack.EMPTY);
                    this.createItemEntity(slotContext.entity().level, stack, slotContext.entity().position());
                }

            });
        }

    }

    public List<Component> getAttributesTooltip(List<Component> tooltips, Item.TooltipContext tooltipContext, ItemStack stack) {
        Player player = MinecraftInstanceHelper.getPlayer();
        if (player != null) {
            super.getAttributesTooltip(tooltips, tooltipContext, stack);
        }

        tooltips.add(0, VANITY_DESCRIPTION);
        return tooltips;
    }

    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        if (slotContext.entity().tickCount % 20 == 0) {
            this.handleCurse(slotContext, stack);
        }

    }

    private void createItemEntity(Level level, ItemStack stack, Vec3 center) {
        Vec3 target = center.add((new Vec3((double)((float)Utils.random.nextIntBetweenInclusive(4, 8) + Utils.random.nextFloat()), (double)0.0F, (double)0.0F)).yRot(Utils.random.nextFloat() * ((float)Math.PI * 2F)));
        Vec3 clipped = Utils.raycastForBlock(level, center.add((double)0.0F, (double)0.5F, (double)0.0F), target.add((double)0.0F, (double)0.5F, (double)0.0F), Fluid.NONE).getLocation();
        Vec3 placement = Utils.moveToRelativeGroundLevel(level, clipped, 5).add((double)0.0F, (double)0.75F, (double)0.0F);
        ItemEntity item = new ItemEntity(level, placement.x, placement.y, placement.z, stack);
        level.addFreshEntity(item);
        MagicManager.spawnParticles(level, ParticleHelper.UNSTABLE_ENDER, placement.x, placement.y, placement.z, 20, 0.2, 0.2, 0.2, 0.2, false);
        level.playSound((Player)null, BlockPos.containing(placement), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
        level.playSound((Player)null, BlockPos.containing(center), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    static {
        VANITY_DESCRIPTION = Component.translatable("item.echoing_magic.impact_augment.desc.alt")
                .withStyle(new ChatFormatting[]{ChatFormatting.AQUA, ChatFormatting.ITALIC});
    }
}
