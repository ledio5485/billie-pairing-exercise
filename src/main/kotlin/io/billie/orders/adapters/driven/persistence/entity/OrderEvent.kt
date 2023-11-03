package io.billie.orders.adapters.driven.persistence.entity

import io.billie.common.persistence.BaseEntity
import io.billie.common.persistence.CreationOnlyAudit
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Entity(name = "OrderEvent")
@Table(name = "order_event", schema = "organisations_schema")
@EntityListeners(AuditingEntityListener::class)
data class OrderEvent(

    @Column(name = "aggregate_id")
    val aggregateId: UUID,

    @Column(name = "event_type")
    @Enumerated(EnumType.STRING)
    val eventType: EventType,

    @Column(name = "payload")
    @JdbcTypeCode(SqlTypes.JSON)
    val payload: String? = null,

    @Embedded
    val audit: CreationOnlyAudit = CreationOnlyAudit()
) : BaseEntity() {

    enum class EventType {
        ORDER_CREATED,
        ORDER_CONFIRMED,
        ORDER_CANCELED,
        ITEM_ADDED,
        ITEM_REMOVED,
        SHIPMENT_PLACED;

        fun isOrderStatus() = arrayOf(ORDER_CREATED, ORDER_CONFIRMED, ORDER_CANCELED).contains(this)
    }
}
