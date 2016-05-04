package unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Value;

@Value
public class PlaceDetailsResult {

    @SerializedName("html_attributions")
    private List<String> htmlAttributions;

    private PlaceDetails result;
}
