package tk.tapfinderapp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class AddSpecialOfferDto {
    String placeId;
    String title;
    String description;
    Date startDate;
    Date endDate;
}
