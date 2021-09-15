# SkungeeRedisBungee
A RedisBungee expansion for Skungee.

Currently in beta testing. Find the jars at https://github.com/Skungee/SkungeeRedisBungee/releases

Installation: 
- Ensure Skungee 2.0.0-BETA-2+ is installed.
- Ensure RedisBungee is installed.
- Install SkungeeRedisBungee on both Spigot and Bungeecord plugin folders.
- Restart Spigot servers and Bungeecords.

Syntaxes:
```
{
  "expressions": [
    {
      "id": "ExprPlayers",
      "name": "RedisBungee - Players",
      "description": [
        "Returns a list of all players on the RedisBungee. If String is used, it will be the Proxy ID and get players on that proxy."
      ],
      "return type": "Offline Player",
      "patterns": [
        "[(all [[of] the]|the)] redis[ ]bungee players [(from|on|connected to) [proxy] %redisbungeeservers/string%]"
      ]
    },
    {
      "id": "ExprProxies",
      "name": "RedisBungee - Proxies",
      "description": [
        "Returns a list of all proxies registered to RedisBungee."
      ],
      "return type": "Text",
      "patterns": [
        "[(all [[of] the]|the)] [redis[ ]bungee] proxies"
      ]
    },
    {
      "id": "ExprProxyOfPlayers",
      "name": "RedisBungee - Proxy Of Player",
      "description": [
        "Returns the redis bungee proxy a player is on."
      ],
      "return type": "Text",
      "patterns": [
        "[the] redis[ ]bungee proxy of %offlineplayers%",
        "%offlineplayers%'[s] redis[ ]bungee proxy"
      ]
    },
    {
      "id": "ExprProxyServers",
      "name": "RedisBungee - Proxy Servers",
      "description": [
        "Returns a list of servers from proxies of a string. Proxy string is the proxy ID."
      ],
      "return type": "RedisBungeeServer",
      "patterns": [
        "[(all [[of] the]|the)] redis[ ]bungee server[s] from prox(y|ies) %strings%"
      ]
    },
    {
      "id": "ExprServerOfPlayers",
      "name": "RedisBungee - Server Of Player",
      "description": [
        "Returns the redis bungee server a player is on."
      ],
      "return type": "RedisBungeeServer",
      "patterns": [
        "[the] redis[ ]bungee server of %offlineplayers%",
        "%offlineplayers%'[s] redis[ ]bungee server"
      ]
    },
    {
      "id": "ExprServers",
      "name": "RedisBungee - Servers",
      "description": [
        "Returns a list of servers from string or if not defined, all of them."
      ],
      "return type": "RedisBungeeServer",
      "patterns": [
        "[(all [[of] the]|the)] redis[ ]bungee server[s] [%strings%]"
      ]
    }
  ],
  "types": [
    {
      "id": "RedisBungeeServer",
      "name": "RedisBungeeServer",
      "description": [
        "A server that is from RedisBungee"
      ],
      "patterns": [
        "redisbungeeserver[s]"
      ]
    }
  ]
}
```
