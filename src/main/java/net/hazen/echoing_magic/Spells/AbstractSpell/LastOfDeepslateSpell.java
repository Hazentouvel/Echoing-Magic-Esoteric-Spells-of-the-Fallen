package net.hazen.echoing_magic.Spells.AbstractSpell;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.hazen.echoing_magic.Registries.EMItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import static net.acetheeldritchking.aces_spell_utils.utils.ASUtils.isValidConsumableUnlockItemInInventory;
import static net.acetheeldritchking.aces_spell_utils.utils.ASUtils.isValidUnlockItemInInventory;

public abstract class LastOfDeepslateSpell extends AbstractSpell {

    @Override
    public Component getLockedMessage() {
        return Component.translatable("ui.echoing_magic.last_of_deepslate_spell");
    }

    @Override
    public boolean allowLooting() {
        return false;
    }

    @Override
    public boolean canBeCraftedBy(Player player) {
        Item echoedManuscript = EMItemRegistry.ECHOED_MANUSCRIPT.get();
        return isValidUnlockItemInInventory(echoedManuscript, player);
    }
}