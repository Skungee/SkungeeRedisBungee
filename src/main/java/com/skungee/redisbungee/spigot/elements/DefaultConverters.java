package com.skungee.redisbungee.spigot.elements;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;

import com.skungee.redisbungee.shared.RedisBungeeServer;
import com.skungee.redisbungee.spigot.SkungeeRedisBungeeSpigot;
import com.skungee.redisbungee.spigot.TrackingManager;
import com.skungee.shared.objects.SkungeeServer;
import com.skungee.spigot.SkungeeAPI;

import ch.njol.skript.classes.Converter;
import ch.njol.skript.registrations.Converters;

public class DefaultConverters {

	static {
		Converters.registerConverter(RedisBungeeServer.class, String.class, new Converter<RedisBungeeServer, String>() {
			@Override
			@Nullable
			public String convert(RedisBungeeServer server) {
				return server.getName();
			}
		});
		Converters.registerConverter(RedisBungeeServer.class, SkungeeServer.class, new Converter<RedisBungeeServer, SkungeeServer>() {
			@Override
			@Nullable
			public SkungeeServer convert(RedisBungeeServer server) {
				return SkungeeAPI.getServers().stream()
						.filter(s -> s.getServerData().getAddress().equals(server.getAddress()))
						.filter(s -> s.getName().equals(server.getName()))
						.findFirst()
						.orElse(null);
			}
		});
		Converters.registerConverter(RedisBungeeServer.class, String.class, new Converter<RedisBungeeServer, String>() {
			@Override
			@Nullable
			public String convert(RedisBungeeServer server) {
				return server.getName();
			}
		});
		Converters.registerConverter(SkungeeServer.class, RedisBungeeServer.class, new Converter<SkungeeServer, RedisBungeeServer>() {
			@Override
			@Nullable
			public RedisBungeeServer convert(SkungeeServer server) {
				TrackingManager tracking = SkungeeRedisBungeeSpigot.getInstance().getTrackingManager();
				return tracking.getServers().stream()
						.filter(s -> s.getAddress().equals(server.getServerData().getAddress()))
						.filter(s -> s.getName().equals(server.getName()))
						.findFirst()
						.orElseGet(() -> {
							InetSocketAddress address = server.getServerData().getAddress();
							Optional<RedisBungeeServer> existing = tracking.getServerFrom(address);
							if (!existing.isPresent())
								return null;
							Set<UUID> uuids = tracking.getPlayersOnServer(existing.get()).stream().map(player -> player.getUniqueId()).collect(Collectors.toSet());
							return new RedisBungeeServer(server.getName(), server.getMotd(), address, uuids);
						});
			}
		});
	}

}
