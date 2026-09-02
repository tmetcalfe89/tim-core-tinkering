---
aliases:
  - How do I enable Exp All?
  - How do I add an Exp All item?
---
# Exp All

Awards experience to eligible Pokémon that did not participate in a victorious battle. Configure the feature in
`config/tim_core/exp_all.json`:

```json
{
  "enabled": true,
  "debugLevel": "WARN",
  "multiplier": 1.0,
  "force": false
}
```

By default, a player has Exp All access when their main inventory contains an item in `#tim_core:exp_all`. The tag is
empty so a mod or data pack can supply the desired item. Set `force` to `true` to grant Exp All access without an item.

`multiplier` is passed to Cobblemon's battle experience calculator and must be a finite, non-negative number. Pokémon
that participated against an opponent are left to Cobblemon's normal experience award, and Pokémon holding Exp Share
are left to Cobblemon's Exp Share award, preventing duplicate experience.

Integrations can subscribe to `ExpAll.Events.CHECK_ELIGIBILITY` and change the event's `hasExpAll` value for a specific
player and Pokémon.
