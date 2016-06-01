package tk.tapfinderapp.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Value;
import tk.tapfinderapp.model.googleplaces.Place;

@Value
@AllArgsConstructor
public class FindBeerSearchResultItem {
    Place place;
    List<PlaceBeerDto> beers;
}
