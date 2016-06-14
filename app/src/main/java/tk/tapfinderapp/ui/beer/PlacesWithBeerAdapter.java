package tk.tapfinderapp.ui.beer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import lombok.AllArgsConstructor;
import lombok.Value;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.googleplaces.Place;
import tk.tapfinderapp.ui.base.FragmentChanger;
import tk.tapfinderapp.ui.place.PlaceFragment;

public class PlacesWithBeerAdapter extends RecyclerView.Adapter<PlacesWithBeerAdapter.PlaceWithBeerViewHolder>{

    private List<PlaceBeerItem> places = new ArrayList<>();
    private FragmentChanger fragmentChanger;

    public PlacesWithBeerAdapter(FragmentChanger fragmentChanger) {
        this.fragmentChanger = fragmentChanger;
    }

    @Override
    public PlaceWithBeerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_with_beer_item,
                parent, false);
        return new PlaceWithBeerViewHolder(itemView, this::onItemClicked);
    }

    private void onItemClicked(View view, int position) {
        fragmentChanger.changeFragment(PlaceFragment.newInstance(places.get(position).getPlace()));
    }

    @Override
    public void onBindViewHolder(PlaceWithBeerViewHolder holder, int position) {
        PlaceBeerItem item = places.get(position);
        holder.placeName.setText(item.getPlace().getName());
        holder.address.setText(item.getPlace().getFormattedAddress());
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        holder.price.setText(format.format(item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void addPlace(Place place, double price) {
        places.add(new PlaceBeerItem(place, price));
    }

    public static class PlaceWithBeerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemClickListener listener;

        @Bind(R.id.place_name)
        TextView placeName;

        @Bind(R.id.price)
        TextView price;

        @Bind(R.id.address)
        TextView address;

        public PlaceWithBeerViewHolder(View view, ItemClickListener mListener) {
            super(view);
            this.listener = mListener;
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClicked(v, getLayoutPosition());
        }

        public interface ItemClickListener {
            void onItemClicked(View caller, int position);
        }
    }

    @Value
    @AllArgsConstructor
    private static class PlaceBeerItem {
        Place place;
        double price;
    }
}
