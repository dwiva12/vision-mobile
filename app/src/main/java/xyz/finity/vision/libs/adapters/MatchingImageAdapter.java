package xyz.finity.vision.libs.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xyz.finity.vision.R;
import xyz.finity.vision.libs.models.MatchingImage;
import xyz.finity.vision.libs.models.WebEntity;

/**
 * Created by dwiva on 4/22/19.
 * This code is a part of Vision project
 */
public class MatchingImageAdapter extends RecyclerView.Adapter<MatchingImageAdapter.ViewHolder> {
    private List<MatchingImage> matchingImages;
    private OnItemClickListener listener;

    public MatchingImageAdapter(List<MatchingImage> matchingImages) {
        this.matchingImages = matchingImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.label_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(MatchingImageAdapter.this, holder.getAdapterPosition());
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MatchingImage item = getItem(position);
        viewHolder.tvLabel.setText(item.getUrl());
    }

    @Override
    public int getItemCount() {
        return matchingImages.size();
    }

    public MatchingImage getItem(int position) {
        return matchingImages.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLabel;

        public ViewHolder(View itemView) {
            super(itemView);

            tvLabel = itemView.findViewById(R.id.description);
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(MatchingImageAdapter adapter, int position);
    }

}
