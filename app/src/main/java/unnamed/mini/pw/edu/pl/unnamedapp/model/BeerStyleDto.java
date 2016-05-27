package unnamed.mini.pw.edu.pl.unnamedapp.model;

import lombok.Data;

@Data
public class BeerStyleDto {
    int id;
    String name;

    @Override
    public String toString() {
        return name;
    }
}
