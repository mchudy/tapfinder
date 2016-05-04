package unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Set;

import lombok.Data;
import lombok.Value;

@Data
public class Place {

    @Value
    public static class Geometry {
        private Location location;
    }

    @Value
    public static class Location {
        private float lat;
        private float lng;
    }

    @SerializedName("formatted_address")
    private String formattedAddress;

    private Geometry geometry;
    private String icon;
    private String id;
    private String name;

    @SerializedName("place_id")
    private String placeId;

    private Float rating;
    private String reference;
    private Set<String> types = Collections.emptySet( );
    private String url;
    private String vicinity;
}
