package unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

import lombok.Value;

@Value
public class OpeningHours {

    @SerializedName("open_now")
    private boolean openNow;

    private List<OpenPeriod> periods = Collections.emptyList( );
}
