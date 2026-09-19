package net.hazen.echoing_magic.Entities.Spells.InterceptBlast;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.projectile.LODInterceptBlast;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ExtendedLODEchoBlast extends LODInterceptBlast {

    private float damage;

    public ExtendedLODEchoBlast(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    public ExtendedLODEchoBlast(EntityType type, double x, double y, double z, Level worldIn) {
        super(type, worldIn);
        this.setPos(x, y, z);
    }

    public ExtendedLODEchoBlast(Level worldIn, LivingEntity shooter) {
        this(EMEntityRegistry.ECHOING_BLAST_SPELL.get(), shooter.getX(), shooter.getEyeY(), shooter.getZ(), worldIn);
        this.setOwner(shooter);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    @Override
    public void blast() {
        if (!this.level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel)this.level();
            this.playSound((SoundEvent) ACSounds.ECHO_STAR_BLAST.get(), 5.0F, (0.5F + this.random.nextFloat() * 0.2F) * (1.8F - this.size));
            ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("echo_blast_intercept"));
            AAALevel.addParticle(serverLevel, true, info.position(this.position()).rotation(0.0F, this.random.nextFloat() * 90.0F, 0.0F).scale(3.0F * this.size));
            Entity cause = (Entity)(this.getOwner() != null ? this.getOwner() : this);
            AABB area = AABB.ofSize(this.position(), (double)(14.0F * this.size), (double)(14.0F * this.size), (double)(14.0F * this.size));

            for(LivingEntity target : serverLevel.getEntitiesOfClass(LivingEntity.class, area, (e) -> e != cause && e.isAlive() && this.canTarget(e))) {
                target.hurt(serverLevel.damageSources().explosion(cause, cause), this.damage);
                DamageSources.applyDamage(target, this.damage, ((AbstractSpell) EMSpellRegistries.ECHO_BLAST.get()).getDamageSource(this, this.getOwner()));
                Vec3 knockback = target.position().subtract(this.position()).normalize().scale(3.0D).add(0.0D, 0.35D, 0.0D);
                target.setDeltaMovement(target.getDeltaMovement().add(knockback));
                target.hurtMarked = true;
            }
        }
    }

    private boolean canTarget(LivingEntity target) {
        Entity var3 = this.getOwner();
        if (var3 instanceof Mob mob) {
            return mob.canAttack(target);
        } else {
            return true;
        }
    }
}