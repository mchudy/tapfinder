package unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

import lombok.Data;

@Data
public class PlaceDetails extends Place {

    @SerializedName("formatted_phone_number")
    private String formattedPhoneNumber;

    @SerializedName("opening_hours")
    private OpeningHours openingHours;

    private Integer priceLevel;
    private URL website;
}
