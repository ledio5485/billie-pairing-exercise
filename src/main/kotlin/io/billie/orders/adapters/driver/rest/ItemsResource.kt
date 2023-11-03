package io.billie.orders.adapters.driver.rest

import io.billie.orders.core.ports.AddItems
import io.billie.orders.core.ports.RemoveItems
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/orders/{orderId}/items")
class ItemsResource(
    private val addItems: AddItems,
    private val removeItems: RemoveItems
) {

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun addItems(
        @PathVariable orderId: UUID,
        @Valid @RequestBody addItemsDto: AddItemsDto
    ) = addItems(addItemsDto.toCommand(orderId))

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun removeItems(
        @PathVariable orderId: UUID,
        @Valid @RequestBody removeItemsDto: RemoveItemsDto
    ) = removeItems(removeItemsDto.toCommand(orderId))
}