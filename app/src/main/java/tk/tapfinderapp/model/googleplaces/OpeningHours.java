package tk.tapfinderapp.model.googleplaces;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Collections;
import java.util.List;

import lombok.Data;

@Parcel
@Data
public class OpeningHours {

    @SerializedName("open_now")
    boolean openNow;

    List<OpenPeriod> periods = Collections.emptyList( );
}
