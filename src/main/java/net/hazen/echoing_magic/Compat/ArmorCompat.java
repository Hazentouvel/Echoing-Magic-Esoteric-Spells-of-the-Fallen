package net.hazen.echoing_magic.Compat;

import net.minecraft.world.entity.EquipmentSlotGroup;

public class ArmorCompat {
    public static boolean LOADED;

    public static String getArmorName(EquipmentSlotGroup group) {
        return switch (group) {
            case HEAD -> "helmet";
            case CHEST -> "chestplate";
            case LEGS -> "leggings";
            case FEET -> "boots";
            default -> null;
        };
    }
}