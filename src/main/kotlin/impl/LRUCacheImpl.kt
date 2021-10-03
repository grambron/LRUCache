package impl

import LRUCache
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.HashMap

class LRUCacheImpl<K, V>(private var capacity: Int) : LRUCache<K, V> {

    inner class Node(var prev: Node?, var next: Node?, val key: K, val value: V)

    private var firstNode: Node? = null
    private var lastNode: Node? = null
    private var nodeCounter = 0
    private val map: MutableMap<K, Node> = HashMap()

    init {
        require(capacity > 0) { "cache capacity mustn't be negative" }
    }

    override fun get(key: K): V? {
        checkNotNull(key)

        val node = map[key] ?: return null
        val value = node.value ?: throw IllegalStateException("null values are not supported")
        deleteNodeFromMap(node)
        addNode(key, value)

        return value
    }

    override fun set(key: K, value: V) {
        checkNotNull(key)
        checkNotNull(value)
        assert(capacity >= map.size)

        map[key]?.let { deleteNodeFromMap(it) }
        assert(!map.containsKey(key))

        if (nodeCounter == capacity) {
            deleteNodeFromMap(lastNode!!)
        }
        addNode(key, value)
    }

    private fun addNode(key: K, value: V) {
        val newNode = Node(null, firstNode, key, value)
        firstNode?.let { it.prev = newNode }
        if (lastNode == null) {
            lastNode = newNode
        }
        firstNode = newNode
        map[key] = newNode
        nodeCounter++
    }

    private fun deleteNodeFromMap(currentNode: Node) {
        assert(nodeCounter > 0)

        val prevNode = currentNode.prev
        val nextNode = currentNode.next

        if (prevNode == null && nextNode == null) {
            assert(map.isNotEmpty())
            lastNode = null
            firstNode = null
        } else if (prevNode == null) {
            checkNotNull(nextNode)
            nextNode.prev = null
            firstNode = nextNode
        } else if (nextNode == null) {
            prevNode.next = null
            lastNode = prevNode
        } else {
            prevNode.next = nextNode
            nextNode.prev = prevNode
        }
        map.remove(currentNode.key)
        nodeCounter--
    }
}

