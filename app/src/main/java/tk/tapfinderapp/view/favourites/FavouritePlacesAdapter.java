package tk.tapfinderapp.view.favourites;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.googleplaces.Place;
import tk.tapfinderapp.view.FragmentChanger;
import tk.tapfinderapp.view.place.PlaceFragment;

public class FavouritePlacesAdapter extends RecyclerView.Adapter<FavouritePlacesAdapter.PlaceViewHolder> {

    private List<Place> items = new ArrayList<>();
    private FragmentChanger fragmentChanger;

    public FavouritePlacesAdapter(FragmentChanger fragmentChanger) {
        this.fragmentChanger = fragmentChanger;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item,
                parent, false);
        return new PlaceViewHolder(itemView, this::onItemClicked);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Place item = items.get(position);
        holder.name.setText(item.getName());
        holder.address.setText(item.getFormattedAddress());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Place item) {
        items.add(item);
    }

    private void onItemClicked(int position) {
        fragmentChanger.changeFragment(PlaceFragment.newInstance(items.get(position)));
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemClickListener listener;

        @Bind(R.id.place_name)
        TextView name;

        @Bind(R.id.address)
        TextView address;

        public PlaceViewHolder(View itemView, ItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClicked(getLayoutPosition());
        }

        public interface ItemClickListener{
            void onItemClicked(int position);
        }
    }
}