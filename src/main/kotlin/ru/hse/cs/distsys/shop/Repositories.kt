package ru.hse.cs.distsys.shop

import org.springframework.data.jpa.repository.JpaRepository

interface ItemEntityRepository : JpaRepository<ItemEntity, Long>