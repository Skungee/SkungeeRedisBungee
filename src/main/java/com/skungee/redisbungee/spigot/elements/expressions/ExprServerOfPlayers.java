package com.skungee.redisbungee.spigot.elements.expressions;

import org.bukkit.OfflinePlayer;
import org.eclipse.jdt.annotation.Nullable;

import com.skungee.redisbungee.shared.RedisBungeeServer;
import com.skungee.redisbungee.spigot.SkungeeRedisBungeeSpigot;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;

@Name("RedisBungee - Server Of Player")
@Description("Returns the redis bungee server a player is on.")
public class ExprServerOfPlayers extends SimplePropertyExpression<OfflinePlayer, RedisBungeeServer> {

	static {
		register(ExprServerOfPlayers.class, RedisBungeeServer.class, "redis[ ]bungee server", "offlineplayers");
	}

	@Override
	public Class<? extends RedisBungeeServer> getReturnType() {
		return RedisBungeeServer.class;
	}

	@Override
	@Nullable
	public RedisBungeeServer convert(OfflinePlayer player) {
		return SkungeeRedisBungeeSpigot.getInstance().getTrackingManager().getServerOfPlayer(player.getUniqueId()).orElse(null);
	}

	@Override
	protected String getPropertyName() {
		return "redis bungee server";
	}

}
