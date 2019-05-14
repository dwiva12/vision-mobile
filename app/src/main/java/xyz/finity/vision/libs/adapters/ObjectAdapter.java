package xyz.finity.vision.libs.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xyz.finity.vision.R;
import xyz.finity.vision.libs.models.Object;

/**
 * Created by dwiva on 4/22/19.
 * This code is a part of Vision project
 */
public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ViewHolder> {
    private List<Object> objects;
    private OnItemClickListener listener;
    private ViewHolder selectedHolder;

    public ObjectAdapter(List<Object> objects) {
        this.objects = objects;
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
                    listener.onItemClick(ObjectAdapter.this, holder.getAdapterPosition());
                    if (selectedHolder != null) {
                        selectedHolder.itemView.setSelected(false);
                    }
                    selectedHolder = holder;
                    selectedHolder.itemView.setSelected(true);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Object item = getItem(position);
        viewHolder.tvName.setText(item.getName());
        viewHolder.tvScore.setText(String.valueOf(item.getScore()));
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public Object getItem(int position) {
        return objects.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvScore;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.description);
            tvScore = itemView.findViewById(R.id.score);
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ObjectAdapter adapter, int position);
    }

}
