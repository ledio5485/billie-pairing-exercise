package io.billie.functional

import io.billie.Application
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@SpringBootTest(
    classes = [Application::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
abstract class AbstractBaseTest {

    companion object {
        @JvmStatic
        @ServiceConnection
        private val postgresql =
            PostgreSQLContainer(DockerImageName.parse("postgres:16.0-alpine"))
                .withDatabaseName("organisations")
                .withUsername("postgres")
                .withPassword("secret")
                .apply { start() }
    }
}