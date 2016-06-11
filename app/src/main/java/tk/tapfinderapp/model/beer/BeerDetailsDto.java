package tk.tapfinderapp.model.beer;

import java.util.List;

import lombok.Value;
import tk.tapfinderapp.model.place.PlaceWithBeerDto;

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
