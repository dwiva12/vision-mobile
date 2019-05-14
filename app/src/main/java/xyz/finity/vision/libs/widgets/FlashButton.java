package xyz.finity.vision.libs.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import xyz.finity.vision.R;

/**
 * Created by dwiva on 4/23/19.
 * This code is a part of Vision project
 */

public class FlashButton extends AppCompatImageButton {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {FlashMode.OFF, FlashMode.ON, FlashMode.AUTO})
    public @interface FlashMode {
        int OFF = 0;
        int ON = 1;
        int AUTO = 2;
    }

    @FlashMode
    private int flashMode = 0;

    public FlashButton(Context context) {
        this(context, null);
    }

    public FlashButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlashButton(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFlashMode(FlashMode.OFF);
    }

    public void setFlashMode(@FlashMode int flashMode) {
        this.flashMode = flashMode;

        switch (flashMode) {
            case FlashMode.OFF:
//                    setContentDescription(getResources().getString(R.string.btn_repeat_off));
                setImageResource(R.drawable.ic_flash_off_24dp);
//                setIconAlpha(0.4f);
                break;
            case FlashMode.ON:
//                    setContentDescription(getResources().getString(R.string.btn_repeat_all));
                setImageResource(R.drawable.ic_flash_on_24dp);
                break;
            case FlashMode.AUTO:
//                    setContentDescription(getResources().getString(R.string.btn_repeat_one));
                setImageResource(R.drawable.ic_flash_auto_24dp);
                break;
        }
    }

    public void setIconAlpha(float alpha) {
        getDrawable().setAlpha((int) (255 * alpha));
    }

    public void toggle() {
        switch (flashMode) {
            case FlashMode.OFF:
                setFlashMode(FlashMode.ON);
                break;
            case FlashMode.ON:
                setFlashMode(FlashMode.AUTO);
                break;
            case FlashMode.AUTO:
                setFlashMode(FlashMode.OFF);
                break;
        }
    }
}

