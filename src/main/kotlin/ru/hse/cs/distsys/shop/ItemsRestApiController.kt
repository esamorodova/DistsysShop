package ru.hse.cs.distsys.shop

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller("/api/items")
class ItemsController {
    @GetMapping("/sample")
    fun sample(): Item = Item(1, "test", "test")
}

data class Item(val itemId: Long, val itemName: String, val itemCategory: String)