package io.billie.orders.core.service

import io.billie.common.domain.Entity
import io.billie.common.domain.UseCase
import io.billie.orders.core.CreateShipmentCommand
import io.billie.orders.core.ports.CreateShipment
import org.springframework.context.ApplicationEventPublisher

@UseCase
internal class ShipmentService(
    private val applicationEventPublisher: ApplicationEventPublisher
) : CreateShipment {
    override fun invoke(command: CreateShipmentCommand): Entity {
        applicationEventPublisher.publishEvent(command)
        return Entity(command.shipmentId)
    }
}