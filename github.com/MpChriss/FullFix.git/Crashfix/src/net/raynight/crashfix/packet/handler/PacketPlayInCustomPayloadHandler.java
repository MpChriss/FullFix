package net.raynight.crashfix.packet.handler;

import java.lang.reflect.Field;

import org.bukkit.entity.Player;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInCustomPayload;

public class PacketPlayInCustomPayloadHandler extends PacketHandler {
	
	public boolean handlePacket(Packet<?> packet, Player player) {
		
		if(packet instanceof PacketPlayInCustomPayload) {
			
			PacketPlayInCustomPayload packetPlayInCustomPayload = (PacketPlayInCustomPayload) packet;
			if(packetPlayInCustomPayload.b().writerIndex() > Short.MAX_VALUE) return false;
			if(packetPlayInCustomPayload.a().equals("MC|BEdit") || packetPlayInCustomPayload.a().equals("MC|BSign")) {
			
				try {
				
					Field packetBufferField = PacketDataSerializer.class.getDeclaredField("a");
					packetBufferField.setAccessible(true);
					ByteBuf byteBuf = (ByteBuf) packetBufferField.get(packetPlayInCustomPayload.b());
					if(byteBuf.capacity() > 8000) {
						return false;
					}
				
				} catch (NoSuchFieldException e) { 
					e.printStackTrace(); 
				} catch (SecurityException e) { 
					e.printStackTrace(); 
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
		return true;
		
	}

}
