package io.billie.countries.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size
import java.util.UUID

data class CountryResponse(
    val id: UUID,
    val name: String,
    @JsonProperty("country_code") @Size(min = 2, max = 2) val countryCode: String,
)
