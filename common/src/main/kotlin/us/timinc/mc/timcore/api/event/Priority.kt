package us.timinc.mc.timcore.api.event

/**
 * Need to tilt later or earlier in responding to a specific event? Subscribe with a lower or higher priority,
 * respectively. I'm not a huge fan of this pattern, and would prefer more contractual events; I'm leaving it here
 * for the meantime while I ensure my events will fit my intended pattern as I've become accustomed to the priority
 * pattern.
 */
enum class Priority {
    LOWEST, LOW, NORMAL, HIGH, HIGHEST
}