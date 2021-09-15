package com.skungee.redisbungee.bungeecord;

import java.net.InetSocketAddress;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import com.skungee.bungeecord.BungeeSkungee;
import com.skungee.japson.gson.JsonArray;
import com.skungee.japson.gson.JsonObject;
import com.skungee.japson.shared.Handler;
import com.skungee.redisbungee.shared.RedisBungeeServer;
import com.skungee.redisbungee.shared.RedisBungeeServerSerializer;
import com.skungee.shared.Packets;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

public class SkungeeRedisBungee extends Plugin {

	private final static RedisBungeeServerSerializer serializer = new RedisBungeeServerSerializer();
	private static SkungeeRedisBungee instance;
	private RedisBungeeAPI redisBungee;
	private BungeeSkungee skungee;

	@Override
	public void onEnable() {
		instance = this;
		if (getProxy().getPluginManager().getPlugin("RedisBungee") == null) {
			getProxy().getConsole().sendMessage(new TextComponent("RedisBungee is not currently enabled. Disabling the Skungee RedisBungee extension."));
			return;
		}
		Plugin plugin = getProxy().getPluginManager().getPlugin("Skungee");
		if (plugin == null) {
			getProxy().getConsole().sendMessage(new TextComponent("Skungee is not currently enabled. Disabling the Skungee RedisBungee extension."));
			return;
		}
		redisBungee = RedisBungeeAPI.getRedisBungeeApi();
		skungee = (BungeeSkungee) plugin;
		skungee.getJapsonServer().registerHandlers(new Handler(Packets.API.getPacketId()) {
			@Override
			public JsonObject handle(InetSocketAddress address, JsonObject object) {
				if (!object.has("redisbungee"))
					return null;
				SkungeeRedisBungee instance = SkungeeRedisBungee.getInstance();
				if (!object.get("redisbungee").getAsString().equals(instance.getDescription().getVersion()))
					throw new IllegalStateException("The version of the RedisBungee Skungee extension from the incoming packet on Spigot did not match that of the RedisBungee Skungee extension version running on the Proxy.");
				return getSerializedTable();
			}
		});
	}

	private RedisBungeeServer fromServerInfo(ServerInfo info) {
		return new RedisBungeeServer(info.getName(), info.getMotd(), (InetSocketAddress) info.getSocketAddress(), info.getPlayers().stream().map(player -> player.getUniqueId()).collect(Collectors.toSet()));
	}

	private Table<String, ServerInfo, UUID> getNetworkTable() {
		Table<String, ServerInfo, UUID> table = HashBasedTable.create();
		RedisBungeeAPI api = SkungeeRedisBungee.getInstance().getRedisBungeeApi();
		for (String proxy : api.getAllServers()) {
			for (UUID player : api.getPlayersOnProxy(proxy)) {
				ServerInfo server = api.getServerFor(player);
				if (table.containsValue(player))
					continue;
				table.put(proxy, server, player);
			}
		}
		return table;
	}

	private JsonObject getSerializedTable() {
		Table<String, ServerInfo, UUID> table = getNetworkTable();
		JsonObject proxies = new JsonObject();
		for (String proxy : table.rowKeySet()) {
			JsonArray serversAndPlayers = new JsonArray();
			for (Entry<ServerInfo, UUID> entry : table.row(proxy).entrySet()) {
				JsonObject serverAndPlayer = new JsonObject();
				serverAndPlayer.add("server", serializer.serialize(fromServerInfo(entry.getKey()), RedisBungeeServer.class, null));
				serverAndPlayer.addProperty("player", entry.getValue().toString());
				serversAndPlayers.add(serverAndPlayer);
			}
			proxies.add(proxy, serversAndPlayers);
		}
		return proxies;
	}

	public static SkungeeRedisBungee getInstance() {
		return instance;
	}

	public RedisBungeeAPI getRedisBungeeApi() {
		return redisBungee;
	}

	public BungeeSkungee getSkungee() {
		return skungee;
	}

}
