package me.thenewtao.mobslistener;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thenewtao.mobscore.MobsMain;
import net.md_5.bungee.api.ChatColor;

public class ZapEvent implements Listener {

	private Random random = new Random();
	private MobsMain main;

	public ZapEvent(MobsMain main) {
		this.main = main;
	}

	@EventHandler
	public void onPig(PigZapEvent e) {
		final Entity entity = e.getEntity();
		if (!main.isPigEnabled()) {
			return;
		}
		if (entity.getCustomName() != null
				&& entity.getCustomName().equals(ChatColor.GOLD + ChatColor.BOLD.toString() + "Golden Pig")) {
			return;
		}
		String specialMob = ChatColor.BLUE + ChatColor.BOLD.toString() + "Pigs Are Flying";
		entity.setCustomName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Golden Pig");
		e.setCancelled(true);
		new BukkitRunnable() {
			int counter = 0;

			public void run() {
				int randomInteger = random.nextInt(40) - 20;
				int randomIntegerZ = random.nextInt(40) - 20;
				Location loc = new Location(entity.getWorld(), entity.getLocation().getX() + randomInteger,
						entity.getLocation().getY() + 30, entity.getLocation().getZ() + randomIntegerZ);
				entity.getWorld().spawnEntity(loc, EntityType.PIG);
				counter++;
				if (counter >= 60) {
					this.cancel();
				}
			}
		}.runTaskTimer(main, 0L, 2L);
		entity.remove();
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (online.getWorld().equals(entity.getWorld())
					&& online.getLocation().distance(entity.getLocation()) < 50) {
				String first = main.getMessageLightning().replace("<creature>", specialMob);
				String message = ChatColor.translateAlternateColorCodes('&', first);
				online.playSound(online.getLocation(), Sound.SUCCESSFUL_HIT, 16, 1);
				online.sendMessage(main.getPrefix() + " " + message);
			}
		}
	}
}
