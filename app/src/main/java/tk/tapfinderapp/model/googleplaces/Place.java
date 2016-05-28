package tk.tapfinderapp.model.googleplaces;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
@Parcel
public class Place {

    @Data
    @Parcel
    public static class Geometry {
        Location location;
    }

    @Data
    @Parcel
    public static class Location {
        float lat;
        float lng;
    }

    @SerializedName("formatted_address")
    String formattedAddress;

    Geometry geometry;
    String icon;
    String id;
    String name;

    @SerializedName("place_id")
    String placeId;

    Float rating;
    String reference;
    Set<String> types = Collections.emptySet( );
    String url;
    String vicinity;

    List<Photo> photos;
}
