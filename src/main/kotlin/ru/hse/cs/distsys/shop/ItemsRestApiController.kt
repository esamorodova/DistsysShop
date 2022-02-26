package ru.hse.cs.distsys.shop

import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.*
import ru.hse.cs.distsys.AuthorizationError
import ru.hse.cs.distsys.NotFoundException
import ru.hse.cs.distsys.auth.AuthServiceImpl
import ru.hse.cs.distsys.auth.AuthenticationService
import ru.hse.cs.distsys.auth.AuthorizationService

@RestController
@RequestMapping("/api/items")
@Profile("shop")
class ItemsRestApiController(val repository: ItemsRepository, val authService: AuthenticationService) {
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
        println("$tokenParts $email")
        return authService.validate(email, tokenParts)
    }


    @GetMapping("/{id}")
    fun getItem(@PathVariable id: Long): Item {
        return repository.getItem(id) ?: throw NotFoundException("item not found")
    }

    @PostMapping
    fun addNewItem(@RequestBody item: Item,
                   @RequestHeader("authorization") token: String): Long {
        if (!checkToken(token)) {
            throw AuthorizationError("no access")
        }
        return repository.addItem(item.itemName, item.itemCategory)
    }

    @DeleteMapping("/{id}")
    fun deleteItem(@PathVariable id: Long, @RequestHeader("authorization") token: String) {
        if (!checkToken(token)) {
            throw AuthorizationError("no access")
        }
        repository.deleteItem(id)
    }

    @PutMapping("/{id}")
    fun updateItem(@PathVariable id: Long, @RequestBody item: Item,
                   @RequestHeader("authorization") token: String) {
        if (!checkToken(token)) {
            throw AuthorizationError("no access")
        }
        val newItem = Item(id, item.itemName, item.itemCategory)
        repository.updateItem(newItem)
    }

    @GetMapping
    fun getItemsList(@RequestParam page: Int, @RequestParam length: Int): ListResult {
        return ListResult(repository.getItemsCount(), repository.getItemsList(page, length))
    }
}

data class Item(val itemId: Long, val itemName: String, val itemCategory: String)
