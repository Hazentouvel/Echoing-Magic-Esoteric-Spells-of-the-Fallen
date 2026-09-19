package net.hazen.echoing_magic.Registries;

import net.hazen.echoing_magic.EchoingMagic;
import net.hazen.echoing_magic.Entities.Mobs.Summons.Brave.SummonBrave;
import net.hazen.echoing_magic.Entities.Mobs.Summons.Grimoray.ConjureGrimoray;
import net.hazen.echoing_magic.Entities.Spells.EchoStar.ExtendedEchoStar;
import net.hazen.echoing_magic.Entities.Spells.FallingBlock.ExtendedLODFallingBlock;
import net.hazen.echoing_magic.Entities.Spells.InterceptBlast.ExtendedLODEchoBlast;
import net.hazen.echoing_magic.Entities.Spells.SlamEffect.ExtendedLODSlamEffect;
import net.hazen.echoing_magic.Entities.Spells.SlamEffect.EchoSlamShockwave;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EMEntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister
            .create(Registries.ENTITY_TYPE, EchoingMagic.MOD_ID);

    /*
    *** Spells
     */

    // Echoing Blast Spell
    public static final DeferredHolder<EntityType<?>, EntityType<ExtendedLODEchoBlast>> ECHOING_BLAST_SPELL =
            ENTITIES.register("echoing_blast_spell", () -> EntityType.Builder.<ExtendedLODEchoBlast>of(ExtendedLODEchoBlast::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(4)
                    .build(ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "echoing_blast_spell").toString())
            );

    // Slam Spell
    public static final DeferredHolder<EntityType<?>, EntityType<ExtendedLODSlamEffect>> SLAM_SPELL =
            ENTITIES.register("slam_spell", () -> EntityType.Builder.<ExtendedLODSlamEffect>of(ExtendedLODSlamEffect::new, MobCategory.MISC)
                    .sized(0.1F, 0.1F)
                    .noSave()
                    .noSummon()
                    .clientTrackingRange(4)
                    .build(ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "slam_spell").toString())
            );

    // Echo Slam Shockwave
    public static final DeferredHolder<EntityType<?>, EntityType<EchoSlamShockwave>> ECHO_SLAM_SHOCKWAVE =
            ENTITIES.register("echo_slam_shockwave", () -> EntityType.Builder.<EchoSlamShockwave>of(EchoSlamShockwave::new, MobCategory.MISC)
                    .sized(4.0F, 0.8F)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "echo_slam_shockwave").toString())
            );

    // Echo Star Spell
    public static final DeferredHolder<EntityType<?>, EntityType<ExtendedEchoStar>> ECHO_STAR_SPELL =
            ENTITIES.register("echo_star_spell", () -> EntityType.Builder.<ExtendedEchoStar>of(ExtendedEchoStar::new, MobCategory.MISC)
                    .sized(1.5F, 1.5F)
                    .noSave()
                    .build(ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "echo_star_spell").toString())
            );

    // Falling Block Spell
    public static final DeferredHolder<EntityType<?>, EntityType<ExtendedLODFallingBlock>> FALLING_BLOCK_SPELL =
            ENTITIES.register("falling_block_spell", () -> EntityType.Builder.<ExtendedLODFallingBlock>of(ExtendedLODFallingBlock::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(8)
                    .noSave()
                    .noSummon()
                    .build(ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "falling_block_spell").toString())
            );


    /*
    *** Summons
     */

    // Conjure Grimoray
    public static final DeferredHolder<EntityType<?>, EntityType<ConjureGrimoray>> CONJURE_GRIMORAY =
            ENTITIES.register("conjure_grimoray", () -> EntityType.Builder.<ConjureGrimoray>of(ConjureGrimoray::new, MobCategory.MISC)
                    .sized(0.9F, 1.0F)
                    .eyeHeight(1.6F)
                    .clientTrackingRange(8)
                    .build(ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "conjure_grimoray").toString())
            );

    // Summon Brave
    public static final DeferredHolder<EntityType<?>, EntityType<SummonBrave>> SUMMON_BRAVE =
            ENTITIES.register("summon_brave", () -> EntityType.Builder.<SummonBrave>of(SummonBrave::new, MobCategory.MISC)
                    .sized(1.0F, 1.77F)
                    .eyeHeight(1.3452F)
                    .clientTrackingRange(10)
                    .build(ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "summon_brave").toString())
            );


    public static void register(IEventBus eventBus)
    {
        ENTITIES.register(eventBus);
    }
}