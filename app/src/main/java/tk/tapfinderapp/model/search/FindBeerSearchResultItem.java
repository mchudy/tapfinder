package tk.tapfinderapp.model.search;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Value;
import tk.tapfinderapp.model.googleplaces.Place;
import tk.tapfinderapp.model.place.PlaceBeerDto;

@Value
@AllArgsConstructor
public class FindBeerSearchResultItem {
    Place place;
    List<PlaceBeerDto> beers;
}
