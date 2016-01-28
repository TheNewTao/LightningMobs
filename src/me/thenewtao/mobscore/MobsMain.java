package me.thenewtao.mobscore;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import me.thenewtao.mobslistener.DamageEvent;
import me.thenewtao.mobslistener.PowerEvent;
import me.thenewtao.mobslistener.ZapEvent;

public class MobsMain extends JavaPlugin {

	private String prefix = null;
	private String lightningMessage = null;
	private boolean creepers = true;
	private boolean skeleton = true;
	private boolean zombie = true;
	private boolean spider = true;
	private boolean pig = true;
	private boolean witch = true;

	@Override
	public void onEnable() {
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}
		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));
		lightningMessage = getConfig().getString("message-if-player-is-near");
		creepers = getConfig().getBoolean("enable-creepers");
		skeleton = getConfig().getBoolean("enable-skeleton");
		zombie = getConfig().getBoolean("enable-zombie");
		spider = getConfig().getBoolean("enable-spider");
		pig = getConfig().getBoolean("enable-pig");
		witch = getConfig().getBoolean("enable-witch");
		Bukkit.getPluginManager().registerEvents(new DamageEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new ZapEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new PowerEvent(this), this);
	}

	public String getPrefix() {
		return prefix;
	}

	public String getMessageLightning() {
		return lightningMessage;
	}

	public boolean isCreeperEnabled() {
		return creepers;
	}

	public boolean isSkeletonEnabled() {
		return skeleton;
	}

	public boolean isZombieEnabled() {
		return zombie;
	}

	public boolean isSpiderEnabled() {
		return spider;
	}

	public boolean isPigEnabled() {
		return pig;
	}

	public boolean isWitchEnabled() {
		return witch;
	}

}
