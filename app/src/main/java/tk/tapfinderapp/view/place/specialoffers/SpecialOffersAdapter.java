package tk.tapfinderapp.view.place.specialoffers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.SpecialOfferDto;

public class SpecialOffersAdapter extends RecyclerView.Adapter<SpecialOffersAdapter.SpecialOfferViewHolder> {

    private List<SpecialOfferDto> specialOffers;
    private Context context;

    public SpecialOffersAdapter(Context context) {
        this.context = context;
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
        //holder.rating.setText(dto.getRating());

        DateFormat dateFormat =
                android.text.format.DateFormat.getDateFormat(context);
        holder.date.setText(String.format("%s - %s", dateFormat.format(dto.getStartDate()),
                dateFormat.format(dto.getEndDate())));
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

        public SpecialOfferViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}