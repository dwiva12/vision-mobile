package xyz.finity.vision.libs.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.finity.vision.R;
import xyz.finity.vision.ResultActivity;
import xyz.finity.vision.libs.models.AnnotatedImage;
import xyz.finity.vision.libs.services.RetrofitClientInstance;
import xyz.finity.vision.libs.services.VisionService;

/**
 * Created by dwiva on 4/22/19.
 * This code is a part of Vision project
 */
public class AnnotatedImageAdapter extends RecyclerView.Adapter<AnnotatedImageAdapter.ViewHolder> {
    private List<AnnotatedImage> annotatedImages;
    private OnItemClickListener listener;

    public AnnotatedImageAdapter(List<AnnotatedImage> annotatedImages) {
        this.annotatedImages = annotatedImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.home_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(AnnotatedImageAdapter.this, holder.getAdapterPosition());
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        AnnotatedImage item = getItem(position);
        viewHolder.tvLabel.setText(String.valueOf(item.getToken()));
        viewHolder.setToken(String.valueOf(item.getToken()));
    }

    @Override
    public int getItemCount() {
        return annotatedImages.size();
    }

    public AnnotatedImage getItem(int position) {
        return annotatedImages.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLabel;
        private String token;
        private ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);

            tvLabel = itemView.findViewById(R.id.description);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }

        public void setToken(String token) {
            this.token = token;
            downloadFile(token);
        }

        public void setThumbnail(Bitmap bitmap) {
            thumbnail.setImageBitmap(bitmap);
        }

        private void downloadFile(String token) {
            VisionService service = RetrofitClientInstance.getRetrofitInstance()
                    .create(VisionService.class);
            Call<ResponseBody> call = service.downloadFile("feed/"  + token + ".thumb.jpg");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        InputStream iStream = response.body().byteStream();
                        new DownloadTask(ViewHolder.this).execute(iStream);
                    } else {
                        Log.w(ResultActivity.class.getSimpleName(), "download unsuccessful");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(ResultActivity.class.getSimpleName(), "failed to download file");
                }
            });
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(AnnotatedImageAdapter adapter, int position);
    }

    private static class DownloadTask extends AsyncTask<InputStream, Void, Bitmap> {

        WeakReference<AnnotatedImageAdapter.ViewHolder> holderRef;

        public DownloadTask(AnnotatedImageAdapter.ViewHolder holder) {
            holderRef = new WeakReference<>(holder);
        }

        @Override
        protected Bitmap doInBackground(InputStream... inputStreams) {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStreams[0]);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (holderRef.get() != null) {
                holderRef.get().setThumbnail(bitmap);
            }
        }
    }
}
