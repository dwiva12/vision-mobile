package xyz.finity.vision;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.finity.vision.libs.adapters.AnnotatedImageAdapter;
import xyz.finity.vision.libs.models.AnnotatedImage;
import xyz.finity.vision.libs.models.HomeData;
import xyz.finity.vision.libs.services.RetrofitClientInstance;
import xyz.finity.vision.libs.services.VisionService;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, isPortrait ? 2 : 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration decoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.left = getResources().getDimensionPixelSize(R.dimen.listItemHorizontalMargin);
                outRect.top = getResources().getDimensionPixelSize(R.dimen.listItemVerticalMargin);
                outRect.right = getResources().getDimensionPixelSize(R.dimen.listItemHorizontalMargin);
                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.listItemVerticalMargin);
            }
        };
        recyclerView.addItemDecoration(decoration);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ImageSelectionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAnnotatedImageList(1);
    }

    private void getAnnotatedImageList(final int pageNumber) {
        VisionService service = RetrofitClientInstance.getRetrofitInstance()
                .create(VisionService.class);

        Call<HomeData> call = service.home(pageNumber);
        final Callback<HomeData> callback = new Callback<HomeData>() {
            @Override
            public void onResponse(Call<HomeData> call, Response<HomeData> response) {
                if (response.isSuccessful() && response.body() != null) {
//                    Toast.makeText(MainActivity.this, "pageCount: " + response.body().getPageCount(), Toast.LENGTH_SHORT).show();
                    displayList(response.body().getAnnotatedImages());
                } else {
                    Toast.makeText(MainActivity.this, "unsuccessful ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(final Call<HomeData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "failed ", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Gagal menghubungkan ke API");
                builder.setPositiveButton("Ulangi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getAnnotatedImageList(pageNumber);
                    }
                });
                builder.setNegativeButton("Batal", null);
                builder.show();
                t.printStackTrace();
            }
        };
        call.enqueue(callback);
    }

    private void displayList(List<AnnotatedImage> annotatedImages) {
        AnnotatedImageAdapter adapter = new AnnotatedImageAdapter(annotatedImages);
        adapter.setListener(new AnnotatedImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AnnotatedImageAdapter adapter, int position) {
                AnnotatedImage annotatedImage = adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("contentType", ResultActivity.CONTENT_TYPE_TOKEN);
                intent.putExtra("token", String.valueOf(annotatedImage.getToken()));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
