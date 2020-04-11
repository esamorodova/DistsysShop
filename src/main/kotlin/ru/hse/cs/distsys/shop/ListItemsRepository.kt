package ru.hse.cs.distsys.shop

import org.slf4j.LoggerFactory

class ListItemsRepository : ItemsRepository {
    private val items = mutableMapOf<Long, Item>()
    private var itemsCnt = 0L
    private val pageSize = 30

    companion object {
        private val logger = LoggerFactory.getLogger(ListItemsRepository::class.java)
    }

    @Synchronized
    override fun addItem(name: String, category: String): Long {
        val id = itemsCnt++
        items[id] = Item(id, name, category)
        logger.info("item added")
        return id
    }

    @Synchronized
    override fun deleteItem(id: Long) {
        items.remove(id)
        logger.info("item deleted")
    }

    @Synchronized
    override fun getItem(id: Long): Item? {
        return items[id]
    }

    @Synchronized
    override fun updateItem(item: Item) {
        items[item.itemId] = item
        logger.info("item updated")
    }

    @Synchronized
    override fun getItemsList(page: Int, length: Int): List<Item> {
        return items.values.drop(page * pageSize).take(length)
    }

    @Synchronized
    override fun getItemsCount(): Long {
        return items.count().toLong()
    }
}