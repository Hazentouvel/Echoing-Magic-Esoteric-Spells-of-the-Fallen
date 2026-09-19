package net.hazen.echoing_magic.Entities.Spells.EchoStar;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.LastOfDeepslate;
import com.ratrod.archaion.entities.projectile.EchoStarProjectile;
import com.ratrod.archaion.registry.ACSounds;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.damage.DamageSources;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.hazen.echoing_magic.Registries.EMEntityRegistry;
import net.hazen.echoing_magic.Spells.EMSpellRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ExtendedEchoStar extends EchoStarProjectile {
    private float baseDamage = 20.0F;
    private float powerBonus;
    private float damage;

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public ExtendedEchoStar(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    public ExtendedEchoStar(EntityType type, double x, double y, double z, Level worldIn) {
        super(type, worldIn);
        this.setPos(x, y, z);
    }

    public ExtendedEchoStar(Level worldIn, LivingEntity shooter) {
        this(EMEntityRegistry.ECHO_STAR_SPELL.get(), shooter.getX(), shooter.getEyeY(), shooter.getZ(), worldIn);
        this.setOwner(shooter);
    }

    public void setPowerBonus(float powerBonus) {
        this.powerBonus = powerBonus;
    }

    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    @Override
    public void damageArea() {
        ServerLevel server = (ServerLevel) this.level();
        float damage = this.baseDamage + this.powerBonus;
        for (LivingEntity target : server.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3.0D))) {
            if (target != this.getOwner() && this.canHurt(target)) {
                target.hurt(server.damageSources().explosion(this, this.getOwner()), damage);
                DamageSources.applyDamage(target, damage, ((AbstractSpell) EMSpellRegistries.ECHO_STAR.get()).getDamageSource(this, this.getOwner()));
            }
        }

        this.playSound((SoundEvent) ACSounds.ECHO_STAR_BLAST.get(), 4.0F, 1.0F);
        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_boom_group"));
        AAALevel.addParticle(this.level(), true, info.position(this.position().add(0.0D, 0.5D, 0.0D)).scale(1.5F));

        this.discard();
    }

    private boolean canHurt(LivingEntity target) {
        return true;
    }

}