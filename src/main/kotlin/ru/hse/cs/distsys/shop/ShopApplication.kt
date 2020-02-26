package ru.hse.cs.distsys.shop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class ShopApplication {
	@Bean
	fun itemsRepository(): ItemsRepository = ListItemsRepository()
}

fun main(args: Array<String>) {
	runApplication<ShopApplication>(*args)
}
