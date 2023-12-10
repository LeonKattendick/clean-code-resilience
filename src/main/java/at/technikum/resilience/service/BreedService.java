package at.technikum.resilience.service;

import at.technikum.resilience.model.Breeds;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BreedService {

    private HttpService httpService;

    public Breeds getBreeds() {
        return fetchBreeds();
    }

    private Breeds fetchBreeds() {
        return httpService.call("https://catfact.ninja/breeds", Breeds.class);
    }
}
