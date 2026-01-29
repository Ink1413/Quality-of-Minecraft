package net.ink.quality.network;

import net.ink.quality.quality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class networking {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("quality", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        INSTANCE.messageBuilder(NameTagUpdatePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(NameTagUpdatePacket::new)
                .encoder(NameTagUpdatePacket::encode)
                .consumerMainThread(NameTagUpdatePacket::handle)
                .add();
    }

    public static void sendToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }

}
