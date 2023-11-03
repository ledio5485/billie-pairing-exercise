package io.billie.orders.core

import com.fasterxml.uuid.Generators
import java.util.UUID

data class CreateOrderCommand(
    val orderId: UUID = Generators.timeBasedEpochGenerator().generate()
)

data class ChangeOrderStatusCommand(
    val orderId: UUID,
    val status: Order.Status
)

data class AddItemsCommand(
    val orderId: UUID,
    val items: List<UUID>
)

data class RemoveItemsCommand(
    val orderId: UUID,
    val items: List<UUID>
)

data class CreateShipmentCommand(
    val shipmentId: UUID = Generators.timeBasedEpochGenerator().generate(),
    val orderId: UUID,
    val from: UUID,
    val to: UUID,
    val items: List<UUID>
)