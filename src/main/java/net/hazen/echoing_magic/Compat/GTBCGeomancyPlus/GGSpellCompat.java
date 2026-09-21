package net.hazen.echoing_magic.Compat.GTBCGeomancyPlus;

import com.gametechbc.gtbcs_geomancy_plus.api.init.GGSchools;
import io.redspace.ironsspellbooks.api.config.ModifyDefaultConfigValuesEvent;
import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import net.hazen.echoing_magic.Spells.Schools.Cosmic.CollapseSpell;
import net.hazen.echoing_magic.Spells.Schools.Evocation.SummonBraveSpell;
import net.neoforged.fml.ModList;

public class GGSpellCompat {
    public static boolean LOADED;
    public static void init() {
        LOADED = ModList.get().isLoaded("gtbcs_geomancy_plus");
    }

    public static class LoadedOnly {
        public static void modifySpellSchool(ModifyDefaultConfigValuesEvent event) {

            if (!LOADED) {
                return;
            }

            if (event.getSpell() instanceof SummonBraveSpell) {
                event.setDefaultValue(SpellConfigParameter.SCHOOL, GGSchools.GEO.get());
            }

            if (event.getSpell() instanceof CollapseSpell) {
                event.setDefaultValue(SpellConfigParameter.SCHOOL, GGSchools.GEO.get()
                );
            }
        }

    }

}