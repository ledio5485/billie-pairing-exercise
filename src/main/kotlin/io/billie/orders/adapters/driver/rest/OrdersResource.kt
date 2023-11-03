package io.billie.orders.adapters.driver.rest

import io.billie.orders.core.CreateOrderCommand
import io.billie.orders.core.ports.ChangeOrderStatus
import io.billie.orders.core.ports.CreateOrder
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/orders")
class OrdersResource(
    private val createOrder: CreateOrder,
    private val changeOrderStatus: ChangeOrderStatus
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder() = createOrder(CreateOrderCommand())

    @PatchMapping("/{orderId}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun changeOrderStatus(
        @PathVariable orderId: UUID,
        @Valid @RequestBody changeOrderStatus: ChangeOrderStatusDto
    ) = changeOrderStatus(changeOrderStatus.toCommand(orderId))
}