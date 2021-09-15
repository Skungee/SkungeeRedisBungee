package com.skungee.redisbungee.spigot.elements.expressions;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import com.skungee.redisbungee.shared.RedisBungeeServer;
import com.skungee.redisbungee.spigot.SkungeeRedisBungeeSpigot;
import com.skungee.redisbungee.spigot.TrackingManager;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

@Name("RedisBungee - Players")
@Description("Returns a list of all players on the RedisBungee. If String is used, it will be the Proxy ID and get players on that proxy.")
public class ExprPlayers extends SimpleExpression<OfflinePlayer> {

	static {
		Skript.registerExpression(ExprPlayers.class, OfflinePlayer.class, ExpressionType.SIMPLE, "[(all [[of] the]|the)] redis[ ]bungee players [(from|on|connected to) [proxy] %-redisbungeeservers/string%]");
	}

	private Expression<Object> servers;

	@Override
	public Class<? extends OfflinePlayer> getReturnType() {
		return OfflinePlayer.class;
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		servers = (Expression<Object>) exprs[0];
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "redis bungee players";
	}

	@Override
	@Nullable
	protected OfflinePlayer[] get(Event event) {
		TrackingManager tracking = SkungeeRedisBungeeSpigot.getInstance().getTrackingManager();
		if (servers == null)
			return tracking.getPlayers().toArray(OfflinePlayer[]::new);
		return servers.stream(event).flatMap(object -> {
			if (object instanceof String) {
				return tracking.getPlayersOnProxy((String)object).stream();
			}
			return tracking.getPlayersOnServer((RedisBungeeServer)object).stream();
		}).toArray(OfflinePlayer[]::new);
	}

}
