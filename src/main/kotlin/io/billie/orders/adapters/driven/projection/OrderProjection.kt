package io.billie.orders.adapters.driven.projection

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.orders.adapters.driven.persistence.entity.OrderEvent
import io.billie.orders.adapters.driven.persistence.entity.OrderEvent.EventType
import io.billie.orders.adapters.driven.persistence.repository.OrderEventStore
import io.billie.orders.core.Item
import io.billie.orders.core.Order
import io.billie.orders.core.Shipment
import io.billie.orders.core.ports.GetOrder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
internal class OrderProjection(
    private val orderEventStore: OrderEventStore,
    private val objectMapper: ObjectMapper
) : GetOrder {

    override fun invoke(orderId: UUID): Order {
        val events = orderEventStore.findAllByAggregateId(orderId)
        check(events.isNotEmpty()) { "No order yet?" }

        return Order(
            id = orderId,
            status = events.statusProjection(),
            items = events.itemsProjection(),
            shipments = events.shipmentProjection()
        )
    }

    private fun List<OrderEvent>.statusProjection(): Order.Status {
        val statusEvent =
            findLast { it.eventType.isOrderStatus() } ?: throw IllegalStateException("No order status found.")

        return when (statusEvent.eventType) {
            EventType.ORDER_CREATED -> Order.Status.CREATED
            EventType.ORDER_CONFIRMED -> Order.Status.CONFIRMED
            EventType.ORDER_CANCELED -> Order.Status.CANCELED
            else -> error("No order status found")
        }
    }

    private fun List<OrderEvent>.itemsProjection(): List<Item> {
        val itemsAdded = projection<Item>(EventType.ITEM_ADDED)
        val itemsRemoved = projection<Item>(EventType.ITEM_REMOVED)

        return mutableListOf<Item>().apply {
            addAll(itemsAdded)
            removeAll(itemsRemoved)
        }
    }

    private fun List<OrderEvent>.shipmentProjection(): List<Shipment> = projection<Shipment>(EventType.SHIPMENT_PLACED)

    private inline fun <reified T> List<OrderEvent>.projection(vararg eventTypes: EventType): List<T> =
        filter { eventTypes.contains(it.eventType) }
            .map { objectMapper.readValue(it.payload, T::class.java) }
}
