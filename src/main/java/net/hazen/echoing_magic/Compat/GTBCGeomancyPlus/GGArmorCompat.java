package net.hazen.echoing_magic.Compat.GTBCGeomancyPlus;

import com.gametechbc.gtbcs_geomancy_plus.api.init.GGAttributes;
import net.hazen.echoing_magic.Compat.ArmorCompat;
import net.hazen.echoing_magic.EchoingMagic;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.fml.ModList;

public class GGArmorCompat extends ArmorCompat {
    public static boolean LOADED;
    public static void init() {
        LOADED = ModList.get().isLoaded("gtbcs_geomancy_plus");
    }

    public static void addGeomancySpellPowerPure(ItemAttributeModifiers.Builder attributes, EquipmentSlotGroup group) {
        if (LOADED) {
            LoadedOnly.addGeomancySpellPowerPure(attributes, group);
        }
    }

    public static class LoadedOnly {

        public static void addGeomancySpellPowerPure(ItemAttributeModifiers.Builder attributes, EquipmentSlotGroup group) {
            String armor = getArmorName(group);
            if (armor == null) {
                return;
            }

            attributes.add(GGAttributes.GEO_SPELL_POWER,
                    new AttributeModifier(EchoingMagic.id("add_geomancy_spellpower_" + armor),
                            0.15f,
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    group);
        }


    }

}