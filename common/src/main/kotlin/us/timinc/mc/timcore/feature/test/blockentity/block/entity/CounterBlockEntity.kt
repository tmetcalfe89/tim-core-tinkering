package us.timinc.mc.timcore.feature.test.blockentity.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import us.timinc.mc.timcore.feature.test.blockentity.TestBlockEntity

class CounterBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(TestBlockEntity.ModBlocks.CLICKER_BLOCK.blockEntityType, pos, state) {
    private var counter = 0

    fun count() {
        counter++
    }
}