package us.timinc.mc.timcore.api.context

import us.timinc.mc.timcore.api.logging.Logger

interface OperationContext<C> {
    val logger: Logger
    val config: C
}