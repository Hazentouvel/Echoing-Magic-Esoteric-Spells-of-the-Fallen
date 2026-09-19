package net.hazen.echoing_magic.Spells;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.hazen.echoing_magic.EchoingMagic;
import net.hazen.echoing_magic.Spells.Schools.Cosmic.*;
import net.hazen.echoing_magic.Spells.Schools.Evocation.ConjureGrimoraySpell;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY;

public class EMSpellRegistries {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, EchoingMagic.MOD_ID);

    public static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    /*
    *** Evocation
     */

    // Conjure Grimoray
    public static final Supplier<AbstractSpell> CONGURE_GRIMORAY = registerSpell(new ConjureGrimoraySpell());

    /*
    *** Cosmic
     */

    // Echo Star
    public static final Supplier<AbstractSpell> ECHO_STAR = registerSpell(new EchoStarSpell());

    // Echoing Explosion
    public static final Supplier<AbstractSpell> ECHOING_EXPLOSION = registerSpell(new EchoingExplosionSpell());

    // Echo Blast
    public static final Supplier<AbstractSpell> ECHO_BLAST = registerSpell(new EchoBlastSpell());

    // Echoing Slam
    public static final Supplier<AbstractSpell> ECHOING_SLAM = registerSpell(new EchoingSlamSpell());

    // Summon Brave
    public static final Supplier<AbstractSpell> SUMMON_BRAVE = registerSpell(new SummonBraveSpell());

    // Collapse
    public static final Supplier<AbstractSpell> COLLAPSE = registerSpell(new CollapseSpell());



    public static void register(IEventBus eventBus)
    {
        SPELLS.register(eventBus);
    }

}