package tk.tapfinderapp.model.googleplaces;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.net.URL;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Parcel
@Data
@EqualsAndHashCode(callSuper=true)
public class PlaceDetails extends Place {

    @SerializedName("formatted_phone_number")
    String formattedPhoneNumber;

    @SerializedName("opening_hours")
    OpeningHours openingHours;

    Integer priceLevel;
    URL website;
}
