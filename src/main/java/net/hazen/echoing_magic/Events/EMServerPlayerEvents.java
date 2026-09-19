package net.hazen.echoing_magic.Events;

import io.redspace.ironsspellbooks.api.events.SpellTeleportEvent;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.hazen.echoing_magic.Items.Curios.ImpactAugmentItem;
import net.hazen.echoing_magic.Registries.EMItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class EMServerPlayerEvents {
    public EMServerPlayerEvents() {
    }

    @SubscribeEvent
    public static void onSpellTeleport(SpellTeleportEvent event) {
        ImpactAugmentItem impactAugmentItem = (ImpactAugmentItem) EMItemRegistry.IMPACT_AUGMENT.get();
        Entity var2 = event.getEntity();
        if (var2 instanceof ServerPlayer player) {
            if (impactAugmentItem.isEquippedBy(player) && impactAugmentItem.tryProcCooldown(player)) {
                player.addEffect(new MobEffectInstance(MobEffectRegistry.EVASION, 60, 0, false, false, true));
                if (player.level() instanceof ServerLevel serverLevel) {
                    Vec3 destination = new Vec3(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                    impact(serverLevel, destination, player);
                }
            }
        }


    }


    private static void impact(ServerLevel level, Vec3 center, LivingEntity owner) {
        BlockPos pos = BlockPos.containing(center);
        BlockState state = level.getBlockState(pos);

        if (state.isAir()) {
            pos = pos.below();
        }

        level.playSound(null, pos, SoundEvents.MACE_SMASH_GROUND_HEAVY, SoundSource.PLAYERS, 2.0F, 1.0F);
        level.levelEvent(2013, pos, 750);

        center = pos.getCenter().add(0.0D, 0.5D, 0.0D);

        int radius = 4;

        AABB area = AABB.ofSize(center, radius * 2.0D, 6.0D, radius * 2.0D);

        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area, entity -> entity != owner && entity.isAlive())) {

            float damage = 5.0F;

            if (target.hurt(level.damageSources().thrown(null, owner), damage)) {

                Vec3 knockback = target.position()
                        .subtract(center)
                        .normalize()
                        .scale(1.8D);

                target.setDeltaMovement(
                        target.getDeltaMovement().add(knockback)
                );

                target.hurtMarked = true;
            }
        }
    }
}
