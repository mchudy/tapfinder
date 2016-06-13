package tk.tapfinderapp.model.googleplaces;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class OpenPeriod {
    OpenTime open;
    OpenTime close;
}
