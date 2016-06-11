package tk.tapfinderapp.model.place;

import java.util.Date;

import lombok.Data;
import tk.tapfinderapp.model.beer.BeerDto;

@Data
public class PlaceBeerDto {
    int id;
    String placeId;
    String description;
    Date addedDate;
    Double price;
    int rating;
    String userName;
    BeerDto beer;
}
