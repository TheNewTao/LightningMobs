package me.thenewtao.mobslistener;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreeper;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.thenewtao.mobscore.MobsMain;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.Explosion;
import net.minecraft.server.v1_8_R3.IBlockData;

public class PowerEvent implements Listener {

	private MobsMain main;

	public PowerEvent(MobsMain main) {
		this.main = main;
	}

	public void customizeCreeper(Creeper creeper, int fuse, int radius) {
		EntityCreeper entCreeper = ((CraftCreeper) creeper).getHandle();
		Field fuseF = null;
		Field radiusF = null;
		try {
			fuseF = EntityCreeper.class.getDeclaredField("maxFuseTicks");
			radiusF = EntityCreeper.class.getDeclaredField("explosionRadius");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		fuseF.setAccessible(true);
		radiusF.setAccessible(true);
		try {
			fuseF.setInt(entCreeper, fuse);
			radiusF.setInt(entCreeper, radius);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onCreeper(CreeperPowerEvent e) {
		final Entity entity = e.getEntity();
		if (entity.getCustomName() != null
				&& entity.getCustomName().equals(ChatColor.GREEN + ChatColor.BOLD.toString() + "BOOM!")) {
			return;
		}
		entity.setCustomName(ChatColor.GREEN + ChatColor.BOLD.toString() + "BOOM!");
		if (!main.isCreeperEnabled()) {
			return;
		}
		String specialMob = ChatColor.RED + ChatColor.BOLD.toString() + "Mega Boom Creeper";
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (online.getWorld().equals(entity.getWorld())
					&& online.getLocation().distance(entity.getLocation()) < 50) {
				String first = main.getMessageLightning().replace("<creature>", specialMob);
				String message = ChatColor.translateAlternateColorCodes('&', first);
				online.playSound(online.getLocation(), Sound.SUCCESSFUL_HIT, 16, 1);
				online.sendMessage(main.getPrefix() + " " + message);
			}
		}

		new BukkitRunnable() {
			public void run() {
				final Creeper c = (Creeper) entity.getWorld().spawnEntity(entity.getLocation().add(1, 1, 1),
						EntityType.CREEPER);
				new BukkitRunnable() {
					public void run() {
						customizeCreeper(c, 0, 15);
					}
				}.runTaskLater(main, 25L);

				// Future Creeper Inflation Animation

				// ((EntityCreeper) c).a(16);
				// ((EntityCreeper) c).t_();
				// BlockPosition bp = new BlockPosition(c.getLocation().getX(),
				// c.getLocation().getY(),
				// c.getLocation().getZ());
				// Explosion explosion = new Explosion(((CraftWorld)
				// c.getWorld()).getHandle(),
				// ((CraftCreeper) c).getHandle(), (double) 1, (double) 1,
				// (double) 1, (float) 1, true, true);
				// IBlockData ibd =
				// net.minecraft.server.v1_8_R3.Block.getByCombinedId(46);
				// ((CraftCreeper) c).getHandle().a(explosion, ((CraftWorld)
				// c.getWorld()).getHandle(), bp, ibd);

			}
		}.runTaskLater(main, 25L);
	}

}
