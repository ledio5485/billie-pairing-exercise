package io.billie.orders.adapters.driven.event

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.orders.adapters.driven.persistence.entity.OrderEvent
import io.billie.orders.adapters.driven.persistence.repository.OrderEventStore
import io.billie.orders.core.AddItemsCommand
import io.billie.orders.core.Address
import io.billie.orders.core.ChangeOrderStatusCommand
import io.billie.orders.core.CreateOrderCommand
import io.billie.orders.core.CreateShipmentCommand
import io.billie.orders.core.Item
import io.billie.orders.core.Order
import io.billie.orders.core.RemoveItemsCommand
import io.billie.orders.core.Shipment
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
internal class OrderEventsHandler(
    private val objectMapper: ObjectMapper,
    private val orderEventStore: OrderEventStore,
) {

    @EventListener
    fun onCreateOrderCommand(command: CreateOrderCommand) {
        val event = OrderEvent(
            aggregateId = command.orderId,
            eventType = OrderEvent.EventType.ORDER_CREATED
        )
        storeEvent(event)
    }

    @EventListener
    fun onChangeOrderStatusCommand(command: ChangeOrderStatusCommand) {
        val eventType = when (command.status) {
            Order.Status.CREATED -> OrderEvent.EventType.ORDER_CREATED
            Order.Status.CONFIRMED -> OrderEvent.EventType.ORDER_CONFIRMED
            Order.Status.CANCELED -> OrderEvent.EventType.ORDER_CANCELED
        }
        val event = OrderEvent(
            aggregateId = command.orderId,
            eventType = eventType
        )
        storeEvent(event)
    }

    @EventListener
    fun onAddItemsCommand(command: AddItemsCommand) {
        val event = OrderEvent(
            aggregateId = command.orderId,
            eventType = OrderEvent.EventType.ITEM_ADDED
        )
        command.items.forEach { storeEvent(event, Item(it)) }
    }

    @EventListener
    fun onRemoveItemsCommand(command: RemoveItemsCommand) {
        val event = OrderEvent(
            aggregateId = command.orderId,
            eventType = OrderEvent.EventType.ITEM_REMOVED
        )
        command.items.forEach { storeEvent(event, Item(it)) }
    }

    @EventListener
    fun onCreateShipmentCommand(command: CreateShipmentCommand) {
        val payload = Shipment(
            id = command.shipmentId,
            from = Address(command.from),
            to = Address(command.to),
            items = command.items.map { Item(it) }
        )
        val event = OrderEvent(
            aggregateId = command.orderId,
            eventType = OrderEvent.EventType.SHIPMENT_PLACED
        )
        storeEvent(event, payload)
    }

    private fun storeEvent(event: OrderEvent) = orderEventStore.save(event)

    private fun storeEvent(event: OrderEvent, payload: Any) {
        val eventWithPayload = event.copy(payload = objectMapper.writeValueAsString(payload))
        orderEventStore.save(eventWithPayload)
    }
}