package io.billie.orders.core.ports

import io.billie.common.domain.Entity
import io.billie.orders.core.AddItemsCommand
import io.billie.orders.core.ChangeOrderStatusCommand
import io.billie.orders.core.CreateOrderCommand
import io.billie.orders.core.CreateShipmentCommand
import io.billie.orders.core.RemoveItemsCommand

fun interface CreateOrder {
    operator fun invoke(command: CreateOrderCommand): Entity
}

fun interface ChangeOrderStatus {
    operator fun invoke(command: ChangeOrderStatusCommand)
}

fun interface AddItems {
    operator fun invoke(command: AddItemsCommand)
}

fun interface RemoveItems {
    operator fun invoke(command: RemoveItemsCommand)
}

fun interface CreateShipment {
    operator fun invoke(command: CreateShipmentCommand) : Entity
}