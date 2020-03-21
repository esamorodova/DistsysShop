package ru.hse.cs.distsys.shop

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "items")
class ItemEntity(
        @GeneratedValue @Id var id: Long? = null,
        var name: String,
        var category: String
)