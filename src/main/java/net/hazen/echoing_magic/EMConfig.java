package net.hazen.echoing_magic;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(
        modid = "echoing_magic",
        bus = EventBusSubscriber.Bus.MOD
)
public class EMConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.ConfigValue<Float> REINFORCED_JUGGERNAUT_EXPLOSION_RESIST;
    private static final ModConfigSpec.ConfigValue<Integer> IMPACT_AUGMENT_COOLDOWN;
    private static final ModConfigSpec.ConfigValue<Integer> REINFORCED_JUGGERNAUT_COOLDOWN;
    public static float reinforcedJuggernautExplosionResist;
    public static int impactAugmentCooldown;
    public static int reinforcedJuggernautCooldown;
    public static final ModConfigSpec SPEC;

    public EMConfig() {
    }

    @SubscribeEvent
    static void onLoad(ModConfigEvent event)
    {
        // Reinforced Juggernaut
        reinforcedJuggernautExplosionResist = (Float) REINFORCED_JUGGERNAUT_EXPLOSION_RESIST.get();
        reinforcedJuggernautCooldown = (Integer) REINFORCED_JUGGERNAUT_COOLDOWN.get();

        impactAugmentCooldown = (Integer) IMPACT_AUGMENT_COOLDOWN.get();
    }

    static {
        BUILDER.comment("##############################################################################################");
        BUILDER.comment("##                                                                                          ##");
        BUILDER.comment("##                                                                                          ##");
        BUILDER.comment("##                                 I ate the Ice Spider                                     ##");
        BUILDER.comment("##                                                                                          ##");
        BUILDER.comment("##                                                                                          ##");
        BUILDER.comment("##                                                                                          ##");
        BUILDER.comment("##############################################################################################");
        BUILDER.comment("");
        {
            BUILDER.push("Armor Ability");

            REINFORCED_JUGGERNAUT_EXPLOSION_RESIST = BUILDER
                    .comment("Defines the percentage for the Reignforced Juggernaut's explosion resist.")
                    .comment("Default is 30%")
                    .define("Reignforced Juggernaut Explosion Resist", 0.30F);


            REINFORCED_JUGGERNAUT_COOLDOWN = BUILDER
                    .comment("Defines the cooldown in seconds for the Reinforced Juggernaut.")
                    .comment("Default is 20 seconds")
                    .define("Reinforced Juggernaut Cooldown", 20);

            BUILDER.pop();
        }
        {
            BUILDER.push("Curio Ability");

            IMPACT_AUGMENT_COOLDOWN = BUILDER
                    .comment("Defines the cooldown in seconds for the Impact Augment.")
                    .comment("Default is 10 seconds")
                    .define("Impact Augment Cooldown", 10);
            BUILDER.pop();
        }

        SPEC = BUILDER.build();
    }
}
