package tk.tapfinderapp.model;

import java.util.Date;

import lombok.Data;

@Data
public class SpecialOfferDto {
    int id;
    String placeId;
    String description;
    Date startDate;
    Date endDate;
    String userName;
}
