package io.github.meatwo310.dousiyopolice.config;

import io.github.meatwo310.dousiyopolice.DousiyoPolice;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = DousiyoPolice.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> HANDCUFFS_ALLOWED;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> HANDCUFFS_COORDINATES_SETTER;
    public static final ForgeConfigSpec.IntValue HANDCUFFS_OP_PERMISSION_LEVEL;
    public static final ForgeConfigSpec.BooleanValue HANDCUFFS_PLAYER_ONLY;

    private static volatile TeamsAndTags ALLOWED_CACHE;
    private static volatile TeamsAndTags COORDINATES_SETTER_CACHE;


    static {
        BUILDER.push("handcuffs");
        HANDCUFFS_ALLOWED = BUILDER
                .comment("A list of teams (or tags if prefixed with '#') that are allowed to use the handcuffs.")
                .define("allowed", List.of("police", "admin"));
        HANDCUFFS_COORDINATES_SETTER = BUILDER
                .comment("A list of teams/tags that can set the teleport coordinates for the handcuffs.")
                .define("coordinatesSetter", List.of("admin"));
        HANDCUFFS_OP_PERMISSION_LEVEL = BUILDER
                .comment(
                        "Minimum operator permission level required to bypass team/tag restrictions.",
                        "Players with this level or higher can always use handcuffs.",
                        "Set to -1 to disable this feature."
                ).defineInRange("opPermissionLevel", 4, -1, 4);
        HANDCUFFS_PLAYER_ONLY = BUILDER
                .comment("If true, handcuffs can only be used on players.")
                .define("playerOnly", true);
        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static TeamsAndTags getAllowed() {
        return ALLOWED_CACHE;
    }

    public static TeamsAndTags getCoordinatesSetter() {
        return COORDINATES_SETTER_CACHE;
    }

    @SubscribeEvent
    public static void onModConfig(ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;
        ALLOWED_CACHE = new TeamsAndTags(HANDCUFFS_ALLOWED.get());
        COORDINATES_SETTER_CACHE = new TeamsAndTags(HANDCUFFS_COORDINATES_SETTER.get());
    }

    public record TeamsAndTags(Set<String> teams, Set<String> tags) {
        public TeamsAndTags(List<? extends String> configList) {
            this(
                    configList.stream()
                            .filter(s -> !s.startsWith("#"))
                            .collect(Collectors.toUnmodifiableSet()),
                    configList.stream()
                            .filter(s -> s.startsWith("#"))
                            .map(s -> s.substring(1))
                            .collect(Collectors.toUnmodifiableSet())
            );
        }

        public boolean check(Player player) {
            return checkPermissionLevel(player) || checkTeam(player) || checkTag(player);
        }

        public boolean checkPermissionLevel(Player player) {
            int opLevel = HANDCUFFS_OP_PERMISSION_LEVEL.get();
            return opLevel >= 0 && player.hasPermissions(opLevel);
        }

        public boolean checkTeam(Player player) {
            if (player.getTeam() == null) return false;
            return teams.contains(player.getTeam().getName());
        }

        public boolean checkTag(Player player) {
            return !Collections.disjoint(player.getTags(), tags);
        }
    }
}
