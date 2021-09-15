package com.skungee.redisbungee.shared;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.skungee.japson.gson.JsonArray;
import com.skungee.japson.gson.JsonDeserializationContext;
import com.skungee.japson.gson.JsonElement;
import com.skungee.japson.gson.JsonObject;
import com.skungee.japson.gson.JsonParseException;
import com.skungee.japson.gson.JsonSerializationContext;
import com.skungee.japson.shared.Serializer;

public class RedisBungeeServerSerializer implements Serializer<RedisBungeeServer> {

	@Override
	public RedisBungeeServer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = element.getAsJsonObject();
		if (!object.has("motd"))
			throw new JsonParseException("A SkungeeServer json element did not contain the property 'motd'");
		if (!object.has("name"))
			throw new JsonParseException("A SkungeeServer json element did not contain the property 'name'");
		if (!object.has("address"))
			throw new JsonParseException("A SkungeeServer json element did not contain the property 'address'");
		if (!object.has("port"))
			throw new JsonParseException("A SkungeeServer json element did not contain the property 'port'");
		Set<UUID> players = new HashSet<>();
		object.get("players").getAsJsonArray().forEach(uuid -> players.add(UUID.fromString(uuid.getAsString())));
		return new RedisBungeeServer(object.get("name").getAsString(), object.get("motd").getAsString(), new InetSocketAddress(object.get("address").getAsString(), object.get("port").getAsInt()), players);
	}

	@Override
	public JsonElement serialize(RedisBungeeServer server, Type type, JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		object.addProperty("motd", server.getMotd());
		object.addProperty("name", server.getName());
		object.addProperty("address", server.getAddress().getHostString());
		object.addProperty("port", server.getAddress().getPort());
		JsonArray players = new JsonArray();
		server.getPlayers().forEach(uuid -> players.add(uuid.toString()));
		object.add("players", players);
		return object;
	}

}
