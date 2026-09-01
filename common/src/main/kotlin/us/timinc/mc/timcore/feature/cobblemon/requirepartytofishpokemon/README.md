---
aliases:
  - How do I require a party before fishing Pokémon?
---
# Require Party to Fish Pokémon

Prevents a player with an empty party from fishing up a Pokémon with a Cobblemon Poké Rod. Item catches from
ordinary fishing and Pokémon spawned by other means are unaffected.

The feature preserves the original Tim Core default and starts disabled. Enable it in
`config/tim_core/require_party_to_fish_pokemon.json`, then restart the game or server:

```json
{
  "enabled": true,
  "debugLevel": "WARN"
}
```

When the feature blocks a catch, Cobblemon exits before spawning the Pokémon or consuming the rod's bait. The player
receives a message explaining that their party needs at least one Pokémon.
