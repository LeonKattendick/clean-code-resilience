package at.technikum.resilience;

import at.technikum.resilience.model.Fact;
import at.technikum.resilience.service.FactService;
import at.technikum.resilience.service.HttpService;
import at.technikum.resilience.util.CustomCircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class FactServiceTest {

    private HttpService httpService;

    private FactService factService;

    @BeforeEach
    void setUp() {
        httpService = mock(HttpService.class);
        factService = new FactService(httpService, new CustomCircuitBreakerConfig().circuitBreaker());
    }

    @Test
    void whenSuccess_staysClosed() {
        when(httpService.call(anyString(), any())).thenReturn(fact());

        assertEquals(fact(), factService.getFact().get());
        assertEquals(fact(), factService.getFact().get());
        assertEquals(fact(), factService.getFact().get());
    }

    @Test
    void whenFails_expectCallNotPermittedException_afterSecondTry() {
        when(httpService.call(anyString(), any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> factService.getFact().get());
        assertThrows(RuntimeException.class, () -> factService.getFact().get());
        assertThrows(CallNotPermittedException.class, () -> factService.getFact().get());
    }

    @Test
    void whenFails_opensUp_afterDuration() throws InterruptedException {
        when(httpService.call(anyString(), any()))
                .thenThrow(new RuntimeException())
                .thenThrow(new RuntimeException())
                .thenReturn(fact());

        assertThrows(RuntimeException.class, () -> factService.getFact().get());
        assertThrows(RuntimeException.class, () -> factService.getFact().get());
        Thread.sleep(1500);
        assertEquals(fact(), factService.getFact().get());
    }

    private Fact fact() {
        return new Fact("Test Fact", 100);
    }
}
