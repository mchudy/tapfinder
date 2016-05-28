package tk.tapfinderapp.model.googleplaces;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Value;

@Value
public class PlaceDetailsResult {

    @SerializedName("html_attributions")
    List<String> htmlAttributions;

    PlaceDetails result;
}
