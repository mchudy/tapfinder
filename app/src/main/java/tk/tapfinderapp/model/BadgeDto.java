package tk.tapfinderapp.model;

import org.parceler.Parcel;

import lombok.Data;

@Parcel
@Data
public class BadgeDto {
    int id;
    String title;
    String description;
}
