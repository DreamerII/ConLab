package com.android.denysyuk.conlab.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.denysyuk.conlab.R;
import com.android.denysyuk.conlab.adapters.RVAdapter;
import com.android.denysyuk.conlab.database.DataManager;
import com.android.denysyuk.conlab.models.Currencies;
import com.android.denysyuk.conlab.models.Finance;
import com.android.denysyuk.conlab.ui.customViews.ShareView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 28.09.15.
 */
public class ShareDialog extends DialogFragment {
    private ImageView mImage;
    private LinearLayout mLayout;
    private Button mBtnShare;
    private int position;
    private Finance mFinance;
    private Bitmap b;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFinance = DataManager.get(getActivity()).getFinance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_dialog_fragment, container, false);

        position = getArguments().getInt(RVAdapter.ORGANIZATION_POSITION);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mImage = (ImageView)v.findViewById(R.id.imageShare);

        mBtnShare = (Button)v.findViewById(R.id.btnShare);
        mBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBitmap(b);
            }
        });


        mLayout = new LinearLayout(getActivity());
        mLayout.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(getActivity());
        title.setText(mFinance.getOrganizations().get(position).getTitle());
        title.setTextSize(22);

        TextView region = new TextView(getActivity());
        region.setText(mFinance.getRegions().get(mFinance.getOrganizations().get(position).getRegionId()));

        TextView city = new TextView(getActivity());
        city.setText(mFinance.getCities().get(mFinance.getOrganizations().get(position).getCityId()));


        if(mFinance.getOrganizations().get(position).getCurrencies() != null) {
            ShareView shareView = new ShareView(getActivity(), position);
            Display display = getActivity().getWindowManager().getDefaultDisplay();

            shareView.setDrawingCacheEnabled(true);


            b = Bitmap.createBitmap(display.getWidth(), shareView.getH(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(b);
            shareView.draw(canvas);

            mImage.setImageBitmap(b);

        }

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        return dialog;
    }

    private void shareBitmap(Bitmap _bitmap) {
        Bitmap bitmap = _bitmap;
        if(bitmap != null){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File f = new File(Environment.getExternalStorageDirectory()
            +File.separator + "temporary_file.jpg");

            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            intent.putExtra(Intent.EXTRA_TEXT, "Finance");

            intent.putExtra(Intent.EXTRA_STREAM,
                    Uri.parse("file:///sdcard/temporary_file.jpg"));
            startActivity(Intent.createChooser(intent, "Share Image"));
        }
    }
}
