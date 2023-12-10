package at.technikum.resilience.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Breeds {

    @JsonProperty("current_page")
    private int currentPage;

    private List<Breed> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Breed {

        private String breed;

        private String country;

        private String origin;

        private String coat;

        private String pattern;

    }
}
