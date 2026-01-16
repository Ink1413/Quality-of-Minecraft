package net.inkium.quality.commands.home;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class landlord {

    private static final Map<UUID, HomeLoco> homes = new HashMap<>();

    public static void setHome(UUID playerId, ServerLevel level, BlockPos pos, float yaw, float pitch) {

        homes.put(playerId, new HomeLoco(level.dimension(), pos, yaw, pitch));

    }

    public static HomeLoco getHome(UUID playerId) {

        return homes.get(playerId);

    }

    public static boolean hasHome(UUID playerId) {

        return homes.containsKey(playerId);

    }

    public static void delHome(UUID playerId) {

        homes.remove(playerId);

    }

    public static class HomeLoco {

        private final ResourceKey<Level> dimension;
        private final BlockPos position;
        private final float yaw;
        private final float pitch;

        public HomeLoco(ResourceKey<Level> dimension, BlockPos position, float yaw, float pitch) {

            this.dimension = dimension;
            this.position = position;
            this.yaw = yaw;
            this.pitch = pitch;

        }

        public ResourceKey<Level> getDimension() {

            return dimension;

        }

        public BlockPos getPosition() {

            return position;

        }

        public float getYaw() {

            return yaw;

        }

        public float getPitch() {


            return pitch;

        }

    }

}
