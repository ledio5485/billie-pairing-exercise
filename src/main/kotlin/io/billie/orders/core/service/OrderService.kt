package io.billie.orders.core.service

import io.billie.common.domain.Entity
import io.billie.common.domain.UseCase
import io.billie.orders.core.ChangeOrderStatusCommand
import io.billie.orders.core.CreateOrderCommand
import io.billie.orders.core.ports.ChangeOrderStatus
import io.billie.orders.core.ports.CreateOrder
import io.billie.orders.core.ports.GetOrder

import org.springframework.context.ApplicationEventPublisher

@UseCase
internal class CreateOrderService(
    private val applicationEventPublisher: ApplicationEventPublisher
) : CreateOrder {

    override fun invoke(command: CreateOrderCommand): Entity {
        applicationEventPublisher.publishEvent(command)
        return Entity(command.orderId)
    }
}

@UseCase
class ChangeOrderStatusService(
    private val getOrder: GetOrder,
    private val applicationEventPublisher: ApplicationEventPublisher
) : ChangeOrderStatus {

    override fun invoke(command: ChangeOrderStatusCommand) {
        val order = getOrder(command.orderId)
        order.changeStatus(command.status)

        applicationEventPublisher.publishEvent(command)
    }
}