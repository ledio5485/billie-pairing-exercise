package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.common.domain.Entity
import io.billie.orders.adapters.driver.rest.AddItemsDto
import io.billie.orders.adapters.driver.rest.ChangeOrderStatusDto
import io.billie.orders.adapters.driver.rest.NewShipmentDto
import io.billie.orders.adapters.driver.rest.RemoveItemsDto
import io.billie.orders.core.Address
import io.billie.orders.core.Item
import io.billie.orders.core.Order
import io.billie.orders.core.Shipment
import io.billie.orders.core.ports.GetOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.UUID

class OrdersIT : AbstractBaseTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var getOrder: GetOrder

    @Test
    fun `should create an order, confirm it, add items and place a shipment`() {
        val orderEntity = createOrder()

        confirmOrder(orderEntity)

        val item1 = UUID.randomUUID()
        val item2 = UUID.randomUUID()

        addItems(orderEntity, AddItemsDto( listOf(item1, item2)))

        removeItems(orderEntity, RemoveItemsDto(listOf(item2)))

        val from = UUID.randomUUID()
        val to = UUID.randomUUID()

        val shipmentEntity = createShipment(orderEntity, NewShipmentDto(from, to, listOf(item1)) )

        val actual = getOrder(orderEntity.id)

        val expected = Order(
            id = orderEntity.id,
            status = Order.Status.CONFIRMED,
            items = listOf(Item(item1)),
            shipments = listOf(
                Shipment(
                    id = shipmentEntity.id,
                    from = Address(from),
                    to = Address(to),
                    items = listOf(Item(item1))
                )
            )
        )
        assertThat(actual).isEqualTo(expected)
    }

    private fun createOrder(): Entity {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/orders")
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        return mapper.readValue(result.response.contentAsString, Entity::class.java)
    }

    private fun confirmOrder(order: Entity) {
        changeOrderStatus(order, ChangeOrderStatusDto.Status.CONFIRMED)
    }

    private fun cancelOrder(order: Entity) {
        changeOrderStatus(order, ChangeOrderStatusDto.Status.CANCELED)
    }

    private fun changeOrderStatus(order: Entity, status: ChangeOrderStatusDto.Status) {
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/orders/${order.id}/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(ChangeOrderStatusDto(status)))
        ).andExpect(MockMvcResultMatchers.status().isAccepted)
    }

    private fun addItems(order: Entity, payload: AddItemsDto) {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/orders/${order.id}/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(payload))
        ).andExpect(MockMvcResultMatchers.status().isAccepted)
    }

    private fun removeItems(order: Entity, payload: RemoveItemsDto) {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/orders/${order.id}/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(payload))
        ).andExpect(MockMvcResultMatchers.status().isAccepted)
    }

    private fun createShipment(order: Entity, payload: NewShipmentDto): Entity {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/orders/${order.id}/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(payload))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        return mapper.readValue(result.response.contentAsString, Entity::class.java)
    }
}
