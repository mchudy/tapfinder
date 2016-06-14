package tk.tapfinderapp.ui.findbeer;

import android.location.Location;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.search.FindBeerSearchResultItem;
import tk.tapfinderapp.model.googleplaces.Place;
import tk.tapfinderapp.ui.base.FragmentChanger;
import tk.tapfinderapp.ui.place.PlaceFragment;

public class FindBeerResultsAdapter extends RecyclerView.Adapter<FindBeerResultsAdapter.FindBeerResultViewHolder>{

    private List<FindBeerSearchResultItem> items = Collections.emptyList();
    private FragmentChanger fragmentChanger;
    private Location userLocation;

    public FindBeerResultsAdapter(FragmentChanger fragmentChanger) {
        this.fragmentChanger = fragmentChanger;
    }

    @Override
    public FindBeerResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_beer_result_item,
                parent, false);
        return new FindBeerResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FindBeerResultViewHolder holder, int position) {
        FindBeerSearchResultItem item = items.get(position);
        holder.placeName.setText(item.getPlace().getName());
        holder.distance.setText(getDistanceToPlace(item.getPlace().getGeometry().getLocation()));
        FindBeerResultBeersAdapter beersAdapter = new FindBeerResultBeersAdapter(fragmentChanger);
        holder.beers.setLayoutManager(new LinearLayoutManager(holder.beers.getContext()));
        holder.beers.setAdapter(beersAdapter);
        beersAdapter.setBeers(item.getBeers());
        beersAdapter.notifyDataSetChanged();
        holder.placeName.setOnClickListener(v -> {
            fragmentChanger.changeFragment(PlaceFragment.newInstance(item.getPlace()));
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
     }

    public void setItems(List<FindBeerSearchResultItem> items) {
        this.items = items;
    }

    public void setUserLocation(Location location) {this.userLocation = location;}

    private String getDistanceToPlace(Place.Location location) {
        Location placeLocation = new Location("");
        placeLocation.setLatitude(location.getLat());
        placeLocation.setLongitude(location.getLng());
        float distance = userLocation.distanceTo(placeLocation) / 1000; // in km
        return String.format("%.2f km", distance);
    }

    public static class FindBeerResultViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.place_name)
        TextView placeName;

        @Bind(R.id.distance)
        TextView distance;

        @Bind(R.id.beers)
        RecyclerView beers;

        public FindBeerResultViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
