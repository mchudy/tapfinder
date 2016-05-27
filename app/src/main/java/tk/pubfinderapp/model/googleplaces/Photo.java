package tk.pubfinderapp.model.googleplaces;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Data;
import tk.pubfinderapp.Constants;

@Data
@Parcel
public class Photo {

    int height;
    int width;

    @SerializedName("photo_reference")
    String photoReference;

    public String getUrl(String key, int maxWidth) {
        return Constants.GOOGLE_MAPS_API_URL + "place/photo?photoreference=" + photoReference
                + "&key=" + key + "&maxwidth=" + maxWidth;
    }
}
