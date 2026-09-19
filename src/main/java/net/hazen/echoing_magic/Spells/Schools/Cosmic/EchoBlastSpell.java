package net.hazen.echoing_magic.Spells.Schools.Cosmic;

import com.ratrod.archaion.registry.ACSounds;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.hazen.echoing_magic.EchoingMagic;
import net.hazen.echoing_magic.Entities.Spells.InterceptBlast.ExtendedLODEchoBlast;
import net.hazen.echoing_magic.Spells.AbstractSpell.LastOfDeepslateSpell;
import net.hazen.hazentouvelib.Registries.HLSchoolRegistry;
import net.hazen.hazentouvelib.Registries.HLSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class EchoBlastSpell extends LastOfDeepslateSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "echo_blast");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 2)),
                Component.translatable("ui.irons_spellbooks.radius", getRadius(spellLevel, caster))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(HLSchoolRegistry.COSMIC_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(30)
            .build();

    public EchoBlastSpell() {
        this.manaCostPerLevel = 20;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 50;
        this.baseManaCost = 100;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(HLSounds.COSMIC_CAST_LONG.get());
    }

    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent) ACSounds.LOD_SHOOT.get());
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 origin = entity.getEyePosition();

        ExtendedLODEchoBlast interceptBlast = new ExtendedLODEchoBlast(world, entity);

        interceptBlast.setDamage(getDamage(spellLevel, entity));
        interceptBlast.setPos(origin.add(entity.getForward()).subtract(0, interceptBlast.getBbHeight() / 2, 0));
        interceptBlast.shootFromRotation(entity, 0.0F, entity.getYRot(), 0.0F, 1.0F, 0.0F);

        world.addFreshEntity(interceptBlast);

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    public float getDamage(int spellLevel, LivingEntity caster) {
        return 10 + 5 * getSpellPower(spellLevel, caster);
    }

    public int getRadius(int spellLevel, LivingEntity caster) {
        return 2 + (int) getSpellPower(spellLevel, caster);
    }
}