package net.hazen.echoing_magic.Spells.Schools.Cosmic;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.Haunter;
import com.ratrod.archaion.registry.ACEffects;
import com.ratrod.archaion.registry.ACItems;
import com.ratrod.archaion.registry.ACSounds;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.network.particles.ShockwaveParticlesPacket;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.particle.ZapParticleOption;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;

import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.acetheeldritchking.aces_spell_utils.utils.ImpactFrameHandler;
import net.hazen.echoing_magic.EchoingMagic;
import net.hazen.hazentouvelib.Registries.HLSchoolRegistry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import static net.acetheeldritchking.aces_spell_utils.utils.ASUtils.isValidUnlockItemInInventory;

public class EchoingExplosionSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "echoing_explosion");
    private final DefaultConfig defaultConfig;

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.effect_length", new Object[]{Utils.timeFromTicks((float)this.getDuration(spellLevel, caster), 2)}), Component.translatable("ui.irons_spellbooks.radius", new Object[]{Utils.stringTruncation((double)this.getRadius(spellLevel, caster), 2)}));
    }

    @Override
    public Component getLockedMessage() {
        return Component.translatable("ui.echoing_magic.echo_charge_spell");
    }

    @Override
    public boolean allowLooting() {
        return false;
    }

    @Override
    public boolean canBeCraftedBy(Player player) {
        Item echoedManuscript = ACItems.ECHO_CHARGE.get();
        return isValidUnlockItemInInventory(echoedManuscript, player);
    }

    public EchoingExplosionSpell() {
        this.defaultConfig = (new DefaultConfig())
                .setMinRarity(SpellRarity.RARE)
                .setSchoolResource(HLSchoolRegistry.COSMIC_RESOURCE)
                .setMaxLevel(8)
                .setCooldownSeconds((double)45.0F)
                .build();
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 5;
        this.castTime = 45;
        this.baseManaCost = 100;
    }

    public CastType getCastType() {
        return CastType.LONG;
    }

    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of((SoundEvent) SoundEvents.CREEPER_PRIMED);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent) ACSounds.HAUNTER_EXPLODE.get());
    }

    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = this.getRadius(spellLevel, entity);

        if (!level.isClientSide()) {
            ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("haunter_boom"));
            AAALevel.addParticle(level, info.position(entity.position().add(0.0D, 1.0D, 0.0D)).scale(1.5F));
        }

        level.getEntities(entity, entity.getBoundingBox().inflate((double)radius, (double)4.0F, (double)radius), (target) -> !DamageSources.isFriendlyFireBetween(target, entity) && Utils.hasLineOfSight(level, entity, target, true)).forEach((target) -> {
            if (target instanceof LivingEntity livingEntity) {
                if (livingEntity.distanceToSqr(entity) < (double)(radius * radius)) {
                    livingEntity.addEffect(new MobEffectInstance(ACEffects.ARMOR_BREAK, this.getDuration(spellLevel, entity)));
                }
            }

        });
        Vec3 start = entity.getBoundingBox().getCenter();

        float damage = this.getDamage(spellLevel, entity);
        level.getEntities(entity, entity.getBoundingBox().inflate((double)radius, (double)radius, (double)radius), (target) -> !DamageSources.isFriendlyFireBetween(target, entity) && Utils.hasLineOfSight(level, entity, target, true)).forEach((target) -> {
            if (target instanceof LivingEntity livingEntity) {
                if (this.canHit(entity, target) && livingEntity.distanceToSqr(entity) < (double)(radius * radius)) {
                    Vec3 dest = livingEntity.getBoundingBox().getCenter();
                    ((ServerLevel)level).sendParticles(new ZapParticleOption(dest), start.x, start.y, start.z, 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F);
                    MagicManager.spawnParticles(level, ParticleHelper.ELECTRICITY, livingEntity.getX(), livingEntity.getY() + (double)(livingEntity.getBbHeight() / 2.0F), livingEntity.getZ(), 10, (double)(livingEntity.getBbWidth() / 3.0F), (double)(livingEntity.getBbHeight() / 3.0F), (double)(livingEntity.getBbWidth() / 3.0F), 0.1, false);
                    DamageSources.applyDamage(target, damage, this.getDamageSource(entity));
                }
            }

        });

        triggerImpactFrame(entity);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private boolean canHit(Entity owner, Entity target) {
        return target != owner && target.isAlive() && target.isPickable() && !target.isSpectator();
    }

    public float getDamage(int spellLevel, LivingEntity caster) {
        return 10.0F + this.getSpellPower(spellLevel, caster) * 0.75F;
    }

    private void triggerImpactFrame(LivingEntity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            ImpactFrameHandler.trigger(serverPlayer, 0xc8fffe, 0x00a5e7, 0.8f, ImpactFrameHandler.DEFAULT_THRESHOLD, 10, 1, 0.1f);
        }
    }

    public float getRadius(int spellLevel, LivingEntity caster) {
        return 6.0F + (float)spellLevel * 0.75F;
    }

    public int getDuration(int spellLevel, LivingEntity caster) {
        return (int)(this.getSpellPower(spellLevel, caster) * 20.0F);
    }

    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.PREPARE_CROSS_ARMS;
    }

    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.CAST_T_POSE;
    }
}
