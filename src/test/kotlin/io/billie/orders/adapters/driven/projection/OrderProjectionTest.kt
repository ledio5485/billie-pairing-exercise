package io.billie.orders.adapters.driven.projection

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.billie.orders.adapters.driven.persistence.entity.OrderEvent
import io.billie.orders.adapters.driven.persistence.entity.OrderEvent.EventType
import io.billie.orders.adapters.driven.persistence.repository.OrderEventStore
import io.billie.orders.core.Address
import io.billie.orders.core.Item
import io.billie.orders.core.Order
import io.billie.orders.core.Shipment
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.util.UUID

class OrderProjectionTest {
    private val orderEventStore = mock<OrderEventStore>()
    private val objectMapper = ObjectMapper().registerKotlinModule()
    private var orderProjection = OrderProjection(orderEventStore, objectMapper)

    @Test
    fun `should do the order projection`() {
        val orderId = UUID.randomUUID()
        val item1 = UUID.randomUUID()
        val item2 = UUID.randomUUID()
        val shipmentId = UUID.randomUUID()
        val from = UUID.randomUUID()
        val to = UUID.randomUUID()

        Mockito.`when`(orderEventStore.findAllByAggregateId(orderId))
            .thenReturn(
                listOf(
                    OrderEvent(
                        aggregateId = orderId,
                        eventType = EventType.ORDER_CREATED
                    ),
                    OrderEvent(
                        aggregateId = orderId,
                        eventType = EventType.ORDER_CONFIRMED
                    ),
                    OrderEvent(
                        aggregateId = orderId,
                        eventType = EventType.ITEM_ADDED,
                        payload = objectMapper.writeValueAsString(Item(item1))
                    ),
                    OrderEvent(
                        aggregateId = orderId,
                        eventType = EventType.ITEM_ADDED,
                        payload = objectMapper.writeValueAsString(Item(item2))
                    ),
                    OrderEvent(
                        aggregateId = orderId,
                        eventType = EventType.ITEM_REMOVED,
                        payload = objectMapper.writeValueAsString(Item(item2))
                    ),
                    OrderEvent(
                        aggregateId = orderId,
                        eventType = EventType.SHIPMENT_PLACED,
                        payload = objectMapper.writeValueAsString(
                            Shipment(
                                id = shipmentId,
                                from = Address(from),
                                to = Address(to),
                                items = listOf(Item(item1))
                            )
                        )
                    )
                )
            )

        val actual = orderProjection(orderId)

        val expected = Order(
            id = orderId,
            status = Order.Status.CONFIRMED,
            items = listOf(Item(item1)),
            shipments = listOf(
                Shipment(
                    id = shipmentId,
                    from = Address(from),
                    to = Address(to),
                    items = listOf(Item(item1))
                )
            )
        )
        assertThat(actual).isEqualTo(expected)
    }
}
