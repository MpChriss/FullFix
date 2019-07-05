package net.raynight.crashfix.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.Packet;
import net.raynight.crashfix.Main;

public class PacketLogger {

	public static void logPacket(Packet<?> packet, Player player, String text) {

//		try {
//			
//			File dataFolder = Main.instance.getInstance().getDataFolder();
//			if (!dataFolder.exists()) dataFolder.mkdir();
//
//			File saveTo = new File(Main.instance.getDataFolder(), "PacketLog.txt");
//			if (!saveTo.exists()) saveTo.createNewFile();
//
//			FileWriter fw = new FileWriter(saveTo, true);
//			PrintWriter pw = new PrintWriter(fw);
//
//			pw.println("PACKET: " + packet.getClass().getSimpleName() + " --- PLAYER: " + player.getName() + " --- " + text);
//			pw.flush();
//			pw.close();
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//
//		}

	}

}
