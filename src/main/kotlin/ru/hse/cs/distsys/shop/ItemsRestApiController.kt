package ru.hse.cs.distsys.shop

import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.*
import ru.hse.cs.distsys.auth.AuthServiceImpl
import ru.hse.cs.distsys.auth.AuthenticationService

@RestController
@RequestMapping("/api/items")
@Profile("shop")
class ItemsRestApiController(val repository: ItemsRepository, val authService: AuthServiceImpl) {
    data class ListResult(val count: Long, val items: List<Item>)

    private fun checkToken(tokenString: String): Boolean {
        if (!tokenString.startsWith("Bearer ")) {
            return false
        }
        val tokenParts = tokenString.removePrefix("Bearer ")
        val splitToken = tokenParts.split(";")
        if (splitToken.size != 2) {
            return false
        }
        val (token, email) = splitToken
        return authService.validate(email, token)
    }


    @GetMapping("/{id}")
    fun getItem(@PathVariable id: Long): Item? = repository.getItem(id)

    @PostMapping
    fun addNewItem(@RequestParam name: String, @RequestParam category: String,
                   @RequestHeader("authorization") token: String): Long {
        if (!checkToken(token)) {
            throw Exception("no access")
        }
        return repository.addItem(name, category)
    }

    @DeleteMapping("/{id}")
    fun deleteItem(@PathVariable id: Long, @RequestHeader("authorization") token: String) {
        repository.deleteItem(id)
    }

    @PutMapping("/{id}")
    fun updateItem(@PathVariable id: Long, @RequestParam name: String, @RequestParam category: String,
                   @RequestHeader("authorization") token: String) {
        val newItem = Item(id, name, category)
        repository.updateItem(newItem)
    }

    @GetMapping
    fun getItemsList(@RequestParam page: Int, @RequestParam length: Int): ListResult {
        return ListResult(repository.getItemsCount(), repository.getItemsList(page, length))
    }
}

data class Item(val itemId: Long, val itemName: String, val itemCategory: String)
