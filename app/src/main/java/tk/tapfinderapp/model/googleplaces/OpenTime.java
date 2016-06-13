package tk.tapfinderapp.model.googleplaces;

import org.parceler.Parcel;

import lombok.Data;

@Data
@Parcel
public class OpenTime {
    int day;
    String time;
}
