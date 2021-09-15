package com.skungee.redisbungee.spigot;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.skungee.japson.gson.JsonElement;
import com.skungee.japson.gson.JsonObject;
import com.skungee.redisbungee.shared.RedisBungeeServer;
import com.skungee.redisbungee.shared.RedisBungeeServerSerializer;
import com.skungee.spigot.SpigotSkungee;

public class TrackingManager {

	private final static RedisBungeeServerSerializer serializer = new RedisBungeeServerSerializer();
	private final Table<String, RedisBungeeServer, UUID> table = HashBasedTable.create();

	public TrackingManager(SkungeeRedisBungeeSpigot instance) {
		Bukkit.getScheduler().runTaskTimerAsynchronously(instance, () -> {
			SpigotSkungee skungee = SpigotSkungee.getInstance();
			JsonObject object = new JsonObject();
			object.addProperty("redisbungee", instance.getDescription().getVersion());
			try {
				this.table.clear();
				this.table.putAll(skungee.getAPI().sendPacket(object, new Function<JsonObject, Table<String, RedisBungeeServer, UUID>>() {
					@Override
					public Table<String, RedisBungeeServer, UUID> apply(JsonObject object) {
						Table<String, RedisBungeeServer, UUID> table = HashBasedTable.create();
						Map<String, JsonElement> proxies = object.entrySet().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
						for (String proxy : proxies.keySet()) {
							for (JsonElement element : proxies.get(proxy).getAsJsonArray()) {
								JsonObject serverAndPlayer = element.getAsJsonObject();
								RedisBungeeServer server = serializer.deserialize(serverAndPlayer.get("server"), RedisBungeeServer.class, null);
								UUID uuid = UUID.fromString(serverAndPlayer.get("player").getAsString());
								table.put(proxy, server, uuid);
							}
						}
						return table;
					}
				}));
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				skungee.debugMessage("Failed to send RedisBungee packet");
			}
		}, 0, 40);
	}

	public Optional<RedisBungeeServer> getServerFrom(InetSocketAddress address) {
		return table.cellSet().parallelStream().filter(cell -> cell.getColumnKey().getAddress().equals(address)).map(cell -> cell.getColumnKey()).findFirst();
	}

	public Optional<RedisBungeeServer> getServerOfPlayer(UUID uuid) {
		return table.cellSet().parallelStream().filter(cell -> cell.getValue().equals(uuid)).map(cell -> cell.getColumnKey()).findFirst();
	}

	public Optional<String> getProxyOfPlayer(UUID uuid) {
		return table.cellSet().parallelStream().filter(cell -> cell.getValue().equals(uuid)).map(cell -> cell.getRowKey()).findFirst();
	}

	public Set<OfflinePlayer> getPlayersOnServer(RedisBungeeServer server) {
		return table.column(server).values().stream()
				.map(uuid -> Bukkit.getOfflinePlayer(uuid))
				.collect(Collectors.toSet());
	}

	public Set<RedisBungeeServer> getServersOfProxy(String proxy) {
		return table.row(proxy).keySet();
	}

	public Optional<RedisBungeeServer> getServer(String name) {
		return table.columnKeySet().stream().filter(server -> server.getName().equals(name)).findFirst();
	}

	public Set<OfflinePlayer> getPlayersOnProxy(String proxy) {
		return table.row(proxy).values().stream()
				.map(uuid -> Bukkit.getOfflinePlayer(uuid))
				.collect(Collectors.toSet());
	}

	public Set<OfflinePlayer> getPlayers() {
		return table.values().stream()
				.map(uuid -> Bukkit.getOfflinePlayer(uuid))
				.collect(Collectors.toSet());
	}

	public Set<RedisBungeeServer> getServers() {
		return table.columnKeySet();
	}

	public Set<String> getProxies() {
		return table.rowKeySet();
	}

}
