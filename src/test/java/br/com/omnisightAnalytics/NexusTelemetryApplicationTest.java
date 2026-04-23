package br.com.omnisightAnalytics;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class NexusTelemetryApplicationTest {

    @Test
    void shouldRunSpringApplication() {
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {

            mockedSpringApplication.when(() -> SpringApplication.run(NexusTelemetryApplication.class, new String[]{}))
                    .thenReturn(null);

            NexusTelemetryApplication.main(new String[]{});

            mockedSpringApplication.verify(() -> SpringApplication.run(NexusTelemetryApplication.class, new String[]{}));
        }
    }
}