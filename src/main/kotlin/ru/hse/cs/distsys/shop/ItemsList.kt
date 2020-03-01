package ru.hse.cs.distsys.shop

interface ItemsRepository {
    fun addItem(name: String, category: String): Long
    fun deleteItem(id: Long)
    fun getItem(id: Long): Item?
    fun updateItem(item: Item)
    fun getItemsList(offset: Int, length: Int): List<Item>
}

class ListItemsRepository : ItemsRepository {
    private val items = mutableMapOf<Long, Item>()
    private var itemsCnt = 0L

    @Synchronized
    override fun addItem(name: String, category: String): Long {
        val id = itemsCnt++
        items[id] = Item(id, name, category)
        return id
    }

    @Synchronized
    override fun deleteItem(id: Long) {
        items.remove(id)
    }

    @Synchronized
    override fun getItem(id: Long): Item? {
        return items[id]
    }

    @Synchronized
    override fun updateItem(item: Item) {
        items[item.itemId] = item
    }

    @Synchronized
    override fun getItemsList(offset: Int, length: Int): List<Item> {
        return items.values.drop(offset).take(length)
    }

}