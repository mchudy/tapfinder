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
import tk.tapfinderapp.model.CommentDto;
import tk.tapfinderapp.service.TapFinderApiService;

public class BeersOnTapAdapter extends RecyclerView.Adapter<BeersOnTapAdapter.BeerOnTapViewHolder> {

    private List<CommentDto> beers;
    private Context context;

    @Inject
    TapFinderApiService service;

    public BeersOnTapAdapter(Context context) {
        this.context = context;
        beers = Collections.emptyList();
    }

    @Override
    public BeerOnTapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.beer_on_tap_item,
                parent, false);
        return new BeerOnTapViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BeerOnTapViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    public void setComments(List<CommentDto> beers) {
        this.beers = beers;
    }

    public static class BeerOnTapViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.username)
        TextView username;
        @Bind(R.id.comment_date)
        TextView date;
        @Bind(R.id.comment_text)
        TextView text;

        public BeerOnTapViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}