package tk.tapfinderapp.view.favourites;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.googleplaces.Place;

public class FavouritePlacesAdapter extends RecyclerView.Adapter<FavouritePlacesAdapter.PlaceViewHolder> {

    private List<Place> items = new ArrayList<>();

    @Inject
    public FavouritePlacesAdapter() {
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item,
                parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Place item = items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Place item) {
        items.add(item);
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {

        public PlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}