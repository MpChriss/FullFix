package net.raynight.crashfix.packet.handler;

import java.lang.reflect.Field;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.ItemWrittenBook;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;
import net.raynight.crashfix.Main;
import net.raynight.crashfix.packet.PacketListener.PacketReader;

public class PacketPlayInWindowClickHandler extends PacketHandler {

	@Override
	public boolean handlePacket(Packet<?> packet, Player player) {

		if (packet.getClass().getSimpleName().equals("PacketPlayInWindowClick")) {

			PacketPlayInWindowClick packetPlayInWindowClick = (PacketPlayInWindowClick) packet;
			if (getWindowItem(packetPlayInWindowClick) != null) {
				ItemStack item = getWindowItem(packetPlayInWindowClick);
				if (item.getItem() instanceof ItemWrittenBook) {
					PacketReader packetReader = Main.packetListener.getPacketReader(player);
					if (packetReader != null) {
						packetReader.windowClickCount++;
						if (packetReader.windowClickCount > 400) return false;
					}
					
				}

			}

		}
		return true;
	}

	public ItemStack getWindowItem(final PacketPlayInWindowClick packet) {
		try {
			Field itemField = null;
			itemField = PacketPlayInWindowClick.class.getDeclaredField("item");
			itemField.setAccessible(true);
			return (ItemStack) itemField.get(packet);

		} catch (Exception ex) {
			return null;
		}
	}

}
