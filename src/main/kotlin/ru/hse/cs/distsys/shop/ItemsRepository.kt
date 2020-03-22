package ru.hse.cs.distsys.shop

interface ItemsRepository {
    fun addItem(name: String, category: String): Long
    fun deleteItem(id: Long)
    fun getItem(id: Long): Item?
    fun updateItem(item: Item)
    fun getItemsList(page: Int, length: Int): List<Item>
}
