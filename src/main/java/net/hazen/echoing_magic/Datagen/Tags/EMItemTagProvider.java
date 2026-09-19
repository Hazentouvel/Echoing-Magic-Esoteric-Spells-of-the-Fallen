package net.hazen.echoing_magic.Datagen.Tags;

import com.ratrod.archaion.registry.ACItems;
import net.hazen.echoing_magic.EchoingMagic;
import net.hazen.echoing_magic.Registries.EMItemRegistry;
import net.hazen.hazentouvelib.Datagen.HLTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMItemTagProvider extends ItemTagsProvider {
    public EMItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, EchoingMagic.MOD_ID, existingFileHelper);
    }

    //.add(GGItems.GEO_RUNE.get())

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(HLTags.COSMIC_FOCUS)
                .add(
                        ACItems.ECHO_CHARGE.get()
                )
        ;

        tag(HLTags.PURE_HELMET)
                .add(
                        EMItemRegistry.REINFORCED_JUGGERNAUT_HELMET.get()
                )
        ;

        tag(HLTags.PURE_CHESTPLATE)
                .add(
                        EMItemRegistry.REINFORCED_JUGGERNAUT_CHESTPLATE.get()
                )
        ;

        tag(HLTags.PURE_LEGGINGS)
                .add(
                        EMItemRegistry.REINFORCED_JUGGERNAUT_LEGGINGS.get()
                )

        ;

        tag(HLTags.PURE_BOOTS)
                .add(
                        EMItemRegistry.REINFORCED_JUGGERNAUT_BOOTS.get()
                )

        ;

    }
}