package com.example.smartpasal.fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.smartpasal.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class scan extends Fragment implements ZXingScannerView.ResultHandler {
    private static final int MY_CAMERA_REQUEST_CODE = 100;

ZXingScannerView scannerView;
LinearLayout qrCameraLayout;
    public scan() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {


        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_scan, scannerView, false);
        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        scannerView=new ZXingScannerView(getContext());
        qrCameraLayout = (LinearLayout) v.findViewById(R.id.qrCameraLayout);


        qrCameraLayout.addView(scannerView);
        scannerView.setResultHandler(this);


        return v;
    }


    @Override
    public void onResume() {


        scannerView.startCamera();
        super.onResume();
    }

    @Override
    public void onStop() {

        scannerView.stopCamera();



        super.onStop();
    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(getContext(),result.getText(),Toast.LENGTH_LONG).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

}
