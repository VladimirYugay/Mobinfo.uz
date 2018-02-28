package com.example.user.mobinfouz;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

public class PageLoader extends AppCompatActivity {

    public  ImageView advImage;
    WebView mWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_loader);

        SlidrConfig config = new SlidrConfig.Builder()
                .primaryColor(getResources().getColor(R.color.basic))
                .secondaryColor(getResources().getColor(R.color.secondary))
                .sensitivity(0.07f)
                .build();
        Slidr.attach(this, config);

        final LinearLayout advLayout = (LinearLayout)findViewById(R.id.adv_layout);
//        advImage = (ImageView)findViewById(R.id.banner);
//        advImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("http://xabardor.uz/"));
//                startActivity(intent);
//            }
//        });
//        ImageView closeImage = (ImageView)findViewById(R.id.close_adv);
//        closeImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                advLayout.setVisibility(View.GONE);
//            }
//        });

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

            mWeb = (WebView)findViewById(R.id.webView);
            mWeb.getSettings().setJavaScriptEnabled(true);
            mWeb.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (!url.contains("http://mobinfo.uz") || url.contains("http://mobinfo.uz/feed")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        PageLoader.this.startActivity(intent);
                        return true;
                    }
                    return false;
                }
            });

        mWeb.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (mWeb.canGoBack()) {
                                mWeb.goBack();
                                return true;
                            }
                            break;
                    }
                }
                return false;
            }
        });
        mWeb.loadUrl(url);
    }
}

