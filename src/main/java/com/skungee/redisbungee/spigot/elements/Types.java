package com.skungee.redisbungee.spigot.elements;

import java.util.Optional;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.Nullable;

import com.skungee.redisbungee.shared.RedisBungeeServer;
import com.skungee.redisbungee.spigot.SkungeeRedisBungeeSpigot;
import com.skungee.spigot.SpigotSkungee;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;

public class Types {

	static {
		Classes.registerClass(new ClassInfo<RedisBungeeServer>(RedisBungeeServer.class, "redisbungeeserver")
				.defaultExpression(new EventValueExpression<RedisBungeeServer>(RedisBungeeServer.class))
				.description("A server that is from RedisBungee")
				.user("redisbungeeservers?")
				.name("RedisBungeeServer")
				.parser(new Parser<RedisBungeeServer>() {

					@Override
					@Nullable
					public RedisBungeeServer parse(String input, ParseContext context) {
						if (!Pattern.compile("^[A-Za-z0-9\\s]+$").matcher(input).matches())
							return null;
						Optional<RedisBungeeServer> server = SkungeeRedisBungeeSpigot.getInstance().getTrackingManager().getServer(input);
						if (server.isPresent())
							return server.get();
						return null;
					}

					@Override
					public boolean canParse(ParseContext context) {
						return SpigotSkungee.getInstance().getConfig().getBoolean("skungee-server-parsing", false);
					}

					@Override
					public String toString(RedisBungeeServer server, int flags) {
						return server.getName();
					}

					@Override
					public String toVariableNameString(RedisBungeeServer server) {
						return server.getName();
					}

					@Override
					public String getVariableNamePattern() {
						return "\\S+";
					}

				}));
	}

}
