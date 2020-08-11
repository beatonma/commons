package org.beatonma.commons.kotlin.extensions

/**
 * Return the result of applying binary or to the given flags
 */
fun combineFlags(vararg flags: Int): Int {
    var outFlag = 0
    flags.forEach { outFlag = outFlag or it }
    return outFlag
}

/**
 * Return the result of applying binary or to the receiver and the given flag
 */
fun Int.addFlag(flag: Int): Int {
    return this or flag
}

/**
 * Returns true if applying bitwise AND to the receiver and the flag yields the flag value.
 * The receiver Int is created by applying bitwise OR to zero or more binary flags.
 *
 * If no flags are set, the receiver Int should be equal to zero.
 */
fun Int.hasFlag(flag: Int): Boolean {
    return this and flag == flag
}

/**
 * Return true if the receiver contains all of the given flags
 */
fun Int.hasAllFlags(vararg flags: Int): Boolean {
    flags.forEach {flag ->
        if (this and flag != flag) return false // Return false if any flag fails
    }
    return true
}

/**
 * Return true if the receiver contains at least one of the given flags
 */
fun Int.hasAnyFlag(vararg flags: Int): Boolean {
    flags.forEach { flag ->
        if (this and flag == flag) return true
    }
    return false
}

/**
 * Return true if the receiver can be constructed entirely from the given flags
 * i.e. The receiver contains all of, and only, the given flags
 */
fun Int.hasOnlyFlags(vararg flags: Int): Boolean {
    return this == combineFlags(*flags)
}

/**
 * 'Switch off' the given flag in the receiver
 */
fun Int.removeFlag(flag: Int): Int {
    return this and flag.inv()
}

/**
 * 'Switch off' all of the given flags in the receiver
 */
fun Int.removeFlags(vararg flags: Int): Int {
    return removeFlag(combineFlags(*flags))
}

/**
 * Remove the first flag and add the second
 */
fun Int.replaceFlag(removed: Int, added: Int): Int {
    return (this and removed.inv()) or added
}

/**
 * Add the flag if condition is true, remove otherwise.
 */
fun Int.setFlag(flag: Int, condition: Boolean): Int =
    when (condition) {
        true -> this.addFlag(flag)
        false -> this.removeFlag(flag)
    }

/**
 * Get the closest multiple of [nearest] AFTER the receiver.
 */
fun Int.roundUp(nearest: Int): Int {
    val mod = this % nearest
    return if (mod == 0) this
    else {
        if (this < 0) this - mod
        else this + nearest - mod
    }
}

/**
 * Get the closest multiple of [nearest] BEFORE the receiver.
 */
fun Int.roundDown(nearest: Int): Int {
    val mod = this % nearest
    return if (mod == 0) this
    else {
        if (this < 0) this - nearest - mod
        else this - mod
    }
}


fun Int.asBinaryString(): String = Integer.toBinaryString(this)
