package io.billie.orders.adapters.driver.rest

import io.billie.orders.core.ports.CreateShipment
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/orders/{orderId}/shipments")
class ShipmentsResource(
    private val createShipment: CreateShipment
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createShipment(
        @PathVariable orderId: UUID,
        @Valid @RequestBody newShipmentDto: NewShipmentDto
    ) = createShipment(newShipmentDto.toCommand(orderId))
}