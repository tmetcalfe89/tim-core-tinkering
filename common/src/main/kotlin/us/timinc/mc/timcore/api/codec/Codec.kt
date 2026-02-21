package us.timinc.mc.timcore.api.codec

import com.mojang.serialization.Codec

@Suppress("Unused")
object Codec {
    val INT_RANGE_CODEC: Codec<IntRange> = Codec.STRING.xmap(
        { str ->
            try {
                val (start, end) = str.split("..")
                val actualStart = when (start.lowercase()) {
                    "min" -> Int.MIN_VALUE
                    else -> start.toInt()
                }
                val actualEnd = when (end.lowercase()) {
                    "max" -> Int.MAX_VALUE
                    else -> end.toInt()
                }
                actualStart..actualEnd
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Invalid integer range $str", e)
            }
        },
        { it.toString() }
    )

    val FLOAT_RANGE_CODEC: Codec<ClosedFloatingPointRange<Float>> = Codec.STRING.xmap(
        { str ->
            try {
                val (start, end) = str.split("..")
                val actualStart = when (start.lowercase()) {
                    "min" -> Float.MIN_VALUE
                    else -> start.toFloat()
                }
                val actualEnd = when (end.lowercase()) {
                    "max" -> Float.MAX_VALUE
                    else -> end.toFloat()
                }
                actualStart..actualEnd
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Invalid float range $str", e)
            }
        },
        { it.toString() }
    )
}