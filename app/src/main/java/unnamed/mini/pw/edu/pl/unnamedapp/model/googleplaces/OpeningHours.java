package unnamed.mini.pw.edu.pl.unnamedapp.model.googleplaces;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class OpeningHours {

    @SerializedName("open_now")
    boolean openNow;

    List<OpenPeriod> periods = Collections.emptyList( );
}
