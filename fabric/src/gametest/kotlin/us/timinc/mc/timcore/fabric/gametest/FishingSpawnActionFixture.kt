package us.timinc.mc.timcore.fabric.gametest

import com.cobblemon.mod.common.api.spawning.BestSpawner
import com.cobblemon.mod.common.api.spawning.detail.SpawnAction
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail
import com.cobblemon.mod.common.api.spawning.fishing.FishingSpawnCause
import com.cobblemon.mod.common.api.spawning.position.FishingSpawnablePosition
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition
import com.cobblemon.mod.common.api.spawning.selection.SpawnSelectionData
import net.minecraft.core.BlockPos
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack

object FishingSpawnActionFixture {
    fun create(
        helper: GameTestHelper,
        player: ServerPlayer,
        rod: ItemStack,
    ): SpawnAction<Entity> {
        val cause = FishingSpawnCause(BestSpawner.fishingSpawner, player, rod, 0)
        val position = FishingSpawnablePosition(cause, helper.level, BlockPos(1, 1, 1), mutableListOf())
        val detail = object : SpawnDetail() {
            override val type = "tim_core_gametest"

            override fun createSpawnAction(
                spawnablePosition: SpawnablePosition,
                bucket: String,
                selectionData: SpawnSelectionData,
            ): SpawnAction<*> = error("The fishing GameTest does not select this synthetic spawn detail.")
        }

        return object : SpawnAction<Entity>(position, "common", detail) {
            override fun run(): Entity? = null
        }
    }
}
