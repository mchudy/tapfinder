package tk.tapfinderapp.model;

import lombok.Value;

@Value
public class BeerDetailsDto {
    int id;
    String name;
    String description;
    int styleId;
    String style;
    BreweryDto brewery;
    String imagePath;
}
