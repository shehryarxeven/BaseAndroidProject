package com.xevensolutions.baseapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.xevensolutions.baseapp.MyBaseApp;
import com.xevensolutions.baseapp.R;

import java.util.ArrayList;
import java.util.List;

import de.mrapp.android.bottomsheet.BottomSheet;

public class ViewUtils {


    public static void showImageSourceSelectorDialog(Activity activity, int imageCount, boolean pickDocuments,
                                                     ArrayList<String> filePaths, List<Image> excludeImages) {
        BottomSheet.Builder builder = new BottomSheet.Builder(activity);
        builder.setTitle("");
        addItemToBottomSheetDialog(builder, R.string.pick_from_camera,
                android.R.drawable.ic_menu_camera);
        addItemToBottomSheetDialog(builder, R.string.pick_from_gallery,
                android.R.drawable.ic_menu_gallery);
        if (pickDocuments)
            addItemToBottomSheetDialog(builder, R.string.select_document,
                    R.drawable.ic_baseline_insert_drive_file_24);


        BottomSheet bottomSheet = builder.create();
        bottomSheet.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    pickImages(1, true, activity, (ArrayList<Image>) excludeImages);
                    break;
                case 1:
                    pickImages(imageCount, false, activity, (ArrayList<Image>) excludeImages);
                    break;
                case 2:
                    pickAttachments(activity);
                    break;
            }
        });


        bottomSheet.show();
    }

    public static void pickAttachments(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword,",
                "application/pdf",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation"

        };

        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);


        activity.startActivityForResult(intent, Constants.PICK_FILES);
    }


    public static void pickImages(int imageCount, boolean cameraOnly, Activity activity
            , ArrayList<Image> excludedFiles) {


        if (!cameraOnly)
            ImagePicker.create(activity).limit(imageCount).exclude(excludedFiles).showCamera(false).start();
        else
            ImagePicker.cameraOnly().start(activity);
    }


    private static void addItemToBottomSheetDialog(BottomSheet.Builder builder, int text,
                                                   int icon) {
        builder.addItem(icon, text, icon);

    }

    public static void setFieldError(EditText etTaskName, String string) {
        etTaskName.setError(string);
        etTaskName.requestFocus();
    }


    public static void setTintToProgressBar(ProgressBar progressBar, int tint) {
        progressBar.getIndeterminateDrawable().setColorFilter(tint, android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    public static void setTintToSeekbar(SeekBar seekbar, int tint) {
        seekbar.setProgressTintList(ColorStateList.valueOf(tint));
        seekbar.setThumbTintList(ColorStateList.valueOf(tint));
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp) {
        Context context = MyBaseApp.getContext();
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px) {
        Context context = MyBaseApp.getContext();
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public static int getScreenWidth() {
        //return activity.getWindow().getDecorView().getHeight();
        return Resources.getSystem().getDisplayMetrics().widthPixels;


    }
}
