package us.timinc.mc.timcore.api.feature

import us.timinc.mc.timcore.api.logging.Logger

/**
 * The basis for a [AbstractFeature]'s config. Feel free to override this and use it in your feature to add more to it.
 */
open class FeatureConfig {
    /**
     * If this is false, the feature's initialization will be skipped.
     */
    val enabled: Boolean = true

    /**
     * What level this feature's logger will log for. Any logs emitted to the logger at or above this priority will show
     * in the user's logs.
     */
    val debugLevel: Logger.LogLevel = Logger.LogLevel.WARN
}