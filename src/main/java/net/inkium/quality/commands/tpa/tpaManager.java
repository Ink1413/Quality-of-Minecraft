package net.inkium.quality.commands.tpa;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class tpaManager {

    private static final Map<UUID,tpaRequest> pendingTPA = new HashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final int REQUEST_TIMEOUT = 120;

    public static void addedRequest(ServerPlayer sender, ServerPlayer target) {

        tpaRequest request = new tpaRequest(sender.getUUID(), target.getUUID(), sender.getDisplayName().getString());
        pendingTPA.put(target.getUUID(), request);

        scheduler.schedule(() -> {

            if (pendingTPA.containsKey(target.getUUID()) && pendingTPA.get(target.getUUID()).equals(request)) {

                pendingTPA.remove(target.getUUID());

            }

        }, REQUEST_TIMEOUT, TimeUnit.SECONDS);

    }

    public static boolean gotRequest(UUID targetId) {

        return pendingTPA.containsKey(targetId);

    }

    public static tpaRequest getRequest(UUID targetId) {

        return pendingTPA.get(targetId);

    }

    public static tpaRequest removeRequest(UUID targetId) {

        return pendingTPA.remove(targetId);

    }

    public static boolean hasPendingTPAFrom(UUID senderId, UUID targetId) {

        tpaRequest request = pendingTPA.get(targetId);
        return request != null && request.getSenderId().equals(senderId);

    }

    public static class tpaRequest {

        private final UUID senderId;
        private final UUID targetId;
        private final String senderName;
        private final long timestamp;

        public tpaRequest(UUID senderId, UUID targetId, String senderName) {

            this.senderId = senderId;
            this.targetId = targetId;
            this.senderName = senderName;
            this.timestamp = System.currentTimeMillis();

        }

        public UUID getSenderId() {

            return senderId;

        }

        public UUID getTargetId() {

            return targetId;

        }

        public String getSenderName() {

            return senderName;

        }

        public long getTimestamp() {

            return timestamp;

        }

    }


}
