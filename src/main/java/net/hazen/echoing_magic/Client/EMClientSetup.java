package net.hazen.echoing_magic.Client;

import com.ratrod.archaion.client.renderers.*;
import net.hazen.echoing_magic.EchoingMagic;
import net.hazen.echoing_magic.Registries.EMEntityRegistry;
import net.hazen.echoing_magic.Utils.EMSpellTooltip;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

@EventBusSubscriber(modid = EchoingMagic.MOD_ID)
public class EMClientSetup {

    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {

        /*
        *** Spells
         */
        event.registerEntityRenderer(EMEntityRegistry.ECHOING_BLAST_SPELL.get(), LODInterceptBlastRenderer::new);
        event.registerEntityRenderer(EMEntityRegistry.ECHO_STAR_SPELL.get(), EchoStarProjectileRenderer::new);
        event.registerEntityRenderer(EMEntityRegistry.SLAM_SPELL.get(), LODSlamRenderer::new);
        event.registerEntityRenderer(EMEntityRegistry.ECHO_SLAM_SHOCKWAVE.get(), NoopRenderer::new);
        event.registerEntityRenderer(EMEntityRegistry.FALLING_BLOCK_SPELL.get(), LODFallingBlockRenderer::new);

        /*
        *** Summons
         */
        event.registerEntityRenderer(EMEntityRegistry.CONJURE_GRIMORAY.get(), GrimorayRenderer::new);
        event.registerEntityRenderer(EMEntityRegistry.SUMMON_BRAVE.get(), BraveRenderer::new);

    }

    /*

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event)
    {

        event.registerSpriteSet(HnSParticleRegistry.SOUL_PARTICLE.get(), SoulParticle.Provider::new);
    }
     */

    @SubscribeEvent
    public static void onRegisterTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(EMSpellTooltip.EMSpellTooltipData.class, EMSpellTooltip::new);
    }
}