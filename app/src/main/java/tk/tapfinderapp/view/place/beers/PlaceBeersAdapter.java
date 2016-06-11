package tk.tapfinderapp.view.place.beers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.BeerDto;
import tk.tapfinderapp.model.LikeDto;
import tk.tapfinderapp.model.PlaceBeerDto;
import tk.tapfinderapp.service.TapFinderApiService;
import tk.tapfinderapp.view.BaseActivity;
import tk.tapfinderapp.view.beer.BeerDetailsFragment;

public class PlaceBeersAdapter extends RecyclerView.Adapter<PlaceBeersAdapter.BeerOnTapViewHolder> {

    private List<PlaceBeerDto> placesBeers;
    private TapFinderApiService apiService;
    private Context context;

    @Inject
    public PlaceBeersAdapter(TapFinderApiService apiService, Context context) {
        this.apiService = apiService;
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
        BeerDto beer = placeBeer.getBeer();
        holder.beerName.setText(beer.getName());
        holder.brewery.setText(beer.getBrewery().getName());
        holder.description.setText(placeBeer.getDescription());
        holder.rating.setText(String.valueOf(placeBeer.getRating()));
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        holder.price.setText(format.format(placeBeer.getPrice()));
        holder.style.setText(beer.getStyle());

        holder.like.setOnClickListener(v -> updateLike(placeBeer.getId(), true, holder.rating));
        holder.dislike.setOnClickListener(v -> updateLike(placeBeer.getId(), false, holder.rating));
        holder.beerName.setOnClickListener(v -> ((BaseActivity)context).changeFragmentWithBackStack(
                BeerDetailsFragment.newInstance(beer.getId())));
    }

    private void updateLike(int id, boolean liked, TextView rating) {
        LikeDto dto = new LikeDto(id, liked);
        apiService.updateLike(dto)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(response -> refreshRating(rating, id),
                    t -> Timber.wtf(t.getMessage()));
    }

    private void refreshRating(TextView rating, int id) {
        apiService.getRating(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> rating.setText(String.valueOf(result.getRating())),
                        t -> Timber.wtf(t.getMessage()));
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

        @Bind(R.id.price)
        TextView price;

        @Bind(R.id.style)
        TextView style;

        @Bind(R.id.like)
        ImageView like;

        @Bind(R.id.dislike)
        ImageView dislike;

        public BeerOnTapViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}