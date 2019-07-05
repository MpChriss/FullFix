package net.raynight.crashfix.packet.handler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.Packet;

public abstract class PacketHandler {
	
	public abstract boolean handlePacket(Packet<?> packet, Player player);
	
	public static class PacketHandlerRegistry {
	
		public static List<PacketHandler> packetHandlers = new ArrayList<PacketHandler>();
		
		public static void addHandler(PacketHandler handler) {
			packetHandlers.add(handler);
		}
		
	}

}
