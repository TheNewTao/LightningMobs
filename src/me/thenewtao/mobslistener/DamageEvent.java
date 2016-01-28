package me.thenewtao.mobslistener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.thenewtao.mobscore.MobsMain;
import net.md_5.bungee.api.ChatColor;

public class DamageEvent implements Listener {

	private MobsMain main;
	private PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1);
	private PotionEffect healthBoost = new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 2);

	public DamageEvent(MobsMain main) {
		this.main = main;
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		Player p = (Player) e.getEntity();
		if (e.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) e.getDamager();
			if (proj.getShooter() instanceof Skeleton) {
				Skeleton skelly = (Skeleton) proj.getShooter();
				if (skelly.getCustomName().equals(ChatColor.RED + ChatColor.BOLD.toString() + "Undead Knight")) {
					e.setDamage(e.getDamage() * 6);
				} else if (skelly.getCustomName().equals(ChatColor.RED + ChatColor.BOLD.toString() + "Rider")) {
					e.setDamage(e.getDamage() * 3);
				}
			} else if (proj.getShooter() instanceof Witch) {
				Witch berta = (Witch) proj.getShooter();
				if (berta.getCustomName().equals(ChatColor.GREEN + ChatColor.BOLD.toString() + "Berta")) {
					e.setDamage(e.getDamage() * 3);
					p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 10));
				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.LIGHTNING) {
			final Entity entity = e.getEntity();
			String specialMob = null;
			if (entity instanceof Zombie) {
				if (!main.isZombieEnabled()) {
					return;
				}
				LivingEntity livingZombie = (LivingEntity) entity;
				livingZombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));

				ItemStack first = new ItemStack(Material.DIAMOND_BOOTS, 1);
				ItemMeta firstMeta = first.getItemMeta();
				firstMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
				firstMeta.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
				firstMeta.addEnchant(Enchantment.PROTECTION_FIRE, 100, true);
				first.setItemMeta(firstMeta);

				ItemStack second = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
				ItemMeta secondMeta = first.getItemMeta();
				secondMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
				secondMeta.addEnchant(Enchantment.PROTECTION_FIRE, 100, true);
				second.setItemMeta(secondMeta);

				ItemStack third = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
				ItemMeta thirdMeta = first.getItemMeta();
				thirdMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
				thirdMeta.addEnchant(Enchantment.THORNS, 3, true);
				thirdMeta.addEnchant(Enchantment.PROTECTION_FIRE, 100, true);
				third.setItemMeta(thirdMeta);

				ItemStack fourth = new ItemStack(Material.DIAMOND_HELMET, 1);
				ItemMeta fourthMeta = first.getItemMeta();
				fourthMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
				fourthMeta.addEnchant(Enchantment.PROTECTION_FIRE, 100, true);
				fourth.setItemMeta(fourthMeta);

				ItemStack fifth = new ItemStack(Material.DIAMOND_SWORD, 1);
				ItemMeta fifthMeta = first.getItemMeta();
				fifthMeta.addEnchant(Enchantment.DAMAGE_ALL, 8, true);
				fifthMeta.addEnchant(Enchantment.KNOCKBACK, 2, true);
				fifthMeta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
				fifth.setItemMeta(fifthMeta);

				livingZombie.getEquipment().setBoots(first);
				livingZombie.getEquipment().setLeggings(second);
				livingZombie.getEquipment().setChestplate(third);
				livingZombie.getEquipment().setHelmet(fourth);
				livingZombie.getEquipment().setItemInHand(fifth);

				specialMob = ChatColor.RED + ChatColor.BOLD.toString() + "Bob";
			} else if (entity instanceof Spider) {
				if (!main.isSpiderEnabled()) {
					return;
				}
				if (entity.getCustomName() != null && entity.getCustomName()
						.equals(ChatColor.GREEN + ChatColor.BOLD.toString() + "Mighty Steed")) {
					return;
				}
				e.setCancelled(true);
				specialMob = ChatColor.RED + ChatColor.BOLD.toString() + "Spider Jockeys";
				Location loc = entity.getLocation();
				Spider one = (Spider) entity.getWorld().spawnEntity(loc.add(1, 0, 1), EntityType.SPIDER);
				Spider two = (Spider) entity.getWorld().spawnEntity(loc.add(0, 0, 0), EntityType.SPIDER);
				Spider three = (Spider) entity.getWorld().spawnEntity(loc.add(-1, 0, -1), EntityType.SPIDER);
				Skeleton oneExtra = (Skeleton) entity.getWorld().spawnEntity(loc.add(1, 0, 1), EntityType.SKELETON);
				Skeleton twoExtra = (Skeleton) entity.getWorld().spawnEntity(loc.add(0, 0, -0), EntityType.SKELETON);
				Skeleton threeExtra = (Skeleton) entity.getWorld().spawnEntity(loc.add(-2, 0, -2), EntityType.SKELETON);
				oneExtra.setCustomName(ChatColor.RED + ChatColor.BOLD.toString() + "Rider");
				twoExtra.setCustomName(ChatColor.RED + ChatColor.BOLD.toString() + "Rider");
				threeExtra.setCustomName(ChatColor.RED + ChatColor.BOLD.toString() + "Rider");
				one.setCustomName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Mighty Steed");
				two.setCustomName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Mighty Steed");
				three.setCustomName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Mighty Steed");

				one.addPotionEffect(speed);
				two.addPotionEffect(speed);
				three.addPotionEffect(speed);
				one.addPotionEffect(healthBoost);
				two.addPotionEffect(healthBoost);
				three.addPotionEffect(healthBoost);
				oneExtra.addPotionEffect(healthBoost);
				twoExtra.addPotionEffect(healthBoost);
				threeExtra.addPotionEffect(healthBoost);

				one.setPassenger(oneExtra);
				two.setPassenger(twoExtra);
				three.setPassenger(threeExtra);

				entity.remove();
			} else if (entity instanceof Witch) {
				if (!main.isWitchEnabled()) {
					return;
				}
				Witch berta = (Witch) e.getEntity();
				if (berta.getName().equals(ChatColor.GREEN + ChatColor.BOLD.toString() + "Berta")) {
					return;
				}
				specialMob = ChatColor.RED + ChatColor.BOLD.toString() + "Berta";
				Witch w = (Witch) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.WITCH);
				w.addPotionEffect(speed);
				w.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 5));
				w.setCustomName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Berta");
				entity.remove();
				w.setFireTicks(0);
			} else if (entity instanceof Skeleton) {
				if (!main.isSkeletonEnabled()) {
					return;
				}
				e.setCancelled(true);
				Skeleton skelly = (Skeleton) e.getEntity();
				if (skelly.getName().equals(ChatColor.RED + ChatColor.BOLD.toString() + "Rider")
						|| skelly.getName().equals(ChatColor.RED + ChatColor.BOLD.toString() + "Undead Knight")) {
					return;
				}
				skelly.setCustomName(ChatColor.RED + ChatColor.BOLD.toString() + "Undead Knight");
				Horse horse = (Horse) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.HORSE);
				horse.setVariant(Variant.SKELETON_HORSE);
				horse.setTamed(true);
				horse.setPassenger(skelly);
				horse.addPotionEffect(speed);
				horse.addPotionEffect(healthBoost);
				skelly.addPotionEffect(healthBoost);
				specialMob = ChatColor.RED + ChatColor.BOLD.toString() + "Undead Knight";
				skelly.setFireTicks(0);
				horse.setFireTicks(0);
			} else {
				return;
			}
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

}
