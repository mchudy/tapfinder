package tk.tapfinderapp.ui.beer;

import tk.tapfinderapp.model.beer.BeerDetailsDto;
import tk.tapfinderapp.model.googleplaces.PlaceDetails;

public interface BeerDetailsView {
    void updateDetails(BeerDetailsDto details);
    void addPlace(PlaceDetails result, double price);
}
