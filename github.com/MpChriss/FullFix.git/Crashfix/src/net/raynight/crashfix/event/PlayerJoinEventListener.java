package net.raynight.crashfix.event;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.minecraft.server.v1_8_R3.Packet;
import net.raynight.crashfix.Main;
import net.raynight.crashfix.packet.PacketListener;
import net.raynight.crashfix.packet.handler.PacketHandler;

public class PlayerJoinEventListener implements Listener {

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent playerJoinEvent) {
		
		if(!Main.packetListener.isListeningFor(playerJoinEvent.getPlayer())) {
			
			Main.packetListener.listening.add(new PacketListener.PacketReader(playerJoinEvent.getPlayer()) {
				
				@Override
				public void packetSent(Packet<?> packet, List<Object> packets) {

					boolean invalid = false;
					for(PacketHandler handler : PacketHandler.PacketHandlerRegistry.packetHandlers) {
						if(!handler.handlePacket(packet, player)) invalid = true;
					}
					
					if(invalid) {
						Main.packetListener.getPacketReader(player).channel.close();
					} 
					
					packets.add(packet);
					
				}
				
			});
			
		}
	}
	
}
