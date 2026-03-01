@file:Suppress("unused")

package us.timinc.mc.timcore.api.extension

import net.minecraft.nbt.CompoundTag

/**
 * Gets the existing UUID value at the given key, or null if no property exists at that key. Assumes it's a UUID and
 * throws if not.
 *
 * @author Timothy Metcalfe
 */
fun CompoundTag.getUUIDOrNull(key: String) = if (this.contains(key)) this.getUUID(key) else null
/**
 * Gets the existing byte value at the given key, or null if no property exists at that key. Assumes it's a byte and
 * throws if not.
 *
 * @author Timothy Metcalfe
 */
fun CompoundTag.getByteOrNull(key: String) = if (this.contains(key)) this.getByte(key) else null
/**
 * Gets the existing short value at the given key, or null if no property exists at that key. Assumes it's a short and
 * throws if not.
 *
 * @author Timothy Metcalfe
 */
fun CompoundTag.getShortOrNull(key: String) = if (this.contains(key)) this.getShort(key) else null
/**
 * Gets the existing integer value at the given key, or null if no property exists at that key. Assumes it's an int and
 * throws if not.
 *
 * @author Timothy Metcalfe
 */
fun CompoundTag.getIntOrNull(key: String) = if (this.contains(key)) this.getInt(key) else null
/**
 * Gets the existing long value at the given key, or null if no property exists at that key. Assumes it's a long and
 * throws if not.
 *
 * @author Timothy Metcalfe
 */
fun CompoundTag.getLongOrNull(key: String) = if (this.contains(key)) this.getLong(key) else null
/**
 * Gets the existing float value at the given key, or null if no property exists at that key. Assumes it's a float and
 * throws if not.
 *
 * @author Timothy Metcalfe
 */
fun CompoundTag.getFloatOrNull(key: String) = if (this.contains(key)) this.getFloat(key) else null
/**
 * Gets the existing double value at the given key, or null if no property exists at that key. Assumes it's a double and
 * throws if not.
 *
 * @author Timothy Metcalfe
 */
fun CompoundTag.getDoubleOrNull(key: String) = if (this.contains(key)) this.getDouble(key) else null
/**
 * Gets the existing string value at the given key, or null if no property exists at that key. Assumes it's a string and
 * throws if not.
 *
 * @author Timothy Metcalfe
 */
fun CompoundTag.getStringOrNull(key: String) = if (this.contains(key)) this.getString(key) else null
/**
 * Gets the existing compound value at the given key, or null if no property exists at that key. Assumes it's a compound
 * and throws if not.
 *
 * @author Timothy Metcalfe
 */
fun CompoundTag.getCompoundOrNull(key: String) = if (this.contains(key)) this.getCompound(key) else null
/**
 * Gets the existing list value at the given key, or null if no property exists at that key. Assumes it's a list and
 * throws if not.
 *
 * @author Timothy Metcalfe
 */
fun CompoundTag.getListOrNull(key: String, i: Int) = if (this.contains(key)) this.getList(key, i) else null
/**
 * Gets the existing boolean value at the given key, or null if no property exists at that key. Assumes it's a bool and
 * throws if not.
 *
 * @author Timothy Metcalfe
 */
fun CompoundTag.getBooleanOrNull(key: String) = if (this.contains(key)) this.getBoolean(key) else null
/**
 * Gets the existing tag value at the given key, or null if no property exists at that key.
 *
 * @author Timothy Metcalfe
 */
fun CompoundTag.getOrNull(key: String) = if (this.contains(key)) this.get(key) else null

/**
 * Get or put a compound tag on this compound tag at the given key. Assumes that if there is a tag at this key, that it
 * is a compound tag.
 *
 * @author Timothy Metcalfe
 */
fun CompoundTag.getOrPutCompound(key: String, default: CompoundTag = CompoundTag()): CompoundTag {
    if (!this.contains(key)) this.put(key, default)
    return this.getCompound(key)
}