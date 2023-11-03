package io.billie.common.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Embeddable
open class CreationOnlyAudit(
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    var createdAt: Instant = Instant.now(),
    @Column(name = "created_by", nullable = false, updatable = false)
    @CreatedBy
    var createdBy: String = "system"
)

@Embeddable
class Audit(
    @Column(name = "updated_at")
    @LastModifiedDate
    var updatedAt: Instant = Instant.now(),
    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: String = "system"
) : CreationOnlyAudit()
