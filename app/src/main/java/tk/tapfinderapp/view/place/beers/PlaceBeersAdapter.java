package tk.tapfinderapp.view.place.beers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.PlaceBeerDto;
import tk.tapfinderapp.service.TapFinderApiService;

public class PlaceBeersAdapter extends RecyclerView.Adapter<PlaceBeersAdapter.BeerOnTapViewHolder> {

    private List<PlaceBeerDto> placesBeers;
    private Context context;

    @Inject
    TapFinderApiService service;

    public PlaceBeersAdapter(Context context) {
        this.context = context;
        placesBeers = Collections.emptyList();
    }

    @Override
    public BeerOnTapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_beer_item,
                parent, false);
        return new BeerOnTapViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BeerOnTapViewHolder holder, int position) {
        PlaceBeerDto placeBeer = placesBeers.get(position);
        holder.beerName.setText(placeBeer.getBeer().getName());
        holder.brewery.setText(placeBeer.getBeer().getBrewery().getName());
        holder.description.setText(placeBeer.getDescription());
        holder.rating.setText(String.valueOf(placeBeer.getRating()));
    }

    @Override
    public int getItemCount() {
        return placesBeers.size();
    }

    public void setBeers(List<PlaceBeerDto> beers) {
        this.placesBeers = beers;
    }

    public static class BeerOnTapViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.beer_name)
        TextView beerName;

        @Bind(R.id.brewery)
        TextView brewery;

        @Bind(R.id.description)
        TextView description;

        @Bind(R.id.rating)
        TextView rating;

        public BeerOnTapViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}