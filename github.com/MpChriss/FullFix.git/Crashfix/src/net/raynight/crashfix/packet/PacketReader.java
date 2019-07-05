package net.raynight.crashfix.packet;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.ItemWrittenBook;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInCustomPayload;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;
import net.raynight.crashfix.logger.PacketLogger;

public class PacketReader {

	public static String currentPlayer;
	
	Player player;
	Channel channel;
	long windowClickCount;

	public PacketReader( Player player ) {
		windowClickCount = 0L;
		this.player = player;
		inject();
	}

	public void inject() {
		
		CraftPlayer craftPlayer = (CraftPlayer) this.player;
		
		this.channel = craftPlayer.getHandle().playerConnection.networkManager.channel;
		this.channel.pipeline().addAfter("decoder", "PacketInjector1", (ChannelHandler) new MessageToMessageDecoder<Object>() {

			@Override
			protected void decode(ChannelHandlerContext channelHandlerContext, Object object, List<Object> objects) throws Exception {
				
				if(object instanceof Packet) {
					
					Packet<?> packet = (Packet<?>) object;
					SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
					df.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
					PacketLogger.logPacket(packet, player, df.format(new Date()));
					if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInBlockPlace")) {
						PacketPlayInBlockPlace packetPlayInBlockPlace = ( PacketPlayInBlockPlace ) packet;
						if(packetPlayInBlockPlace.getItemStack() != null) {
							if( packetPlayInBlockPlace.getItemStack().getItem() instanceof ItemWrittenBook ) {
								packetPlayInBlockPlace.getItemStack().getTag().remove( "pages" );
								packetPlayInBlockPlace.getItemStack().getTag().remove( "author" );
								packetPlayInBlockPlace.getItemStack().getTag().remove( "title" );
								currentPlayer = player.getName();
								PacketReader.this.channel.close();
								Bukkit.getOnlinePlayers().stream().filter(a -> a.isOp()).forEach(PacketReader::notify);
								
							} else {
								
								objects.add(packet);
								
							}
							
						} else {
							
							objects.add(packet);
							
						}
					
					} else if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInWindowClick")){
						
						PacketPlayInWindowClick packetPlayInWindowClick = (PacketPlayInWindowClick) packet;
						if(!getWindowItem(packetPlayInWindowClick)) objects.add(packet);
						
					} else if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInCustomPayload")) {
						
						PacketPlayInCustomPayload packetPlayInCustomPayload = (PacketPlayInCustomPayload) packet;
						if(packetPlayInCustomPayload.b().writerIndex() > 32767) return;
						if(packetPlayInCustomPayload.a().equals("MC|BEdit")) {
							Field aField = PacketDataSerializer.class.getDeclaredField("a");
							aField.setAccessible(true);
							ByteBuf byteBuf = (ByteBuf) aField.get(packetPlayInCustomPayload.b());
							if(byteBuf.capacity() > 8000) {
								currentPlayer = player.getName();
								PacketReader.this.channel.close();
								Bukkit.getOnlinePlayers().stream().filter(a -> a.isOp()).forEach(PacketReader::notify);
							}
						} 
						
						objects.add(packet);
						
					} else {
						objects.add(packet);
					}
					
				}
				
			}
			
		});
		
	}
	
	public void eject() {
		if( this.channel.pipeline().get( "PacketInjector1" ) != null ) this.channel.pipeline().remove( "PacketInjector1" );
	}

	public boolean getWindowItem( final PacketPlayInWindowClick packet ) {
		try {
			Field itemField = null;
			itemField = PacketPlayInWindowClick.class.getDeclaredField( "item" );
			itemField.setAccessible( true );
			final ItemStack item = ( ItemStack ) itemField.get( packet );
			if( item.getItem() instanceof ItemWrittenBook ) {
				item.getTag().remove( "pages" );
				item.getTag().remove( "author" );
				item.getTag().remove( "title" );
				currentPlayer = player.getName();
				PacketReader.this.channel.close();
				Bukkit.getOnlinePlayers().stream().filter(a -> a.isOp()).forEach(PacketReader::notify);
				return true;
			}
		} catch( Exception ex ) {
			return false;
		}
		return false;
	}

	public static void notify(Player p) {
		p.sendMessage("§8§m|§r §cSpieler §4" + currentPlayer + " §chat versucht den Server zu crashen."); 
	}
	
}
