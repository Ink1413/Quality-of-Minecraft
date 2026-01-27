package net.ink.quality.commands.back;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class jailor {

    private static final Map<UUID, BackLocation> lastLocations = new HashMap<>();

    public static void setLastLocation(UUID playerId, ResourceKey<Level> dimension, BlockPos pos, float yaw, float pitch) {
        lastLocations.put(playerId, new BackLocation(dimension, pos, yaw, pitch));
    }

    public static BackLocation getLastLocation(UUID playerId) {
        return lastLocations.get(playerId);
    }

    public static boolean hasLastLocation(UUID playerId) {
        return lastLocations.containsKey(playerId);
    }

    public static void clearLastLocation(UUID playerId) {
        lastLocations.remove(playerId);
    }

    public static class BackLocation {
        private final ResourceKey<Level> dimension;
        private final BlockPos position;
        private final float yaw;
        private final float pitch;

        public BackLocation(ResourceKey<Level> dimension, BlockPos position, float yaw, float pitch) {
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
