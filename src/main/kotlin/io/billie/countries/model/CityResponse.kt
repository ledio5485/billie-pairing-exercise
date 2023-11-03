package io.billie.countries.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size
import java.util.UUID

data class CityResponse(
    val id: UUID,
    val name: String,
    @Size(min = 2, max = 2)
    @JsonProperty("country_code")
    val countryCode: String
)
