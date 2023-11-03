package io.billie.orders.core.service

import io.billie.common.domain.UseCase
import io.billie.orders.core.AddItemsCommand
import io.billie.orders.core.RemoveItemsCommand
import io.billie.orders.core.ports.AddItems
import io.billie.orders.core.ports.GetOrder
import io.billie.orders.core.ports.RemoveItems
import org.springframework.context.ApplicationEventPublisher

@UseCase
internal class AddItemService(
    private val applicationEventPublisher: ApplicationEventPublisher
) : AddItems {
    override fun invoke(command: AddItemsCommand) = applicationEventPublisher.publishEvent(command)
}

@UseCase
internal class RemoveItemService(
    private val getOrder: GetOrder,
    private val applicationEventPublisher: ApplicationEventPublisher
) : RemoveItems {
    override fun invoke(command: RemoveItemsCommand) {
        val order = getOrder(command.orderId)
        require (order.items.map { it.id }.containsAll(command.items)) { "Please check your payload, not all the items are present." }
        applicationEventPublisher.publishEvent(command)
    }
}