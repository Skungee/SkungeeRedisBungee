package com.skungee.redisbungee.shared;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RedisBungeeServer {

	private final Set<UUID> players = new HashSet<>();
	private final InetSocketAddress address;
	private final String name, motd;

	public RedisBungeeServer(String name, String motd, InetSocketAddress address, Collection<UUID> players) {
		this.players.addAll(players);
		this.address = address;
		this.motd = motd;
		this.name = name;
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	public Set<UUID> getPlayers() {
		return players;
	}

	public String getMotd() {
		return motd;
	}

	public String getName() {
		return name;
	}

}
