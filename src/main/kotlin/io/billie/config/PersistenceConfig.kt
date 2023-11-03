package io.billie.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.time.Clock
import java.time.OffsetDateTime
import java.util.Optional

@EnableJpaAuditing(dateTimeProviderRef = "offsetDatetimeProvider", auditorAwareRef = "processAuditorAware")
@Configuration
class PersistenceConfig {
    @Bean("offsetDatetimeProvider")
    fun dateTimeProvider(): DateTimeProvider {
        return DateTimeProvider { Optional.of(OffsetDateTime.now(Clock.systemUTC())) }
    }

    @Bean("processAuditorAware")
    fun processAuditorAware(): AuditorAware<String> {
        return AuditorAware<String> {
            Optional.of(
                //SecurityContextHolder.getContext()?.authentication?.principal ?: "system"
                "system"
            )
        }
    }
}
