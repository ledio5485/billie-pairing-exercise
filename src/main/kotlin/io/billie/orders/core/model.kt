package io.billie.orders.core

import java.util.UUID

data class Item(val id: UUID)

data class Order(
    val id: UUID,
    var status: Status,
    var items: List<Item> = mutableListOf(),
    var shipments: List<Shipment> = mutableListOf()
) {
    enum class Status {
        CREATED,
        CONFIRMED,
        CANCELED
    }

    fun changeStatus(status: Status) {
        require(this.status != status) { "Same status is not allowed."}

        when (this.status) {
            Status.CANCELED -> throw IllegalArgumentException("The status is cancelled.")
            Status.CONFIRMED -> require(status != Status.CREATED) { "The status is already confirmed." }
            Status.CREATED -> Unit
        }
        this.status = status
    }
}

data class Address(val id: UUID)

data class Shipment(
    val id: UUID,
    val from: Address,
    val to: Address,
    val items: List<Item>
)