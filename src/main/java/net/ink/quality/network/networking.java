package net.ink.quality.network;

import net.ink.quality.quality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class networking {

    private static SimpleChannel INSTANCE;
    private static int packetId = 0;

    private static int id() {

        return packetId++;

    }

    public static void register() {

        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(quality.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(NameTagUpdatePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(NameTagUpdatePacket::new)
                .encoder(NameTagUpdatePacket::toBytes)
                .consumerMainThread(NameTagUpdatePacket::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG message) {

        INSTANCE.sendToServer(message);

    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer player) {

        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);

    }

}
