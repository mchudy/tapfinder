package tk.tapfinderapp.view.findbeer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.place.PlaceBeerDto;
import tk.tapfinderapp.view.FragmentChanger;
import tk.tapfinderapp.view.beer.BeerDetailsFragment;

public class FindBeerResultBeersAdapter extends RecyclerView.Adapter<FindBeerResultBeersAdapter.FindBeerResultBeerViewHolder>{

    private List<PlaceBeerDto> beers = Collections.emptyList();
    private FragmentChanger fragmentChanger;

    public FindBeerResultBeersAdapter(FragmentChanger fragmentChanger) {
        this.fragmentChanger = fragmentChanger;
    }

    @Override
    public FindBeerResultBeerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_beer_result_beer_item,
                parent, false);
        return new FindBeerResultBeerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FindBeerResultBeerViewHolder holder, int position) {
        PlaceBeerDto beer = beers.get(position);
        holder.beerName.setText(beer.getBeer().getName());
        holder.brewery.setText(beer.getBeer().getBrewery().getName());
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        holder.price.setText(format.format(beer.getPrice()));

        holder.beerName.setOnClickListener(v -> fragmentChanger.changeFragment(
                BeerDetailsFragment.newInstance(beer.getBeer().getId())));
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    public void setBeers(List<PlaceBeerDto> beers) {
        this.beers = beers;
    }

    public static class FindBeerResultBeerViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.beer_name)
        TextView beerName;

        @Bind(R.id.brewery)
        TextView brewery;

        @Bind(R.id.price)
        TextView price;

        public FindBeerResultBeerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
