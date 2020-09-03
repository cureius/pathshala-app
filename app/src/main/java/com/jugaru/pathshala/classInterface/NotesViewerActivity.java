package com.jugaru.pathshala.classInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jugaru.pathshala.MainActivity;
import com.jugaru.pathshala.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NotesViewerActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_viewer);

        webView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.progress_bar);
        String pdf_url = getIntent().getStringExtra("url");
        progressBar.setVisibility(View.VISIBLE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebChromeClient(new WebChromeClient()
//                                   {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
//                if(newProgress == 100) {
//                    progressBar.setVisibility(View.GONE);
//                }
//            }
//        }
        );
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:(function() { " +
                        "document.querySelector('[role=\"toolbar\"]').remove();})()");
                progressBar.setVisibility(View.GONE);
            }
        });
        String url = "";
        try {
            url= URLEncoder.encode( pdf_url , "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String FinalUrl = "https://docs.google.com/viewerng/viewer?embedded=true&url="+url;
        webView.loadUrl(FinalUrl);
        Toast.makeText(this, FinalUrl, Toast.LENGTH_SHORT).show();
    }
}
