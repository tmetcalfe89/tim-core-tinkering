package us.timinc.mc.timcore.feature.test.blockentity.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import us.timinc.mc.timcore.feature.test.blockentity.block.entity.CounterBlockEntity

class CounterBlock(properties: Properties) : BaseEntityBlock(properties) {
    companion object {
        private val CODEC: MapCodec<CounterBlock> = simpleCodec(::CounterBlock)
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = CODEC

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        CounterBlockEntity(pos, state)

    override fun useWithoutItem(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        player: Player,
        blockHitResult: BlockHitResult
    ): InteractionResult {
        val blockEntity = level.getBlockEntity(blockPos) as? CounterBlockEntity ?: return InteractionResult.PASS
        if (level.isClientSide) {
            return InteractionResult.SUCCESS
        }

        blockEntity.count()
        return InteractionResult.SUCCESS
    }
}