package net.hazen.echoing_magic.Spells.Schools.Cosmic;

import com.ratrod.archaion.registry.ACItems;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;

import net.hazen.echoing_magic.EchoingMagic;
import net.hazen.echoing_magic.Entities.Spells.FallingBlock.ExtendedLODFallingBlock;
import net.hazen.echoing_magic.Registries.EMEntityRegistry;
import net.hazen.hazentouvelib.Registries.HLSchoolRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.jetbrains.annotations.Nullable;

import static net.acetheeldritchking.aces_spell_utils.utils.ASUtils.isValidUnlockItemInInventory;

public class CollapseSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID,  "collapse");
    private final DefaultConfig defaultConfig;

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 2))
        );
    }

    @Override
    public Component getLockedMessage() {
        return Component.translatable("ui.echoing_magic.brave_rod_spell");
    }

    @Override
    public boolean allowLooting() {
        return false;
    }

    @Override
    public boolean canBeCraftedBy(Player player) {
        Item echoedManuscript = ACItems.BRAVE_ROD.get();
        return isValidUnlockItemInInventory(echoedManuscript, player);
    }

    public CollapseSpell() {
        this.defaultConfig = (new DefaultConfig())
                .setMinRarity(SpellRarity.EPIC)
                .setSchoolResource(HLSchoolRegistry.COSMIC_RESOURCE)
                .setMaxLevel(5)
                .setCooldownSeconds(16)
                .build();
        this.manaCostPerLevel = 45;
        this.baseSpellPower = 25;
        this.spellPowerPerLevel = 5;
        this.castTime = 10;
        this.baseManaCost = 90;
    }

    public boolean canBeInterrupted(Player player) {
        return false;
    }

    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
        return this.getCastTime(spellLevel);
    }

    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    public CastType getCastType() {
        return CastType.LONG;
    }

    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent)SoundRegistry.EARTHQUAKE_CAST.get());
    }

    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return spellLevel;
    }

    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!playerMagicData.getPlayerCooldowns().isOnCooldown(this)
                && !playerMagicData.getPlayerRecasts().hasRecastForSpell(this.getSpellId())) {
            playerMagicData.getPlayerRecasts().addRecast(new RecastInstance(this.getSpellId(), spellLevel, this.getRecastCount(spellLevel, entity), 80, castSource, (ICastDataSerializable) null),
                    playerMagicData
            );
        }

        float radius = this.getRadius(spellLevel, entity);
        float range = 1.7F;

        Vec3 hitLocation = Utils.moveToRelativeGroundLevel(level, Utils.raycastForBlock(level,
                        entity.getEyePosition(),
                        entity.getEyePosition().add(entity.getForward()
                                        .multiply((double) range, 0.0D, (double) range)), Fluid.NONE).getLocation(), 4);

        int blockCount = 10 + (spellLevel - 1) * 2;

        if (!level.isClientSide()) {
            for (int i = 0; i < blockCount; i++) {
                double angle = level.random.nextDouble() * Math.PI * 2.0D;
                double distance = Math.sqrt(level.random.nextDouble()) * 10.0D;
                double x = hitLocation.x + Math.cos(angle) * distance;
                double z = hitLocation.z + Math.sin(angle) * distance;
                int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, (int) Math.floor(x), (int) Math.floor(z));
                double y = groundY + 5.0D + level.random.nextDouble() * 8.0D;

                ExtendedLODFallingBlock fallingBlock = new ExtendedLODFallingBlock(EMEntityRegistry.FALLING_BLOCK_SPELL.get(), x, y, z, level);

                fallingBlock.setOwner(entity);
                fallingBlock.setDamage(this.getDamage(spellLevel, entity));
                fallingBlock.setDeltaMovement(new Vec3(0.0D, -0.1D - level.random.nextDouble() * 0.15D, 0.0D));
                level.addFreshEntity(fallingBlock);
            }
        }

        CameraShakeManager.addCameraShake(new CameraShakeData(level, 20 + (int) radius, hitLocation, radius * 2.0F + 5.0F));

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) * 0.4F;
    }

    private float getRadius(int spellLevel, LivingEntity entity) {
        return 8.0F;
    }

    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.STOMP;
    }

    public boolean shouldAIStopCasting(int spellLevel, Mob mob, LivingEntity target) {
        float range = this.getRadius(spellLevel, mob) * 1.1F;
        return Utils.raycastForBlock(mob.level, mob.position(), mob.position().subtract((double)0.0F, (double)0.5F, (double)0.0F), Fluid.NONE).getType() == Type.MISS || target.distanceToSqr(mob) > (double)(range * range);
    }
}
