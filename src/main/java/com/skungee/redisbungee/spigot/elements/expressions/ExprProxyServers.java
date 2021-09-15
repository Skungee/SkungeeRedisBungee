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

@Name("RedisBungee - Proxy Servers")
@Description("Returns a list of servers from proxies of a string. Proxy string is the proxy ID.")
public class ExprProxyServers extends SimpleExpression<RedisBungeeServer> {

	static {
		Skript.registerExpression(ExprProxyServers.class, RedisBungeeServer.class, ExpressionType.SIMPLE, "[(all [[of] the]|the)] redis[ ]bungee server[s] from prox(y|ies) %strings%");
	}

	private Expression<String> proxies;

	@Override
	public Class<? extends RedisBungeeServer> getReturnType() {
		return RedisBungeeServer.class;
	}

	@Override
	public boolean isSingle() {
		return proxies.isSingle();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		proxies = (Expression<String>) exprs[0];
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (debug)
			return "redis bungee servers from proxies";
		return "redis bungee servers from proxies: " + proxies.toString(event, debug);
	}

	@Override
	@Nullable
	protected RedisBungeeServer[] get(Event event) {
		TrackingManager tracking = SkungeeRedisBungeeSpigot.getInstance().getTrackingManager();
		return proxies.stream(event).flatMap(proxy -> tracking.getServersOfProxy(proxy).stream()).toArray(RedisBungeeServer[]::new);
	}

}
