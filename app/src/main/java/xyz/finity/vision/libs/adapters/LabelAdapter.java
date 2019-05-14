package xyz.finity.vision.libs.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xyz.finity.vision.R;
import xyz.finity.vision.libs.models.Label;

/**
 * Created by dwiva on 4/22/19.
 * This code is a part of Vision project
 */
public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.ViewHolder> {
    private List<Label> labels;

    public LabelAdapter(List<Label> labels) {
        this.labels = labels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.label_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Label item = getItem(position);
        viewHolder.tvLabel.setText(item.getDescription());
        viewHolder.tvConfidence.setText(String.valueOf(item.getScore()));
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    public Label getItem(int position) {
        return labels.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLabel;
        private TextView tvConfidence;

        public ViewHolder(View itemView) {
            super(itemView);

            tvLabel = itemView.findViewById(R.id.description);
            tvConfidence = itemView.findViewById(R.id.score);
        }
    }



}
