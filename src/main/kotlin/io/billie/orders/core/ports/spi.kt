package io.billie.orders.core.ports

import io.billie.orders.core.Order
import java.util.UUID

fun interface GetOrder {
   operator fun invoke (orderId: UUID): Order
}