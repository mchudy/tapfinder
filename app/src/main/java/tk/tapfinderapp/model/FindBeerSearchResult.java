package tk.tapfinderapp.model;

import java.util.List;

import lombok.Value;
import tk.tapfinderapp.model.googleplaces.Place;

@Value
public class FindBeerSearchResult {
    Place place;
    List<PlaceBeerDto> beers;
}
