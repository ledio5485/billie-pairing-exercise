package io.billie.orders.adapters.driven.persistence.repository

import io.billie.orders.adapters.driven.persistence.entity.OrderEvent
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrderEventStore : JpaRepository<OrderEvent, UUID> {
    fun findAllByAggregateId(aggregateId: UUID): List<OrderEvent>
}