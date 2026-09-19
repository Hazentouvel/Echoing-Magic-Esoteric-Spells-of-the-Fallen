package net.hazen.echoing_magic.Entities.Spells.SlamEffect;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.projectile.LODSlamEffect;
import com.ratrod.archaion.registry.ACEntityTypes;
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
import org.jetbrains.annotations.Nullable;

public class ExtendedLODSlamEffect extends LODSlamEffect {
    private boolean damaged;
    private float damage;

    public ExtendedLODSlamEffect(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    public ExtendedLODSlamEffect(EntityType type, double x, double y, double z, Level worldIn) {
        super(type, worldIn);
        this.setPos(x, y, z);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }


    public void tick() {
        if (this.level().isClientSide() && this.firstTick) {
            ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_smash_initial"));
            AAALevel.addParticle(this.level(), info.position(this.position().add((double)0.0F, (double)0.5F, (double)0.0F)).scale(2.0F));
        }

        super.tick();
        if (!this.level().isClientSide() && !this.isRemoved()) {
            if (this.tickCount == 2 && this.generation < 10) {
                this.spawnChild();
            }

            if (this.tickCount == 20) {
                this.damageArea();
                this.discard();
            }

        }
    }

    public static void summonRing(
            ServerLevel serverLevel,
            Vec3 originPos,
            @Nullable Entity source,
            float damage
    ) {
        int count = 12;
        double rotationOffset = serverLevel.getRandom().nextDouble() * Math.PI * 2.0D;

        for (int i = 0; i < count; ++i) {
            double angle = (Math.PI * 2D) * (double) i / (double) count + rotationOffset;
            double dx = Math.cos(angle);
            double dz = Math.sin(angle);

            Vec3 dir = new Vec3(dx, 0.0D, dz);

            ExtendedLODSlamEffect slam = (ExtendedLODSlamEffect) EMEntityRegistry.SLAM_SPELL.get().create(serverLevel);

            slam.moveTo(originPos.add(dir.scale(4.0D)));
            slam.slamDirection = dir;
            slam.generation = 1;
            slam.source = source;
            slam.damage = damage;

            serverLevel.addFreshEntity(slam);
        }
    }

    private void spawnChild() {
        ServerLevel serverLevel = (ServerLevel)this.level();
        this.place(serverLevel, this.position().add(this.slamDirection.scale((double)4.0F)), this.slamDirection, this.generation + 1);
    }

    private void place(ServerLevel serverLevel, Vec3 pos, Vec3 dir, int gen) {
        ExtendedLODSlamEffect slam = (ExtendedLODSlamEffect)((EntityType) EMEntityRegistry.SLAM_SPELL.get()).create(serverLevel);
        slam.moveTo(pos);
        slam.slamDirection = dir;
        slam.generation = gen;
        slam.source = this.source;
        serverLevel.addFreshEntity(slam);
    }

    private void damageArea() {
        if (!this.damaged) {
            this.damaged = true;
            ServerLevel serverLevel = (ServerLevel)this.level();
            Entity cause = (Entity)(this.source != null ? this.source : this);
            this.playSound((SoundEvent) ACSounds.LOD_SMASH.get(), 1.2F, 1.8F);
            AABB area = AABB.ofSize(this.position(), (double)3.0F, (double)12.0F, (double)3.0F);

            for(LivingEntity target : serverLevel.getEntitiesOfClass(LivingEntity.class, area, (e) -> e != cause && e.isAlive() && this.canTarget(e))) {
                target.hurt(serverLevel.damageSources().explosion(cause, cause), 30.0F);

                DamageSources.applyDamage(target, this.damage, ((AbstractSpell) EMSpellRegistries.ECHOING_SLAM.get()).getDamageSource(this, this.source));
            }

        }
    }

    private boolean canTarget(LivingEntity target) {
        Entity var3 = this.source;
        if (var3 instanceof Mob mob) {
            return mob.canAttack(target);
        } else {
            return true;
        }
    }
}