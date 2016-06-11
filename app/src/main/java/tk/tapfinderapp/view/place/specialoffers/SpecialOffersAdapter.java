package tk.tapfinderapp.view.place.specialoffers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.LikeDto;
import tk.tapfinderapp.model.SpecialOfferDto;
import tk.tapfinderapp.service.TapFinderApiService;

public class SpecialOffersAdapter extends RecyclerView.Adapter<SpecialOffersAdapter.SpecialOfferViewHolder> {

    private List<SpecialOfferDto> specialOffers;

    @Inject
    TapFinderApiService apiService;

    @Inject
    public SpecialOffersAdapter() {
        specialOffers = Collections.emptyList();
    }

    @Override
    public SpecialOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.special_offer_item,
                parent, false);
        return new SpecialOfferViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SpecialOfferViewHolder holder, int position) {
        SpecialOfferDto dto = specialOffers.get(position);
        holder.title.setText(dto.getTitle());
        holder.description.setText(dto.getDescription());
        holder.rating.setText(String.valueOf(dto.getRating()));

        DateFormat dateFormat =
                android.text.format.DateFormat.getDateFormat(holder.date.getContext());
        holder.date.setText(String.format("%s - %s", dateFormat.format(dto.getStartDate()),
                dateFormat.format(dto.getEndDate())));

        holder.like.setOnClickListener(v -> updateLike(dto.getId(), true, holder.rating));
        holder.dislike.setOnClickListener(v -> updateLike(dto.getId(), false, holder.rating));
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
        return specialOffers.size();
    }

    public void setSpecialOffers(List<SpecialOfferDto> beers) {
        this.specialOffers = beers;
    }

    public static class SpecialOfferViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.title)
        TextView title;

        @Bind(R.id.description)
        TextView description;

        @Bind(R.id.date)
        TextView date;

        @Bind(R.id.rating)
        TextView rating;

        @Bind(R.id.like)
        ImageView like;

        @Bind(R.id.dislike)
        ImageView dislike;

        public SpecialOfferViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}