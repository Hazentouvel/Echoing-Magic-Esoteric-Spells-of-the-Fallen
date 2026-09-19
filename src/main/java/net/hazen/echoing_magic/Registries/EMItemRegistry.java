package net.hazen.echoing_magic.Registries;


import net.hazen.echoing_magic.EchoingMagic;
import net.hazen.echoing_magic.Items.Armor.ReinforcedJuggernaut.ReinforcedJuggernautArmor;
import net.hazen.echoing_magic.Items.Curios.ImpactAugmentItem;
import net.hazen.hazentouvelib.Rarities.HLRarities;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;

public class EMItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EchoingMagic.MOD_ID);

    /*
    *** Materials
     */
    // Echoing Essence
    public static final DeferredItem<Item> ECHOING_ESSENCE = ITEMS.register("echoing_essence",
            () -> new Item(new Item
                    .Properties()
                    .rarity(HLRarities.COSMIC_RARITY.getValue())
                    .fireResistant())
    );

    /*
    *** Curios
     */

    // Impact Augment
    public static final DeferredHolder<Item, Item> IMPACT_AUGMENT = ITEMS.register
            ("impact_augment", ImpactAugmentItem::new);

    /*
    *** Armor Sets
     */

    // Reinforced Juggernaut Armor Set
    public static final DeferredHolder<Item, Item> REINFORCED_JUGGERNAUT_HELMET = ITEMS.register("reinforced_juggernaut_helmet", () -> new ReinforcedJuggernautArmor(ArmorItem.Type.HELMET, new Item.Properties()
            .durability(ArmorItem.Type.HELMET.getDurability(96))
    ));

    public static final DeferredHolder<Item, Item> REINFORCED_JUGGERNAUT_CHESTPLATE = ITEMS.register("reinforced_juggernaut_chestplate", () -> new ReinforcedJuggernautArmor(ArmorItem.Type.CHESTPLATE, new Item.Properties()
            .durability(ArmorItem.Type.CHESTPLATE.getDurability(96))
    ));

    public static final DeferredHolder<Item, Item> REINFORCED_JUGGERNAUT_LEGGINGS = ITEMS.register("reinforced_juggernaut_leggings", () -> new ReinforcedJuggernautArmor(ArmorItem.Type.LEGGINGS, new Item.Properties()
            .durability(ArmorItem.Type.LEGGINGS.getDurability(96))
    ));


    public static final DeferredHolder<Item, Item> REINFORCED_JUGGERNAUT_BOOTS = ITEMS.register("reinforced_juggernaut_boots", () -> new ReinforcedJuggernautArmor(ArmorItem.Type.BOOTS, new Item.Properties()
            .durability(ArmorItem.Type.BOOTS.getDurability(96))
    ));



    public static Collection<DeferredHolder<Item, ? extends Item>> getEMItems()
    {
        return ITEMS.getEntries();
    }

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}