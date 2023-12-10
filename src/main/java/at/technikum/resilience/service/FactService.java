package at.technikum.resilience.service;

import at.technikum.resilience.model.Fact;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class FactService {

    private HttpService httpService;

    private CircuitBreaker circuitBreaker;

    public Supplier<Fact> getFact() {
        return circuitBreaker.decorateSupplier(this::fetchFact);
    }

    private Fact fetchFact() {
        return httpService.call("https://catfact.ninja/fact", Fact.class);
    }
}
