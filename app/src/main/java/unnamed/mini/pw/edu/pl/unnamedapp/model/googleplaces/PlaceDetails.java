package unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

import lombok.Data;

@Data
public class PlaceDetails extends Place {

    @SerializedName("formatted_phone_number")
    String formattedPhoneNumber;

    @SerializedName("opening_hours")
    OpeningHours openingHours;

    Integer priceLevel;
    URL website;
}
