package tk.tapfinderapp.model;

import java.util.List;

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
    List<PlaceWithBeerDto> places;
}
