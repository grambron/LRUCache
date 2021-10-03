package impl

import LRUCache
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class LRUCacheTest {

    @Test
    fun setAndGet() {
        val lruCache: LRUCache<Int, Int> = LRUCacheImpl(8)
        val key = 1
        val value = 6
        lruCache[key] = value
        assertEquals(lruCache[key], value)
    }

    @Test
    fun setAndReplace() {
        val lruCache: LRUCache<Int, Int> = LRUCacheImpl(2)

        val firstKeyValue = Pair(1, 2)
        val secondKeyValue = Pair(3, 4)
        val thirdKeyValue = Pair(5, 6)

        lruCache[firstKeyValue.first] = firstKeyValue.second
        lruCache[secondKeyValue.first] = secondKeyValue.second
        lruCache[thirdKeyValue.first] = thirdKeyValue.second

        assertNull(lruCache[firstKeyValue.first])
        assertEquals(lruCache[secondKeyValue.first], secondKeyValue.second)
        assertEquals(lruCache[thirdKeyValue.first], thirdKeyValue.second)
    }

    @Test
    fun refreshKey() {
        val lruCache: LRUCache<Int, Int> = LRUCacheImpl(2)
        val firstKeyValue = Pair(1, 2)
        val secondKeyValue = Pair(3, 4)
        val thirdKeyValue = Pair(1, 6)
        val fourthKeyValue = Pair(5, 6)

        lruCache[firstKeyValue.first] = firstKeyValue.second
        lruCache[secondKeyValue.first] = secondKeyValue.second
        lruCache[thirdKeyValue.first] = thirdKeyValue.second
        lruCache[fourthKeyValue.first] = fourthKeyValue.second

        assertNull(lruCache[secondKeyValue.first])
        assertEquals(lruCache[firstKeyValue.first], thirdKeyValue.second)
        assertEquals(lruCache[fourthKeyValue.first], fourthKeyValue.second)
    }

    @Test
    fun getNotExistingKey() {
        val lruCache: LRUCache<Int, Int> = LRUCacheImpl(10)
        assertNull(lruCache[1])
    }

    @Test
    fun setNullKey() {
        val lruCache: LRUCache<Int?, Int?> = LRUCacheImpl(2)
        assertThrows<IllegalStateException> { lruCache[null] = 1 }
    }

    @Test
    fun setNullValue() {
        val lruCache: LRUCache<Int?, Int?> = LRUCacheImpl(2)
        assertThrows<IllegalStateException> { lruCache[1] = null }
    }

    @Test
    fun getNullKey() {
        val lruCache: LRUCache<Int?, Int?> = LRUCacheImpl(2)
        assertThrows<IllegalStateException> { lruCache[null] }
    }

    @Test
    fun illegalCapacity() {
        assertThrows<IllegalArgumentException> {
            val lruCache: LRUCache<Int, Int> = LRUCacheImpl(-1)
        }
        assertThrows<IllegalArgumentException> {
            val lruCache: LRUCache<Int, Int> = LRUCacheImpl(0)
        }
    }
}