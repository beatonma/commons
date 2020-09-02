package org.beatonma.commons.test.extensions.assertions

/**
 * Run each assertion block on the value in the corresponding position of this list.
 * i.e. The first block will run on the first item, etc.
 * An assertion function must be provided for each list item.
 */
fun <T> Collection<T>.assertEach(vararg assertionBlocks: (T) -> Unit) {
    assertionBlocks.size.assertEquals(this.size, message = "An assertion function must be provided for each list item.")

    forEachIndexed { index, obj ->
        assertionBlocks[index].invoke(obj)
    }
}


infix fun <T> Collection<T>.shouldAllBe(expected: Collection<T>) {
    if (size != expected.size) println("Length not equal: $size should be ${expected.size}")

    forEachIndexed { index, t ->
        t shouldbe expected.elementAt(index)
    }
}
