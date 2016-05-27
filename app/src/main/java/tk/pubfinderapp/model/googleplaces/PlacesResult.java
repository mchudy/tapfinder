package tk.pubfinderapp.model.googleplaces;


import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import lombok.Data;

@Data
public class PlacesResult implements Iterable<Place> {

    @SerializedName("next_page_token")
    private String nextPageToken;

    private List<Place> results;

    public List<Place> asList( ) {
        return Collections.unmodifiableList( this.results );
    }

    @Override
    public Iterator<Place> iterator( ) {
        return this.results.iterator( );
    }

    public int size( ) {
        return this.results.size( );
    }
}