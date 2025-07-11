package org.erp_microservices.peopleandorganizations.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integration")
@Tag("integration")
@DisplayName("Application Integration Test")
class ApplicationIntegrationTest {

    @Test
    @DisplayName("Application context should load")
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
        assertThat(true).isTrue();
    }
}