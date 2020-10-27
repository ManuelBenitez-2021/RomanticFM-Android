package tcking.github.com.rfm.aacplay;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebViewActivity extends Activity {

    WebView web_view;
    String url="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        web_view = (WebView) findViewById(R.id.web_view);

        if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
        }


    /*    final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(WebViewActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();

                }
            }
        }, 10000);*/





        web_view.setWebViewClient(new WebViewClient() {


            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);




                return true;
            }


            public void onLoadResource (WebView view, String url) {
                Log.d("yjhjeneno","yo 1");


            }
            public void onPageFinished(WebView view, String url) {
                Log.d("yjhjeneno","yo 2");

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.d("yjhjeneno","yo "+error);
            }

            /*  @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);

                Log.d("loadderr","1");

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();

                }
            }*/
        });



        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        web_view.loadUrl(url);

        web_view.getSettings().setLoadWithOverviewMode(true);
        web_view.getSettings().setUseWideViewPort(true);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        web_view=null;
    }
}
