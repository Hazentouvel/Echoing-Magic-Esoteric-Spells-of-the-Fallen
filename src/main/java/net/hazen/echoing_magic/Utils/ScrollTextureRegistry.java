package net.hazen.echoing_magic.Utils;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.hazen.echoing_magic.EchoingMagic;
import net.hazen.echoing_magic.Spells.EMSpellRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ScrollTextureRegistry {

    private static final Map<ResourceLocation, ResourceLocation> SPELL_MODELS =
            new LinkedHashMap<>();

    private ScrollTextureRegistry() {
    }

    public static void register() {
        SPELL_MODELS.clear();


        /*
        *** Echoing
         */
        registerSpell(EMSpellRegistries.ECHO_STAR.get(), "item/echoing_scroll");
        registerSpell(EMSpellRegistries.ECHOING_SLAM.get(), "item/echoing_scroll");
        registerSpell(EMSpellRegistries.ECHO_BLAST.get(), "item/echoing_scroll");
        registerSpell(EMSpellRegistries.ECHOING_EXPLOSION.get(), "item/echoing_scroll");
        registerSpell(EMSpellRegistries.COLLAPSE.get(), "item/echoing_scroll");

        registerSpell(EMSpellRegistries.SUMMON_BRAVE.get(), "item/echoing_scroll");


    }

    private static void registerSpell(
            AbstractSpell spell,
            String modelPath
    ) {
        ResourceLocation spellId = spell.getSpellResource();

        ResourceLocation modelLocation =
                ResourceLocation.fromNamespaceAndPath(
                        EchoingMagic.MOD_ID,
                        modelPath
                );

        SPELL_MODELS.put(spellId, modelLocation);
    }

    public static ResourceLocation getModel(ResourceLocation spellId) {
        return SPELL_MODELS.get(spellId);
    }

    public static Map<ResourceLocation, ResourceLocation> getRegisteredModels() {
        return Collections.unmodifiableMap(SPELL_MODELS);
    }
}