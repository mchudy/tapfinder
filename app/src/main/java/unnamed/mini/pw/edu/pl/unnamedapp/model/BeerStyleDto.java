package unnamed.mini.pw.edu.pl.unnamedapp.model;

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
