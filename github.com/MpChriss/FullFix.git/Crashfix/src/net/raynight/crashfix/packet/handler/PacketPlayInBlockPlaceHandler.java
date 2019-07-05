package net.raynight.crashfix.packet.handler;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.raynight.crashfix.Main;
import net.raynight.crashfix.packet.PacketListener.PacketReader;

public class PacketPlayInBlockPlaceHandler extends PacketHandler {

	@Override
	public boolean handlePacket(Packet<?> packet, Player player) {

		if (packet.getClass().getSimpleName().equals("PacketPlayInBlockPlace")) {

			PacketPlayInBlockPlace packetPlayInBlockPlace = (PacketPlayInBlockPlace) packet;
			if (packetPlayInBlockPlace.getItemStack() != null) {
				PacketReader packetReader = Main.packetListener.getPacketReader(player);
				if (packetReader != null) {
					packetReader.windowClickCount++;
					if (packetReader.windowClickCount > 400) return false;
					
				}

			}

			return true;
		}

		return true;
	}

}
