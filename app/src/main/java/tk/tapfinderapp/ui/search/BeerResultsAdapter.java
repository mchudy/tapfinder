package tk.tapfinderapp.ui.search;

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
import tk.tapfinderapp.model.beer.BeerDto;
import tk.tapfinderapp.ui.base.FragmentChanger;
import tk.tapfinderapp.ui.beer.BeerDetailsFragment;

public class BeerResultsAdapter extends RecyclerView.Adapter<BeerResultsAdapter.BeerResultViewHolder> {

    private List<BeerDto> beers;
    private FragmentChanger fragmentChanger;

    @Inject
    public BeerResultsAdapter(FragmentChanger fragmentChanger) {
        this.fragmentChanger = fragmentChanger;
        beers = Collections.emptyList();
    }

    @Override
    public BeerResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.beer_item,
                parent, false);
        return new BeerResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BeerResultViewHolder holder, int position) {
        BeerDto beer = beers.get(position);
        holder.beerName.setText(beer.getName());
        holder.brewery.setText(beer.getBrewery().getName());
        holder.beerName.setOnClickListener(v -> fragmentChanger.changeFragment(
                BeerDetailsFragment.newInstance(beer.getId())));
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    public void setBeers(List<BeerDto> beers) {
        this.beers = beers;
    }

    public static class BeerResultViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.beer_name)
        TextView beerName;

        @Bind(R.id.brewery)
        TextView brewery;

        public BeerResultViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}