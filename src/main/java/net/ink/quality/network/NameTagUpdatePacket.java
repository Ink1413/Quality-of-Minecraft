package net.ink.quality.network;

import net.ink.quality.gui.CustomContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NameTagUpdatePacket {

    private final String name;
    private final String lore;

    public NameTagUpdatePacket(String name, String lore) {

        this.name = name;
        this.lore = lore;

    }

    public NameTagUpdatePacket(FriendlyByteBuf buf) {

        this.name = buf.readUtf();
        this.lore = buf.readUtf();

    }

    public void toBytes(FriendlyByteBuf buf) {

        buf.writeUtf(this.name);
        buf.writeUtf(this.lore);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {

        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {

            ServerPlayer player = ctx.getSender();
            if (player != null && player.containerMenu instanceof CustomContainerMenu menu) {

                menu.setTitleText(this.name);
                menu.setSubtitleText(this.lore);

            }

        });

        return true;

    }

}
