package net.hazen.echoing_magic.Entities.Spells.FallingBlock;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.entities.LODFallingBlock;
import com.ratrod.archaion.registry.ACSounds;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.hazen.echoing_magic.Registries.EMEntityRegistry;
import net.hazen.echoing_magic.Spells.EMSpellRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class ExtendedLODFallingBlock extends LODFallingBlock {

    private LivingEntity owner;
    private float damage;

    public ExtendedLODFallingBlock(EntityType<? extends ExtendedLODFallingBlock> type, Level worldIn) {
        super(type, worldIn);
        this.damage = 25.0F;
    }

    public ExtendedLODFallingBlock(Level levelIn, LivingEntity shooter) {
        this((EntityType) EMEntityRegistry.FALLING_BLOCK_SPELL.get(), levelIn);
        this.setOwner(shooter);
    }

    public ExtendedLODFallingBlock(EntityType type, double x, double y, double z, Level worldIn) {
        super(type, worldIn);
        this.setPos(x, y, z);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    public LivingEntity getOwner() {
        return this.owner;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    @Override
    public void tick() {
        if (this.getBlockState().isAir()) {
            this.discard();
            return;
        }

        this.applyGravity();
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (!this.level().isClientSide() && (this.onGround() || this.tickCount > 600)) {
            this.damageArea();
            this.discard();
        }

        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
    }

    private void damageArea() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        this.playSound(ACSounds.LOD_BLOCK_FALL.get(), 1.0F, 0.4F + this.random.nextFloat() * 0.2F);

        AABB area = this.getBoundingBox().inflate(2.0D, 1.0D, 2.0D);

        for (LivingEntity target : serverLevel.getEntitiesOfClass(LivingEntity.class, area, e -> e != this.owner && e.isAlive() && this.canTarget(e))) {
            DamageSources.applyDamage(target, this.getDamage(), EMSpellRegistries.COLLAPSE.get().getDamageSource(this, this.owner));
        }

        ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("lod_falling_block"));
        AAALevel.addParticle(serverLevel, info.position(this.position().add(0.0D, 1.0D, 0.0D)).scale(2.5F));
    }

    private boolean canTarget(LivingEntity target) {
        if (this.owner instanceof Mob mob) {
            return mob.canAttack(target);
        }
        return true;
    }
}