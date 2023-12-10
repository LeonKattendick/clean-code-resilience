package at.technikum.resilience;

import at.technikum.resilience.model.Breeds;
import at.technikum.resilience.service.BreedsService;
import at.technikum.resilience.service.HttpService;
import at.technikum.resilience.util.CustomRetryConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BreedsServiceTest {

    private HttpService httpService;

    private BreedsService breedsService;

    @BeforeEach
    void setUp() {
        httpService = mock(HttpService.class);
        breedsService = new BreedsService(httpService, new CustomRetryConfig().retry());
    }

    @Test
    void whenSuccess_runsOnce() {
        when(httpService.call(anyString(), any())).thenReturn(breeds());

        breedsService.showBreeds();

        verify(httpService, times(1)).call(anyString(), any());
    }

    @Test
    void whenFailsOnce_runSecondTime() {
        when(httpService.call(anyString(), any()))
                .thenThrow(new RuntimeException())
                .thenReturn(breeds());

        breedsService.showBreeds();

        verify(httpService, times(2)).call(anyString(), any());
    }

    @Test
    void whenFails_onlyRunsThreeTimes() {
        when(httpService.call(anyString(), any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> breedsService.showBreeds());

        verify(httpService, times(3)).call(anyString(), any());
    }

    private Breeds breeds() {
        return new Breeds(
                1,
                Collections.singletonList(new Breeds.Breed("Breed", "Country", "Origin", "Coat", "Pattern"))
        );
    }
}
