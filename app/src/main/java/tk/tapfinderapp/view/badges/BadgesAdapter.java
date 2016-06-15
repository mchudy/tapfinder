package tk.tapfinderapp.view.badges;

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
import tk.tapfinderapp.model.BadgeDto;

public class BadgesAdapter extends RecyclerView.Adapter<BadgesAdapter.BadgeViewHolder>{

    private List<BadgeDto> items = Collections.emptyList();

    @Inject
    public BadgesAdapter() {
    }

    @Override
    public BadgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.badge_item,
                parent, false);
        return new BadgeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BadgeViewHolder holder, int position) {
        BadgeDto item = items.get(position);
        holder.name.setText(item.getTitle());
        holder.description.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<BadgeDto> items) {
        this.items = items;
    }

    public static class BadgeViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.badge_name)
        TextView name;

        @Bind(R.id.badge_description)
        TextView description;

        public BadgeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
