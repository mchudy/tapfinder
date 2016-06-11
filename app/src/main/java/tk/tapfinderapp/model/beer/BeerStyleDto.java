package tk.tapfinderapp.model.beer;

import org.parceler.Parcel;

import lombok.Data;

@Data
@Parcel
public class BeerStyleDto {
    int id;
    String name;

    @Override
    public String toString() {
        return name;
    }
}
