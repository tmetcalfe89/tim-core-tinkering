/*
 * Copyright (C) 2023 Cobblemon Contributors
 * Copyright (C) 2026 timinc (Timothy Metcalfe)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * This file contains modifications from the original Cobblemon implementation.
 */

package us.timinc.mc.timcore.feature.cobblemon.customdroplogic.dropentry

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.drop.DropEntry
import com.cobblemon.mod.common.api.drop.ItemDropMethod
import com.cobblemon.mod.common.api.text.green
import com.cobblemon.mod.common.api.text.red
import com.cobblemon.mod.common.util.lang
import com.cobblemon.mod.common.util.toBlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3
import us.timinc.mc.timcore.api.logging.LoggerScope
import us.timinc.mc.timcore.feature.cobblemon.customdroplogic.CustomDropLogic

open class TagItemDropEntry : DropEntry {
    override var percentage: Float = 100F
    override var quantity: Int = 1
    override val maxSelectableTimes: Int = 1
    open var quantityRange: IntRange? = null
    open var dropMethod: ItemDropMethod? = null
    open var itemTag: ResourceLocation = ResourceLocation.parse("minecraft:fishes")

    fun getCount() = quantityRange?.random() ?: quantity

    override fun drop(
        entity: LivingEntity?,
        world: ServerLevel,
        pos: Vec3,
        player: ServerPlayer?
    ) {
        CustomDropLogic.withOperationContext {
            val logger = LoggerScope.current()
            logger.sing("Dropping ")

            val tagKey = TagKey.create(BuiltInRegistries.ITEM.key(), itemTag)
            val holders = BuiltInRegistries.ITEM.getTagOrEmpty(tagKey)
            val holder = holders.toList().randomOrNull() ?: run {
                logger.alert("Unable to load drop item from tag: $itemTag")
                return
            }
            val stack = ItemStack(holder, getCount())

            val inLava = world.getBlockState(pos.toBlockPos()).block == Blocks.LAVA
            val dropMethod = (dropMethod ?: Cobblemon.config.defaultDropItemMethod).let {
                if (inLava) {
                    logger.sing("Item would spawn in lava, reverting to placing it in user's inventory.")
                    ItemDropMethod.TO_INVENTORY
                } else it
            }

            if (dropMethod == ItemDropMethod.ON_PLAYER && player != null) {
                world.addFreshEntity(ItemEntity(player.level(), player.x, player.y, player.z, stack))
                logger.sing("Drop method is on player, but there is no player, dropped in world.")
            } else if (dropMethod == ItemDropMethod.TO_INVENTORY && player != null && !stack.isEmpty) {
                val name = stack.hoverName
                val count = stack.count
                val succeeded = player.addItem(stack)
                if (Cobblemon.config.announceDropItems) {
                    player.sendSystemMessage(
                        if (succeeded) lang("drop.item.inventory", count, name.copy().green())
                        else lang("drop.item.full", name).red()
                    )
                }
                if (succeeded) {
                    logger.sing("Drop method is to inventory, dropped to player's inventory.")
                } else {
                    logger.sing("Drop method is to inventory, but player's inventory was full.")
                }
            } else if (dropMethod == ItemDropMethod.ON_ENTITY && entity != null) {
                world.addFreshEntity(ItemEntity(entity.level(), entity.x, entity.y, entity.z, stack))
                logger.sing("Drop method is on entity, dropped on entity.")
            } else {
                world.addFreshEntity(ItemEntity(world, pos.x, pos.y, pos.z, stack))
                logger.sing("Dropped to world.")
            }
        }
    }

    @Suppress("unused")
    fun encode(buffer: RegistryFriendlyByteBuf) {
        buffer.writeFloat(this.percentage)
        buffer.writeVarInt(this.quantity)
        buffer.writeResourceLocation(this.itemTag)
        buffer.writeNullable(this.quantityRange) { _, it -> buffer.writeVarInt(it.first); buffer.writeVarInt(it.last) }
    }

    @Suppress("unused")
    fun decode(buffer: RegistryFriendlyByteBuf): TagItemDropEntry {
        this.percentage = buffer.readFloat()
        this.quantity = buffer.readVarInt()
        this.itemTag = buffer.readResourceLocation()
        this.quantityRange = buffer.readNullable { buffer.readVarInt()..buffer.readVarInt() }
        return this
    }
}