package tk.tapfinderapp.model;

import lombok.Data;

@Data
public class BeerDto {
    int id;
    String name;
    String description;
    int styleId;
    String style;
    BreweryDto brewery;

    @Override
    public String toString() {
        return brewery.getName() + " " + name;
    }
}
