package net.hazen.echoing_magic.Items.Armor.ReinforcedJuggernaut;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.hazen.echoing_magic.EchoingMagic;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ReinforcedJuggernautArmorModel extends DefaultedEntityGeoModel<ReinforcedJuggernautArmor> {
    public ReinforcedJuggernautArmorModel() {
        super(ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, ""));
    }

    @Override
    public ResourceLocation getModelResource(ReinforcedJuggernautArmor animatable) {
        return ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "geo/armor/reinforced_juggernaut_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ReinforcedJuggernautArmor animatable) {
        return ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "textures/armor/reinforced_juggernaut_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ReinforcedJuggernautArmor animatable) {
        return ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "animations/wizard_armor_animation.json");
    }
}