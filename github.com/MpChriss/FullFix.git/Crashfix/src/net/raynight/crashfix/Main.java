package net.raynight.crashfix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.raynight.crashfix.event.PlayerJoinEventListener;
import net.raynight.crashfix.packet.PacketListener;
import net.raynight.crashfix.packet.handler.PacketHandler.PacketHandlerRegistry;
import net.raynight.crashfix.packet.handler.PacketPlayInBlockPlaceHandler;
import net.raynight.crashfix.packet.handler.PacketPlayInCustomPayloadHandler;
import net.raynight.crashfix.packet.handler.PacketPlayInWindowClickHandler;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

	public static PacketListener packetListener;
	public static Main instance;
	
	@Override
	public void onEnable() {
		
		instance = this;
		packetListener = new PacketListener();
		
		Bukkit.getPluginManager().registerEvents( new PlayerJoinEventListener(), this );
		this.getCommand("fullfix").setExecutor(this);
		
		PacketHandlerRegistry.addHandler(new PacketPlayInCustomPayloadHandler());
		PacketHandlerRegistry.addHandler(new PacketPlayInWindowClickHandler());
		PacketHandlerRegistry.addHandler(new PacketPlayInBlockPlaceHandler());
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(ChatColor.GREEN.toString() + this.getDescription().getName() + " " + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors().get(0));
		return false;
	}
	
}
