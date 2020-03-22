package ru.hse.cs.distsys.shop

import javax.persistence.*

@Entity
@Table(name = "items")
class ItemEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long? = null,
        var name: String,
        var category: String
)