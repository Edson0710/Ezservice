package com.example.edson0710.ezservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class Registro1 extends AppCompatActivity {

    WebView webView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro1);

        webView1 = (WebView) findViewById(R.id.webview1);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.getSettings().setBuiltInZoomControls(true);
        webView1.loadUrl("http://ezservice.tech/nuevo_uc.php");

    }
}
