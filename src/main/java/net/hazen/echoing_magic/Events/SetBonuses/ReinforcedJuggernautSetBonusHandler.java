package net.hazen.echoing_magic.Events.SetBonuses;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACSounds;
import io.redspace.ironsspellbooks.api.util.Utils;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.hazen.echoing_magic.EMConfig;
import net.hazen.echoing_magic.EchoingMagic;
import net.hazen.echoing_magic.Items.Armor.ReinforcedJuggernaut.ReinforcedJuggernautArmor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

@EventBusSubscriber(modid = EchoingMagic.MOD_ID)
public class ReinforcedJuggernautSetBonusHandler {

    public static final float EXPLOSION_RESISTANCE;

    public static final int CRIT_AOE_COOLDOWN = 200;

    public static final double CRIT_AOE_RADIUS = 8.0D;
    public static final double CRIT_AOE_HEIGHT = 4.0D;

    public static final float CRIT_AOE_DAMAGE = 3.0F;
    public static final float CRIT_WEAPON_DAMAGE_MULTIPLIER = 0.75F;
    public static final double CRIT_AOE_KNOCKBACK = 2.0D;

    private static boolean ReinforcedJuggernautArmorSetBonus(LivingEntity entity) {
        return entity.getItemBySlot(ArmorItem.Type.HELMET.getSlot()).getItem() instanceof ReinforcedJuggernautArmor &&
                entity.getItemBySlot(ArmorItem.Type.CHESTPLATE.getSlot()).getItem() instanceof ReinforcedJuggernautArmor &&
                entity.getItemBySlot(ArmorItem.Type.LEGGINGS.getSlot()).getItem() instanceof ReinforcedJuggernautArmor &&
                entity.getItemBySlot(ArmorItem.Type.BOOTS.getSlot()).getItem() instanceof ReinforcedJuggernautArmor;
    }

    protected float getExplosionResistance() {
        return EMConfig.reinforcedJuggernautExplosionResist;
    }

    static {
        EXPLOSION_RESISTANCE = EMConfig.reinforcedJuggernautExplosionResist;
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity livingEntity = event.getEntity();

        if (!ReinforcedJuggernautArmorSetBonus(livingEntity)) {
            return;
        }

        DamageSource source = event.getSource();

        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            event.setAmount(event.getAmount() * EXPLOSION_RESISTANCE);
        }

        if (!livingEntity.level().isClientSide()) {
            livingEntity.playSound(ACSounds.LOD_HURT.get(), 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();

        if (!(target instanceof LivingEntity livingTarget)) {
            return;
        }

        if (!event.isCriticalHit()) {
            return;
        }

        if (!ReinforcedJuggernautArmorSetBonus(player)) {
            return;
        }

        Item chestplate = player.getItemBySlot(ArmorItem.Type.CHESTPLATE.getSlot()).getItem();

        if (player.getCooldowns().isOnCooldown(chestplate)) {
            return;
        }

        player.getCooldowns().addCooldown(chestplate, CRIT_AOE_COOLDOWN);
        applyCriticalAoE(player);
    }

    private static void applyCriticalAoE(Player player) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        player.playSound((SoundEvent) ACSounds.LOD_SMASH.get(), 3.0F, 1.0F);

        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_boom_ground"));

        AAALevel.addParticle(serverLevel, info.position(player.position().add(0.0D, 0.2D, 0.0D)).scale(3.0F));
        AABB area = AABB.ofSize(player.position(), CRIT_AOE_RADIUS * 2.0D, CRIT_AOE_HEIGHT, CRIT_AOE_RADIUS * 2.0D);

        for (LivingEntity target : serverLevel.getEntitiesOfClass(LivingEntity.class, area, entity -> entity != player && entity.isAlive())) {

            if (target.distanceToSqr(player) > CRIT_AOE_RADIUS * CRIT_AOE_RADIUS) {
                continue;
            }

            Vec3 knockback = target.position()
                    .subtract(player.position())
                    .normalize()
                    .scale(CRIT_AOE_KNOCKBACK)
                    .add(0.0D, 0.5D, 0.0D);

            target.setDeltaMovement(
                    target.getDeltaMovement().add(knockback)
            );

            target.hurtMarked = true;

            float weaponDamage = Utils.getWeaponDamage(player);
            float damage = CRIT_AOE_DAMAGE + (weaponDamage * CRIT_WEAPON_DAMAGE_MULTIPLIER);

            target.hurt(player.damageSources().playerAttack(player), damage);
        }
    }

}