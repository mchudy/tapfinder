package tk.tapfinderapp.model.search;

import java.util.List;

import lombok.Value;
import tk.tapfinderapp.model.place.PlaceBeerDto;

@Value
public class FindBeerSearchResultDto {
    String placeId;
    List<PlaceBeerDto> beers;
}
