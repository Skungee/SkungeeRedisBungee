package com.skungee.redisbungee.spigot.elements.expressions;

import org.bukkit.OfflinePlayer;
import org.eclipse.jdt.annotation.Nullable;

import com.skungee.redisbungee.spigot.SkungeeRedisBungeeSpigot;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;

@Name("RedisBungee - Proxy Of Player")
@Description("Returns the redis bungee proxy a player is on.")
public class ExprProxyOfPlayers extends SimplePropertyExpression<OfflinePlayer, String> {

	static {
		register(ExprProxyOfPlayers.class, String.class, "redis[ ]bungee proxy", "offlineplayers");
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	@Nullable
	public String convert(OfflinePlayer player) {
		return SkungeeRedisBungeeSpigot.getInstance().getTrackingManager().getProxyOfPlayer(player.getUniqueId()).orElse(null);
	}

	@Override
	protected String getPropertyName() {
		return "redis bungee proxy";
	}

}
