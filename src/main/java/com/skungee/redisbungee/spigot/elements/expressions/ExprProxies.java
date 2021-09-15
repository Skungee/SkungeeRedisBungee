package com.skungee.redisbungee.spigot.elements.expressions;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import com.skungee.redisbungee.spigot.SkungeeRedisBungeeSpigot;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

@Name("RedisBungee - Proxies")
@Description("Returns a list of all proxies registered to RedisBungee.")
public class ExprProxies extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprProxies.class, String.class, ExpressionType.SIMPLE, "[(all [[of] the]|the)] [redis[ ]bungee] proxies");
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "proxies";
	}

	@Override
	@Nullable
	protected String[] get(Event event) {
		return SkungeeRedisBungeeSpigot.getInstance().getTrackingManager().getProxies().toArray(String[]::new);
	}

}
