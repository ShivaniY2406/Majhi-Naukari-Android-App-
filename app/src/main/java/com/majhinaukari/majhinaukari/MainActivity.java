package com.majhinaukari.majhinaukari;

import static com.majhinaukari.majhinaukari.R.layout.activity_main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;


import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    String websiteURL = "https://majhinaukari.online/"; // sets web url
    private WebView webview;
    SwipeRefreshLayout mySwipeRefreshLayout;
    BottomNavigationView Circumnavigation;
    private ProgressBar progressBar;



    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         Circumnavigation = findViewById(R.id.bottom_line);






        if( ! CheckNetwork.isInternetAvailable(this)) //returns true if internet available
        {
            //if there is no internet do this
            setContentView(activity_main);
            //Toast.makeText(this,"No Internet Connection, Chris",Toast.LENGTH_LONG).show();

            new AlertDialog.Builder(this) //alert the person knowing they are about to close
                    .setTitle("No internet connection available")
                    .setMessage("Please Check you're Mobile data or Wifi network.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    //.setNegativeButton("No", null)
                    .show();

        }
        else
        {

            webview = findViewById(R.id.webView);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setDomStorageEnabled(true);
            webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
            webview.loadUrl(websiteURL);
            webview.setWebViewClient(new WebViewClientDemo());
            webview.setWebChromeClient(new WebChromeClientDemo());

        }

        //Swipe to refresh functionality
       mySwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.swipeContainer);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        webview.reload();
                    }
                }
        );

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(50);


       Circumnavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch(item.getItemId()){
                   case R.id.home: webview.loadUrl(websiteURL);
                                     return true;

                   case R.id.policy: webview.loadUrl("https://majhinaukari.online/privacy-policy/");
                                     return true;

                   case R.id.share:
                                   Intent intent = new Intent(Intent.ACTION_SEND);
                                   intent.setType("text/plain");
                                   String shareMessage = "आता मिळवा नौकरी विषयक जाहिरातींची खात्रीशीर माहिती आपल्या मोबाइल वर आत्ताच आमचे App डाउनलोड करा\n"+"https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID+"\n\n";
                                    intent.putExtra(Intent.EXTRA_TEXT,shareMessage);
                                    startActivity(Intent.createChooser(intent,"Share Via :"));
                                    return true;


                   case R.id.open:    String a = webview.getUrl();
                                      Uri uri  = Uri.parse(a);
                                      startActivity(new Intent(Intent.ACTION_VIEW,uri));
                                      return true;



                   case R.id.rate: {
                       try {
                           Uri ux = Uri.parse("market://details?id=" + getPackageName());
                           Intent ab = new Intent(Intent.ACTION_VIEW, ux);
                           ab.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(ab);

                       } catch (ActivityNotFoundException e) {
                           Uri ux = Uri.parse("market://details?id=" + getPackageName());
                           Intent ab = new Intent(Intent.ACTION_VIEW, ux);
                           ab.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(ab);
                       }
                       return true;
                   }
               }
               return false;
           }
       });
    }





    private class WebViewClientDemo extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
            mySwipeRefreshLayout.setRefreshing(false);
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

    }
    private class WebChromeClientDemo extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            progressBar.setProgress(progress);
        }
    }

    //set back button functionality
    @Override
    public void onBackPressed() { //if user presses the back button do this
        if (webview.isFocused() && webview.canGoBack()) { //check if in webview and the user can go back
            webview.goBack(); //go back in webview
        } else { //do this if the webview cannot go back any further

            new AlertDialog.Builder(this) //alert the person knowing they are about to close
                    .setTitle("EXIT")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }


}

class CheckNetwork {

    private static final String TAG = CheckNetwork.class.getSimpleName();

    public static boolean isInternetAvailable(Context context)
    {
        @SuppressLint("MissingPermission") NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            Log.d(TAG,"no internet connection");
            return false;
        }
        else
        {
            if(info.isConnected())
            {
                Log.d(TAG," internet connection available...");
                return true;
            }
            else
            {
                Log.d(TAG," internet connection");
                return true;
            }

        }
    }
}
