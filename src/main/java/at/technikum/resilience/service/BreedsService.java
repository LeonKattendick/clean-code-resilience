package at.technikum.resilience.service;

import at.technikum.resilience.model.Breeds;
import io.github.resilience4j.retry.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BreedsService {

    private HttpService httpService;

    private Retry retry;

    public void showBreeds() {
        Retry.decorateRunnable(retry, this::printBreeds).run();
    }

    private void printBreeds() {
        Breeds breeds = httpService.call("https://catfact.ninja/breeds", Breeds.class);

        for (Breeds.Breed breed : breeds.getData()) {
            System.out.println(breed.getBreed());
        }
    }
}
