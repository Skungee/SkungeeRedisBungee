package com.skungee.redisbungee.spigot;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.skungee.spigot.SpigotSkungee;

import ch.njol.skript.Skript;

public class SkungeeRedisBungeeSpigot extends JavaPlugin {

	private static SkungeeRedisBungeeSpigot instance;
	private TrackingManager manager;
	private SpigotSkungee skungee;

	@Override
	public void onEnable() {
		instance = this;
		manager = new TrackingManager(this);
		skungee = SpigotSkungee.getInstance();
		try {
			Skript.registerAddon(this).loadClasses("com.skungee.redisbungee.spigot", "elements");
		} catch (IOException cause) {
			Skript.exception(cause, "Failed to load syntaxes for the Skungee RedisBungee extension.");
		}
	}

	public static SkungeeRedisBungeeSpigot getInstance() {
		return instance;
	}

	public TrackingManager getTrackingManager() {
		return manager;
	}

	public SpigotSkungee getSkungee() {
		return skungee;
	}

}
