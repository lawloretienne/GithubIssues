package com.etiennelawlor.issues.views;

/**
 * Created by etiennelawlor on 12/6/14.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.etiennelawlor.issues.R;


public class CustomFontTextView extends TextView {
    public CustomFontTextView(Context context) {
        super(context);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes( attrs, R.styleable.CustomFontTextView, 0, 0);
        try {
            String fontName = getFontName(a.getInteger(R.styleable.CustomFontTextView_textFont, 0));
            if (!fontName.equals("")) {
                try {
                    setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName));
                } catch (Exception e) {
                    Log.e("CustomFontTextView", e.getMessage());
                }
            }

        } finally {
            a.recycle();
        }
    }

    private String getFontName(int index) {

        switch (index) {
            case 0 :
                return "Roboto-Black.ttf";
            case 1 :
                return "Roboto-BlackItalic.ttf";
            case 2 :
                return "Roboto-Bold.ttf";
            case 3 :
                return "Roboto-BoldItalic.ttf";
            case 4 :
                return "Roboto-Italic.ttf";
            case 5 :
                return "Roboto-Light.ttf";
            case 6 :
                return "Roboto_LightItalic.ttf";
            case 7 :
                return "Roboto-Medium.ttf";
            case 8 :
                return "Roboto-MediumItalic.ttf";
            case 9 :
                return "Roboto-Regular.ttf";
            case 10 :
                return "Roboto-Thin.ttf";
            case 11 :
                return "Roboto-ThinItalic.ttf";
            case 12 :
                return "RobotoCondensed-Bold.ttf";
            case 13 :
                return "RobotoCondensed-BoldItalic.ttf";
            case 14 :
                return "RobotoCondensed-Italic.ttf";
            case 15 :
                return "RobotoCondensed-Light.ttf";
            case 16 :
                return "RobotoCondensed-LightItalic.ttf";
            case 17 :
                return "RobotoCondensed-Regular.ttf";
            default:
                return "";
        }
    }
}

