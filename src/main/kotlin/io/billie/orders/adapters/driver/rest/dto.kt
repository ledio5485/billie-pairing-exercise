package io.billie.orders.adapters.driver.rest

import com.fasterxml.jackson.annotation.JsonValue
import io.billie.common.util.fromEnum
import io.billie.orders.core.AddItemsCommand
import io.billie.orders.core.ChangeOrderStatusCommand
import io.billie.orders.core.CreateShipmentCommand
import io.billie.orders.core.Order
import io.billie.orders.core.RemoveItemsCommand
import jakarta.validation.constraints.Size
import java.util.UUID

data class ChangeOrderStatusDto(
    val status: Status
) {
    enum class Status {
        CREATED,
        CONFIRMED,
        CANCELED;

        @JsonValue
        override fun toString() = name.lowercase()
    }

    fun toCommand(orderId: UUID) = ChangeOrderStatusCommand(
        orderId = orderId,
        status = fromEnum<Order.Status>(status)
    )
}

data class AddItemsDto (
    @field:Size(min = 1)
    val items: List<UUID>
) {
    fun toCommand(orderId: UUID) = AddItemsCommand(orderId, items)
}

data class RemoveItemsDto (
    @field:Size(min = 1)
    val items: List<UUID>
) {
    fun toCommand(orderId: UUID) = RemoveItemsCommand(orderId, items)
}

data class NewShipmentDto(
    val from: UUID,
    val to: UUID,
    @field:Size(min = 1)
    val items: List<UUID>,
) {
    fun toCommand(orderId: UUID) = CreateShipmentCommand(
        orderId = orderId,
        from = from,
        to = to,
        items = items
    )
}