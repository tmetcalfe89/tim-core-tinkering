---
aliases:
  - How do I change EV gain?
  - How do I disable EV gain?
---
# EV Gain Multiplier

Scales every EV gain before Cobblemon applies it. Configure the feature in
`config/tim_core/ev_gain_multiplier.json`:

```json
{
  "enabled": true,
  "debugLevel": "WARN",
  "multiplier": 1.0
}
```

The multiplier must be a finite, non-negative number. `1.0` leaves EV gain unchanged, `2.0` doubles it, and `0.0`
disables EV gain. Decimal multipliers are supported; because Cobblemon stores the event amount as a whole number,
positive fractional results are rounded down (`7 × 0.5` becomes `3`). Invalid multipliers are ignored and leave the
original EV gain unchanged.
