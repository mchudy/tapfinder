package tk.tapfinderapp.model;

import java.util.Date;

import lombok.Data;

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
