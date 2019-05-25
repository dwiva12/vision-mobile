package xyz.finity.vision.libs.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import xyz.finity.vision.R;
import xyz.finity.vision.ResultActivity;
import xyz.finity.vision.libs.services.DownloadImageService;

/**
 * Created by dwiva on 5/25/19.
 * This code is a part of Vision project
 */
public class ImageDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "AlertDialog";

    private ImageView image;
    private String imageUrl;
    private Button btnPositive;
    private CharSequence positiveText;
    private Button btnNegative;
    private CharSequence negativeText;
    private int dialogSize;
    private ProgressBar progressBar;
    private TextView title;

    private View.OnClickListener positiveListener;
    private View.OnClickListener negativeListener;

    public ImageDialog() {
        //Required empty constructor
    }

    public static ImageDialog newInstance(String imageUrl) {
        ImageDialog dialog = new ImageDialog();
        Bundle args = new Bundle();
        args.putString("imageUrl", imageUrl);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.dialog_image, container, false);

        // mencari view dari tvMessage, btnPositive dan btnNegative
        image = rootView.findViewById(R.id.image);
        title = rootView.findViewById(R.id.title);
        progressBar = rootView.findViewById(R.id.progress_bar);
        btnPositive = rootView.findViewById(R.id.btn_positive);
        btnNegative = rootView.findViewById(R.id.btn_negative);

        // mengeset pesan pada tvMessag serta mengeset text dan listener pada btnPositive dan btnNegative
        btnPositive.setText(positiveText);
        btnPositive.setOnClickListener(this);
        btnNegative.setText(negativeText);
        btnNegative.setOnClickListener(this);

        //Menyesuaikan tampilan dialog
        setupDialog();
        downloadImage();
        return rootView;
    }

    public void setupDialog() {
        // Menyesuaikan tampilan dialog serta mengatur animasi saat dialog dimunculkan dan ditutup.
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getDialog().getWindow().getAttributes().windowAnimations = R.style.windowAnimation;
        getDialog().setCanceledOnTouchOutside(false);

        // Menetukan ukuran lebar dialog, ukuran dialog disesuaikan dengan lebar layar (bagian yang
        // ukurannya lebih kecil). Contoh jka layar ponsel berukuran 3 x 4, maka lebar dialog
        // adalah 3 untuk orientasi portrait maupun landscape
        final DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        final int width = metrics.widthPixels;
        final int height = metrics.heightPixels;
        dialogSize = width < height ? width : height;
    }

    /*
     * Mengeset ukuran dialog (dialogSize) yang telah didapat sebelumnya pada method setupDialog()
     * agar ukuran dialog tetap sama pada saat layar dalam posisi portrait maupun landscape.
     * */
    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                dialogSize,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    // Mengeset text pesan yang akan ditampilkan pada dialog peringatan
    public void setImageUrl(String url) {
        this.imageUrl = imageUrl;
    }

    // Mengeset text pada btnPositive dan mengeset positiveListener yang akan dijalankan saat
    // konfirmasi positive (Ya) diklik
    public void setPositiveButton(CharSequence text, View.OnClickListener listener) {
        positiveText = text;
        positiveListener = listener;
    }

    // Mengeset text pada btnNegative dan mengeset negativeListener (listener dapat bernilai null)
    public void setNegativeButton(CharSequence text, View.OnClickListener listener) {
        negativeText = text;
        negativeListener = listener;
    }

    /*
     * Method onClick berfungsi untuk merespon aksi klik pada tombol.
     * btnNegative + Click = tutup dialog kemudian jalankan negativeListener dari pemanggil dialog jika ada.
     * btnPositive + Click = tutup dialog kemudian jalankan positiveListener dari pemanggil dialig
     * (misal matikan pompa ketika konfirmasi ya dipilih).
     * */
    public void onClick(View v) {
        ImageDialog.this.dismiss();

        switch(v.getId()) {
            case R.id.btn_positive:
                if (positiveListener != null) {
                    positiveListener.onClick(v);
                }
                break;
            case R.id.btn_negative:
                if (negativeListener != null) {
                    negativeListener.onClick(v);
                }
                break;
        }
    }

    public void setBitmap(Bitmap bitmap){
        progressBar.setVisibility(View.GONE);
        if (bitmap != null) {
            title.setText("Similar Image");
            image.setImageBitmap(bitmap);
            btnPositive.setText("Save");
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "NOT YET IMPLEMENTED", Toast.LENGTH_SHORT).show();
                }
            });
            btnPositive.setVisibility(View.VISIBLE);
        } else {
            title.setText("Download Failed");
            btnPositive.setText("Retry");
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadImage();
                    btnPositive.setVisibility(View.GONE);
                }
            });
            btnPositive.setVisibility(View.VISIBLE);
        }
    }

    private void downloadImage() {
        title.setText("Downloading");
        progressBar.setVisibility(View.VISIBLE);
        new DownloadTask(ImageDialog.this).execute(getArguments().getString("imageUrl"));
    }

    private static class DownloadTask extends AsyncTask<String, Void, Bitmap> {

        WeakReference<ImageDialog> dialogWeakReference;

        public DownloadTask(ImageDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        protected Bitmap doInBackground(String... imageUrl) {
            InputStream stream = null;
            try {
                URL url = new URL(imageUrl[0]);
                stream = url.openStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (dialogWeakReference.get() != null) {
                dialogWeakReference.get().setBitmap(bitmap);
            }
        }
    }
}
