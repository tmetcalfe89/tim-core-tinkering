package us.timinc.mc.timcore.feature.cobblemon.evgainmultiplier.handler

import com.cobblemon.mod.common.api.events.pokemon.EvGainedEvent
import us.timinc.mc.timcore.feature.cobblemon.evgainmultiplier.EvGainMultiplier

object EvGainMultiplierHandler {
    fun handle(event: EvGainedEvent.Pre) {
        EvGainMultiplier.withOperationContext {
            val multiplier = config.multiplier
            if (!multiplier.isFinite() || multiplier < 0.0) {
                logger.warn("EV gain multiplier must be a finite, non-negative number; ignoring $multiplier.")
                return
            }

            val originalAmount = event.amount
            val multipliedAmount = multiply(originalAmount, multiplier)
            logger.sing(
                "Multiplying ${event.stat.identifier} EV gain from $originalAmount to $multipliedAmount " +
                    "with multiplier $multiplier.",
            )

            event.amount = multipliedAmount
            if (multipliedAmount == 0 && originalAmount != 0) {
                event.cancel()
            }
        }
    }

    internal fun multiply(amount: Int, multiplier: Double): Int {
        require(multiplier.isFinite() && multiplier >= 0.0) {
            "EV gain multiplier must be finite and non-negative."
        }

        return (amount.toDouble() * multiplier)
            .coerceIn(Int.MIN_VALUE.toDouble(), Int.MAX_VALUE.toDouble())
            .toInt()
    }
}
