package net.hazen.echoing_magic.Items.Armor.ReinforcedJuggernaut;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hazen.echoing_magic.EchoingMagic;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class ReinforcedJuggernautArmorLayer extends GeoRenderLayer<ReinforcedJuggernautArmor> {
    private static final ResourceLocation LAYER = ResourceLocation.fromNamespaceAndPath(
            EchoingMagic.MOD_ID,
            "textures/armor/reinforced_juggernaut_armor_glowmask.png");

    public ReinforcedJuggernautArmorLayer(GeoRenderer<ReinforcedJuggernautArmor> entityRenderer) {
        super(entityRenderer);
    }

    public void render(PoseStack poseStack, ReinforcedJuggernautArmor animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.eyes(LAYER);
        this.getRenderer()
                .reRender(this.getDefaultBakedModel(animatable),
                        poseStack,
                        bufferSource,
                        animatable,
                        glowRenderType,
                        bufferSource.getBuffer(glowRenderType),
                        partialTick,
                        packedLight,
                        OverlayTexture.NO_OVERLAY,
                        0xFFFFFFFF
                );
    }
}