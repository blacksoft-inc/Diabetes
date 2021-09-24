package com.medical.diabetes.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.medical.diabetes.MainActivity;
import com.medical.diabetes.R;
import com.medical.diabetes.fragments.utils.Type_FragmentType;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebNavigator extends Type_FragmentType {
    private String title;
    private String fileName;
    private AlertDialog dialog;

    public WebNavigator(String title, String fileName) {
        this.title = title;
        this.fileName = fileName;

    }

    public WebNavigator(String title, String fileName, AlertDialog dialog) {
        this.title = title;
        this.fileName = fileName;
        this.dialog = dialog;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_navigator, container, false);
    }//

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setting the name of the page from section name
        ((TextView) view.findViewById(R.id.section_name)).setText(title);
        //back navigation
        (view.findViewById(R.id.back_navigation)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragmentManager.popBackStack();
            }
        });
        //setting the content of the webview
        WebView webView = (view.findViewById(R.id.web_view));
        //
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        //
        webView.getSettings().setBuiltInZoomControls(true);/*enable zooming the content*/
        webView.loadUrl("file:///android_asset/" + fileName);
    }//

    @Override
    public int type() {
        return 30;
    }


    @Override
    public void onDestroyView() {
        if (dialog != null) dialog.show();
        super.onDestroyView();
    }
}//
