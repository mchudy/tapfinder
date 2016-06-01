package tk.tapfinderapp.view.findbeer;

import android.content.Context;
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
import tk.tapfinderapp.model.FindBeerSearchResultItem;

public class FindBeerResultsAdapter extends RecyclerView.Adapter<FindBeerResultsAdapter.FindBeerResultViewHolder>{

    private List<FindBeerSearchResultItem> items = Collections.emptyList();
    private Context context;

    public FindBeerResultsAdapter(Context context) {
        this.context = context;
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
        //holder.distance.setText();
        FindBeerResultBeersAdapter beersAdapter = new FindBeerResultBeersAdapter();
        holder.beers.setLayoutManager(new LinearLayoutManager(context));
        holder.beers.setAdapter(beersAdapter);
        beersAdapter.setBeers(item.getBeers());
        beersAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
     }

    public void setItems(List<FindBeerSearchResultItem> items) {
        this.items = items;
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
