package unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces;

import java.util.Collections;
import java.util.Set;

import lombok.Data;
import lombok.Value;

@Value
public class Place {

    @Data
    public static class Geometry {
        private Location location;

    }

    @Data
    public static class Location {
        private float lat;
        private float lng;
    }

    private String formattedAddress;
    private Geometry geometry;
    private String icon;
    private String id;
    private String name;
    private String placeId;
    private Float rating;
    private String reference;
    private Set<String> types = Collections.emptySet( );
    private String url;
    private String vicinity;
}
