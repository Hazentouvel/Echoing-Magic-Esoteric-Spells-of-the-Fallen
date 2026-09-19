package net.hazen.echoing_magic.Spells.Schools.Cosmic;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACItems;
import com.ratrod.archaion.registry.ACSounds;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.firebolt.FireboltProjectile;
import java.util.List;
import java.util.Optional;

import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.hazen.echoing_magic.EchoingMagic;
import net.hazen.echoing_magic.Entities.Spells.EchoStar.ExtendedEchoStar;
import net.hazen.echoing_magic.Registries.EMEntityRegistry;
import net.hazen.echoing_magic.Registries.EMItemRegistry;
import net.hazen.hazentouvelib.Registries.HLSchoolRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import static net.acetheeldritchking.aces_spell_utils.utils.ASUtils.isValidUnlockItemInInventory;

public class EchoStarSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "echo_star");
    private final DefaultConfig defaultConfig;

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", new Object[]{Utils.stringTruncation((double)this.getDamage(spellLevel, caster), 2)}));
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

    public EchoStarSpell() {
        this.defaultConfig = (new DefaultConfig())
                .setMinRarity(SpellRarity.EPIC)
                .setSchoolResource(HLSchoolRegistry.COSMIC_RESOURCE)
                .setMaxLevel(10)
                .setCooldownSeconds((double)1.0F)
                .build();
        this.manaCostPerLevel = 8;
        this.baseSpellPower = 20;
        this.spellPowerPerLevel = 10;
        this.castTime = 0;
        this.baseManaCost = 75;
    }

    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent) ACSounds.LOD_SHOOT.get());
    }

    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!world.isClientSide()) {
            Vec3 center = entity.position().add(0.0D, entity.getEyeHeight(), 0.0D);

            ParticleEmitterInfo info = new ParticleEmitterInfo(Archaion.prefix("echo_blast"));
            AAALevel.addParticle(world, info.position(center).scale(6.0F));

            for (int i = 0; i < 5; ++i) {

                ExtendedEchoStar projectile = EMEntityRegistry.ECHO_STAR_SPELL.get().create(world);
                if (projectile == null) {
                    continue;
                }

                projectile.setPos(entity.position().add(0.0D, entity.getEyeHeight() - projectile.getBoundingBox().getYsize() * 0.5D, 0.0D));
                projectile.setOwner(entity);

                float xR = (-1.0F + entity.getRandom().nextFloat() * 2.0F) * 15.0F;
                float yR = (-1.0F + entity.getRandom().nextFloat() * 2.0F) * 20.0F;

                projectile.shootFromRotation(entity, entity.getXRot() + xR, entity.getYRot() + yR, 0.0F, 1.5F, 0.0F);
                projectile.setDamage(
                        this.getDamage(spellLevel, entity)
                );

                world.addFreshEntity(projectile);
            }
        }

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) * 0.5F;
    }
}
