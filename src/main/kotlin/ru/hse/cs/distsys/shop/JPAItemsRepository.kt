package ru.hse.cs.distsys.shop

import org.springframework.context.annotation.Profile
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception

@Component
@Profile("shop")
class JPAItemsRepository(private val itemsRepository: ItemEntityRepository) : ItemsRepository {
    private fun ItemEntity.toItem() = Item(id!!, name, category)
    private fun Item.toEntity() = ItemEntity(itemId, itemName, itemCategory)

    @Transactional
    @Throws(Exception::class)
    override fun addItem(name: String, category: String): Long {
        val itemEntity = ItemEntity(null, name, category)
        val savedEntity = itemsRepository.save(itemEntity)
        return savedEntity.id!!
    }

    @Transactional
    @Throws(Exception::class)
    override fun deleteItem(id: Long) {
        itemsRepository.deleteById(id)
    }

    @Transactional
    @Throws(Exception::class)
    override fun getItem(id: Long): Item? {
        val itemEntity = itemsRepository.findByIdOrNull(id)
        return itemEntity?.toItem()
    }

    @Transactional
    @Throws(Exception::class)
    override fun updateItem(item: Item) {
        val itemEntity = item.toEntity()
        itemsRepository.save(itemEntity)
    }

    @Transactional
    @Throws(Exception::class)
    override fun getItemsList(page: Int, length: Int): List<Item> {
        return itemsRepository.findAll(PageRequest.of(page, length)).map { it.toItem() }.toList()
    }

    @Transactional
    @Throws(Exception::class)
    override fun getItemsCount(): Long {
        return itemsRepository.count();
    }
}