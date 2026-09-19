package net.hazen.echoing_magic.Datagen;

import com.ratrod.archaion.registry.ACItems;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.hazen.echoing_magic.EchoingMagic;
import net.hazen.echoing_magic.Registries.EMItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
//import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;

import java.util.concurrent.CompletableFuture;

public class EMRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public EMRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

        /*
        *** Curios
         */

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.ECHO_SHARD),
                        Ingredient.of(ItemRegistry.TELEPORTATION_AMULET.get()),
                        Ingredient.of(ACItems.IMPACT_PEARL),
                        RecipeCategory.COMBAT,
                        EMItemRegistry.IMPACT_AUGMENT.get())
                .unlocks("has_impact_pearl", has(ACItems.IMPACT_PEARL))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "smithing/curios/impact_augment"));


        /*
        *** Armor
         */

        //Aerospec
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, EMItemRegistry.REINFORCED_JUGGERNAUT_HELMET.get())
                .pattern("CEC")
                .pattern("DAD")
                .pattern("   ")
                .define('A', ItemRegistry.NETHERITE_MAGE_HELMET.get())
                .define('C', ACItems.ECHO_CHARGE.get())
                .define('E', EMItemRegistry.ECHOING_ESSENCE.get())
                .define('D', Items.COBBLED_DEEPSLATE)
                .unlockedBy("has_echoing_essnece", has(EMItemRegistry.ECHOING_ESSENCE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "crafting/armor/reinforced_juggernaut/reinforced_juggernaut_helmet"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, EMItemRegistry.REINFORCED_JUGGERNAUT_CHESTPLATE.get())
                .pattern("BAB")
                .pattern("DED")
                .pattern("DCD")
                .define('A', ItemRegistry.NETHERITE_MAGE_CHESTPLATE.get())
                .define('B', ACItems.BRAVE_ESSENCE.get())
                .define('C', ACItems.ECHO_CHARGE.get())
                .define('E', EMItemRegistry.ECHOING_ESSENCE.get())
                .define('D', Items.COBBLED_DEEPSLATE)
                .unlockedBy("has_echoing_essnece", has(EMItemRegistry.ECHOING_ESSENCE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "crafting/armor/reinforced_juggernaut/reinforced_juggernaut_chestplate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, EMItemRegistry.REINFORCED_JUGGERNAUT_LEGGINGS.get())
                .pattern("BEB")
                .pattern("DAD")
                .pattern("D D")
                .define('A', ItemRegistry.NETHERITE_MAGE_LEGGINGS.get())
                .define('B', ACItems.BRAVE_ESSENCE.get())
                .define('E', EMItemRegistry.ECHOING_ESSENCE.get())
                .define('D', Items.COBBLED_DEEPSLATE)
                .unlockedBy("has_echoing_essnece", has(EMItemRegistry.ECHOING_ESSENCE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "crafting/armor/reinforced_juggernaut/reinforced_juggernaut_leggings"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, EMItemRegistry.REINFORCED_JUGGERNAUT_BOOTS.get())
                .pattern("BEB")
                .pattern("DAD")
                .pattern("   ")
                .define('A', ItemRegistry.NETHERITE_MAGE_BOOTS.get())
                .define('B', ACItems.BRAVE_ESSENCE.get())
                .define('E', EMItemRegistry.ECHOING_ESSENCE.get())
                .define('D', Items.COBBLED_DEEPSLATE)
                .unlockedBy("has_echoing_essnece", has(EMItemRegistry.ECHOING_ESSENCE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(EchoingMagic.MOD_ID, "crafting/armor/reinforced_juggernaut/reinforced_juggernaut_boots"));


    }
}