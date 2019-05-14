package xyz.finity.vision;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.media.ExifInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.support.design.widget.TabLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.finity.vision.libs.adapters.BlockAdapter;
import xyz.finity.vision.libs.adapters.LabelAdapter;
import xyz.finity.vision.libs.adapters.ObjectAdapter;
import xyz.finity.vision.libs.fragments.LabelFragment;
import xyz.finity.vision.libs.fragments.OCRFragment;
import xyz.finity.vision.libs.fragments.ObjectFragment;
import xyz.finity.vision.libs.fragments.WebFragment;
import xyz.finity.vision.libs.models.BestGuessLabel;
import xyz.finity.vision.libs.models.Block;
import xyz.finity.vision.libs.models.Label;
import xyz.finity.vision.libs.models.Object;
import xyz.finity.vision.libs.models.Vertices;
import xyz.finity.vision.libs.models.VisionData;
import xyz.finity.vision.libs.models.Web;
import xyz.finity.vision.libs.services.RetrofitClientInstance;
import xyz.finity.vision.libs.services.VisionService;
import xyz.finity.vision.libs.utils.FileUtils;

public class ResultActivity extends AppCompatActivity {

    AppBarLayout appBar;
    Toolbar toolbar;
    TabLayout tab;
    ViewPager viewPager;
    ImageView imageView;
    private Bitmap bitmap;
    private float defScale;
    File file;
    Uri uri;
    ArrayList<Rect> objectsRect;
    ArrayList<Rect> blocksRect;

    public static final int OBJECTS_RECT = 0;
    public static final int BLOCK_RECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        appBar = findViewById(R.id.appbar);
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.view_pager);
        tab = findViewById(R.id.tab);
        imageView = findViewById(R.id.image);

        tab.setupWithViewPager(viewPager);

        final int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = appBar.getLayoutParams();
        params.height = width;
        appBar.setLayoutParams(params);
        appBar.setElevation(getResources().getDimensionPixelSize(R.dimen.fab_margin));

        /*appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            private int mTargetElevation = ResourcesUtils.toPixels(6);
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int spaceHeight = width - (tab.getHeight() + toolbar.getHeight());
                verticalOffset = Math.abs(verticalOffset);

//                int difference = appBarLayout.getTotalScrollRange() - toolbarHeight;
                float fraction = Math.abs((float) verticalOffset / spaceHeight);
//                appBar.setAlpha(1 - fraction);
//                appBar.getBackground().setAlpha((int) ((1 - fraction) * 255));
//                BitmapDrawable drawable = (BitmapDrawable) appBar.getBackground();
                imageView.setScaleX(1 + fraction * 0.5f);
                imageView.setScaleY(1 + fraction * 0.5f);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    appBarLayout.setElevation(getResources().getDimensionPixelSize(R.dimen.appbar_padding_top) * fraction);
                }
            }
        });*/

        appBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*defScale = 1;
                final int height = Resources.getSystem().getDisplayMetrics().heightPixels;
                ViewGroup.LayoutParams params = appBar.getLayoutParams();
                params.height = height;
                appBar.setLayoutParams(params);

                imageView.setScaleX(1);
                imageView.setScaleY(1);*/
            }
        });

        if (getIntent().getIntExtra("contentType", 0) == 0) {
            final String path = getIntent().getStringExtra("path");
            file = new File(path);
        } else {
            uri = getIntent().getParcelableExtra("uri");
            file = FileUtils.getFile(this, uri);
        }

        bitmap = openBitmapFromFile(this, file);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(new FileInputStream(file));

            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationDegree = exifToDegrees(rotation);

            Matrix matrix = new Matrix();
            if (rotation != 0) {matrix.preRotate(rotationDegree);}

            Bitmap adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (adjustedBitmap != null) {
                bitmap = adjustedBitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setDefaultScale(bitmap.getWidth(), bitmap.getHeight());
        BitmapDrawable drawable;
        drawable = new BitmapDrawable(getResources(), bitmap);
        imageView.setImageDrawable(drawable);

        sendRequest();
    }

    private void sendRequest() {
        VisionService service = RetrofitClientInstance.getRetrofitInstance()
                .create(VisionService.class);

        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileRequestBody);

        String desc = "description";
        RequestBody description = RequestBody.create(MultipartBody.FORM, desc);

        Call<VisionData> call = service.check(part, description);
        final Callback<VisionData> callback = new Callback<VisionData>() {
            @Override
            public void onResponse(Call<VisionData> call, Response<VisionData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ResultActivity.this, "success ", Toast.LENGTH_SHORT).show();
                    /*try {
                        Log.i("response_: ", response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    displayList(response.body());

                    List<Object> objects = response.body().getObjects();

                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();

                    objectsRect = new ArrayList<>();
                    for (Object object : objects) {
                        Vertices vertices = object.getVertices();
                        objectsRect.add(new Rect(
                                (int) (vertices.getLeft() * width),
                                (int) (vertices.getTop() * height),
                                (int) (vertices.getRight() * width),
                                (int) (vertices.getBottom() * height)
                        ));
                    }

                    if (response.body().getOcr() != null) {
                        List<Block> blocks = response.body().getOcr().getBlocks();
                        blocksRect = new ArrayList<>();
                        for (Block block : blocks) {
                            Vertices vertices = block.getVertices();
                            blocksRect.add(new Rect(
                                    (int) (vertices.getLeft() * width),
                                    (int) (vertices.getTop() * height),
                                    (int) (vertices.getRight() * width),
                                    (int) (vertices.getBottom() * height)
                            ));
                        }
                    }

                    drawVertices(bitmap, objectsRect);

                    List<BestGuessLabel> labels = response.body().getWeb().getBestGuessLabels();
                    if (labels.size() > 0) {
                        toolbar.setTitle(labels.get(0).getLabel());
                    }
                } else {
                    Toast.makeText(ResultActivity.this, "unsuccessful ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(final Call<VisionData> call, Throwable t) {
                Toast.makeText(ResultActivity.this, "failed ", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
                builder.setMessage("Gagal menghubungkan ke API");
                builder.setPositiveButton("Ulangi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendRequest();
                    }
                });
                builder.setNegativeButton("Batal", null);
                builder.show();
                t.printStackTrace();
            }
        };
        call.enqueue(callback);
    }

    public Bitmap openBitmapFromFile(Context context, File file) {
        Bitmap bitmap = null;
        try {
            FileInputStream fis = new FileInputStream(file);
//            FileInputStream fis = context.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap openBitmapFromUri(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void drawBitmap(Bitmap bitmap) {
        BitmapDrawable drawable;
        drawable = new BitmapDrawable(getResources(), bitmap);
        imageView.setImageDrawable(drawable);
    }

    private void drawVertices(Bitmap source, ArrayList<Rect> rects) {
        int width = source.getWidth();
        int height = source.getHeight();
        int color = ResourcesCompat.getColor(getResources(), R.color.verticesMarker, getTheme());

        Paint paint = new Paint();
        paint.setColor(color);
//        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.verticesStrokeWidth));
        paint.setStrokeWidth(width * 0.005f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        Bitmap bitmap = source.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);

        for (Rect rect : rects) {
            canvas.drawRect(rect, paint);
        }

        BitmapDrawable drawable;
        drawable = new BitmapDrawable(getResources(), bitmap);
        imageView.setImageDrawable(drawable);
    }

    public void highlightVertices(int rectType, int index) {
        List<Rect> rects;
        if (rectType == 0) {
            rects = objectsRect;
        } else {
            rects = blocksRect;
        }
        int width = this.bitmap.getWidth();
        int color = ResourcesCompat.getColor(getResources(), R.color.verticesMarker, getTheme());
        int colorSelected = ResourcesCompat.getColor(getResources(), R.color.verticesSelectedMarker, getTheme());

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(width * 0.005f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        Paint paintBold = new Paint();
        paintBold.setColor(colorSelected);
        paintBold.setStrokeWidth(width * 0.010f);
        paintBold.setStyle(Paint.Style.STROKE);
        paintBold.setAntiAlias(true);

        Bitmap bitmap = this.bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);

        for (int i = 0; i < rects.size(); i++) {
            Rect rect = rects.get(i);
            if (i == index) {
                canvas.drawRect(rect, paintBold);
            } else {
                canvas.drawRect(rect, paint);
            }
        }

        BitmapDrawable drawable;
        drawable = new BitmapDrawable(getResources(), bitmap);
        imageView.setImageDrawable(drawable);
    }

    private void setDefaultScale(int width, int height) {
        int max;
        int min;
        if (width > height) {
            max = width;
            min = height;
        } else {
            max = height;
            min = width;
        }

        defScale = (float) max / min;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    private void displayList(VisionData data) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager(), data);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Toast.makeText(ResultActivity.this, "" + i, Toast.LENGTH_SHORT).show();
                String title = viewPager.getAdapter().getPageTitle(i).toString();
                switch (title) {
                    case "Object":
                        drawVertices(bitmap, objectsRect);
                        break;
                    case "Text":
                        drawVertices(bitmap, blocksRect);
                        break;
                    default:
                        drawBitmap(bitmap);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }




    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private static final int OBJECT_DETECTION = 0;
        private static final int LABEL_DETECTION = 1;
        private static final int WEB_DETECTION = 2;
        private static final int TEXT_DETECTION = 3;

        private VisionData visionData;
        private List<Integer> detectionList;

        public SectionsPagerAdapter(FragmentManager fm, VisionData visionData) {
            super(fm);
            this.visionData = visionData;
            detectionList = new ArrayList<>();
            if (visionData.getObjects().size() > 0) {
                detectionList.add(OBJECT_DETECTION);
            }
            if (visionData.getLabels().size() > 0) {
                detectionList.add(LABEL_DETECTION);
            }
            if (visionData.getWeb() != null) {
                detectionList.add(WEB_DETECTION);
            }
            if (visionData.getOcr() != null) {
                detectionList.add(TEXT_DETECTION);
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (detectionList.get(position)) {
                case OBJECT_DETECTION:
                    return ObjectFragment.newInstance(visionData.getObjects());
                case LABEL_DETECTION:
                    return LabelFragment.newInstance(visionData.getLabels());
                case WEB_DETECTION:
                    return WebFragment.newInstance(visionData.getWeb().getWebEntities());
                case TEXT_DETECTION:
                    return OCRFragment.newInstance(visionData.getOcr().getBlocks());
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return detectionList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (detectionList.get(position)) {
                case OBJECT_DETECTION:
                    return "Object";
                case LABEL_DETECTION:
                    return "Label";
                case WEB_DETECTION:
                    return "Web";
                case TEXT_DETECTION:
                    return "Text";
                default:
                    return null;
            }
        }
    }
}
