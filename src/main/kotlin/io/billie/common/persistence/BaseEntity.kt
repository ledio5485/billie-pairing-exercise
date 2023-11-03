package io.billie.common.persistence

import com.fasterxml.uuid.Generators
import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.io.Serializable
import java.util.UUID

@MappedSuperclass
abstract class BaseEntity(
    @Id
    @Column(name = "id")
    val id: UUID = Generators.timeBasedEpochGenerator().generate()
) : Serializable
