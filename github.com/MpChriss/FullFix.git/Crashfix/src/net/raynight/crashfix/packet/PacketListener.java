package net.raynight.crashfix.packet;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R3.Packet;
import net.raynight.crashfix.Main;

public class PacketListener {

	public List<PacketReader> listening;
	public BukkitTask windowClickCheckerTask;
	
	public PacketListener() {
		listening = new ArrayList<PacketListener.PacketReader>();
		windowClickCheckerTask = new BukkitRunnable() {
			
			@Override
			public void run() {
				listening.forEach(packetReader -> packetReader.windowClickCount = 0);			
			}
			
		}.runTaskTimer(Main.instance, 0, 20);
	}
	
	public boolean isListeningFor(Player player) {
		for(PacketReader packetReader : listening) {
			if(packetReader.player == player) return true;
		}
		return false;
	}
	
	public PacketReader getPacketReader(Player player) {
		for(PacketReader packetReader : listening) {
			if(packetReader.player == player) return packetReader;
		}
		return null;
	}
	
	public static abstract class PacketReader {
		
		public Player player;
		public Channel channel;
		public long windowClickCount;

		public PacketReader( Player player ) {
			windowClickCount = 0L;
			this.player = player;
			injectDecoder();
		}
		
		public void injectDecoder() {
			this.channel = ((CraftPlayer) this.player).getHandle().playerConnection.networkManager.channel;
			this.channel.pipeline().addAfter("decoder", "PacketReader221", new MessageToMessageDecoder<Packet<?>>() {

				@Override
				protected void decode(ChannelHandlerContext arg0, Packet<?> packet, List<Object> packets) throws Exception {
					if(packet instanceof Packet) packetSent(packet, packets);
				}
				
			});
		}
		
		public abstract void packetSent(Packet<?> packet, List<Object> packets);
		
	}
	
}
