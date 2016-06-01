package tk.tapfinderapp.model;

import java.util.List;

import lombok.Value;

@Value
public class FindBeerSearchResultDto {
    String placeId;
    List<PlaceBeerDto> beers;
}
