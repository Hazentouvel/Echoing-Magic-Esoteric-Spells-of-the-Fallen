package net.hazen.echoing_magic.Entities.Spells.SlamEffect;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;

import net.hazen.echoing_magic.Registries.EMEntityRegistry;
import net.hazen.echoing_magic.Spells.EMSpellRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class EchoSlamShockwave extends AoeEntity {
    int waveAnim;

    public EchoSlamShockwave(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.waveAnim = -1;
        this.reapplicationDelay = 25;
        this.setCircular();
    }

    public EchoSlamShockwave(Level level, float radius) {
        this((EntityType) EMEntityRegistry.ECHO_SLAM_SHOCKWAVE.get(), level);
        this.setRadius(radius);
    }

    public void applyEffect(LivingEntity target) {
        SpellDamageSource damageSource = ((AbstractSpell) EMSpellRegistries.ECHOING_SLAM.get()).getDamageSource((Entity)(this.getOwner() == null ? this : this.getOwner()));
        DamageSources.ignoreNextKnockback(target);
        if (target.hurt(damageSource, this.getDamage())) {
            target.igniteForSeconds(5.0F);
            target.setDeltaMovement(target.getDeltaMovement().add((double)0.0F, 0.65, (double)0.0F));
            target.hurtMarked = true;
        }

    }

    public float getParticleCount() {
        return 0.0F;
    }

    public void ambientParticles() {
    }

    public void tick() {
        float radius = this.getRadius();
        Level level = this.level;
        if ((float)(this.waveAnim++) < radius) {
            if (!level.isClientSide) {
                if (this.waveAnim % 2 == 0) {
                    float volume = (float)(this.waveAnim + 8) / 16.0F;
                    this.playSound((SoundEvent)SoundRegistry.EARTHQUAKE_IMPACT.get(), volume, (float)Utils.random.nextIntBetweenInclusive(90, 110) * 0.01F);
                }

                float circumferenceMin = (float)((this.waveAnim - 1) * 2) * 3.14F;
                float circumferenceMax = (float)((this.waveAnim + 1) * 2) * 3.14F;
                int minBlocks = Mth.clamp((int)circumferenceMin, 0, 750);
                int maxBlocks = Mth.clamp((int)circumferenceMax, 0, 750);
                float anglePerBlockMin = 360.0F / (float)minBlocks;
                float anglePerBlockMax = 360.0F / (float)maxBlocks;

                for(int i = 0; i < minBlocks; ++i) {
                    Vec3 vec3 = new Vec3((double)((float)this.waveAnim * Mth.cos(anglePerBlockMin * (float)i)), (double)0.0F, (double)((float)this.waveAnim * Mth.sin(anglePerBlockMin * (float)i)));
                    BlockPos blockPos = BlockPos.containing(Utils.moveToRelativeGroundLevel(level, this.position().add(vec3), 4)).below();
                    Utils.createTremorBlock(level, blockPos, 0.1F + this.random.nextFloat() * 0.2F);
                }

                List<LivingEntity> targets = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(this.getInflation().x, this.getInflation().y, this.getInflation().z));
                int r1Sqr = this.waveAnim * this.waveAnim;
                int r2Sqr = (this.waveAnim + 1) * (this.waveAnim + 1);

                for(LivingEntity target : targets) {
                    double distanceSqr = target.distanceToSqr(this);
                    if (this.canHitEntity(target) && distanceSqr >= (double)r1Sqr && distanceSqr <= (double)r2Sqr && this.canHitTargetForGroundContext(target)) {
                        this.applyEffect(target);
                    }
                }
            }
        } else {
            this.discard();
        }

    }

    public boolean shouldBeSaved() {
        return false;
    }

    protected boolean canHitTargetForGroundContext(LivingEntity target) {
        return !this.level.noCollision(target.getBoundingBox().move(new Vec3((double)0.0F, -0.9999, (double)0.0F)));
    }

    protected Vec3 getInflation() {
        return new Vec3((double)0.0F, (double)5.0F, (double)0.0F);
    }

    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 3.0F);
    }

    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }
}
