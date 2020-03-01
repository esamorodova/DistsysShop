package ru.hse.cs.distsys.shop

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/items")
class ItemsRestApiController(val repository: ItemsRepository) {
    @GetMapping("/get_item")
    fun getItem(@RequestParam id: Long): Item? = repository.getItem(id)

    @PostMapping("/add")
    fun addNewItem(@RequestParam name: String, @RequestParam category: String): Long {
        return repository.addItem(name, category)
    }

    @DeleteMapping("/delete")
    fun deleteItem(@RequestParam id: Long) {
        repository.deleteItem(id)
    }

    @PutMapping("/update")
    fun updateItem(@RequestParam id: Long, @RequestParam name: String, @RequestParam category: String) {
        val newItem = Item(id, name, category)
        repository.updateItem(newItem)
    }

    @GetMapping("/get_list")
    fun getItemsList(@RequestParam offset: Int, @RequestParam length: Int): List<Item> {
        return repository.getItemsList(offset, length)
    }
}

data class Item(val itemId: Long, val itemName: String, val itemCategory: String)