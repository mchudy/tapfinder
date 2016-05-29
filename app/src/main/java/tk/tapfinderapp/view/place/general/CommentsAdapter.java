package tk.tapfinderapp.view.place.general;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tk.tapfinderapp.R;
import tk.tapfinderapp.model.CommentDto;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<CommentDto> comments;
    private Context context;

    public CommentsAdapter(Context context) {
        this.context = context;
        comments = Collections.emptyList();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,
                parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        CommentDto data = comments.get(position);
        String dateString = DateUtils.getRelativeDateTimeString(
                context,
                data.getDate().getTime(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                0
        ).toString();
        holder.date.setText(dateString);
        holder.text.setText(data.getText());
        holder.username.setText(data.getUserName());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.username)
        TextView username;
        @Bind(R.id.comment_date)
        TextView date;
        @Bind(R.id.comment_text)
        TextView text;

        public CommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}