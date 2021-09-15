package com.skungee.redisbungee.spigot.elements.expressions;

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

@Name("RedisBungee - Servers")
@Description("Returns a list of servers from string or if not defined, all of them.")
public class ExprServers extends SimpleExpression<RedisBungeeServer> {

	static {
		Skript.registerExpression(ExprServers.class, RedisBungeeServer.class, ExpressionType.SIMPLE, "[(all [[of] the]|the)] redis[ ]bungee server[s] [%-strings%]");
	}

	private Expression<String> servers;

	@Override
	public Class<? extends RedisBungeeServer> getReturnType() {
		return RedisBungeeServer.class;
	}

	@Override
	public boolean isSingle() {
		return servers.isSingle();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		servers = (Expression<String>) exprs[0];
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "redis bungee servers";
	}

	@Override
	@Nullable
	protected RedisBungeeServer[] get(Event event) {
		TrackingManager tracking = SkungeeRedisBungeeSpigot.getInstance().getTrackingManager();
		if (servers == null)
			return tracking.getServers().toArray(RedisBungeeServer[]::new);
		return servers.stream(event).map(string -> tracking.getServer(string)).toArray(RedisBungeeServer[]::new);
	}

}
