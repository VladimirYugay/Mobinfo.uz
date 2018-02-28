package com.example.user.mobinfouz;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.user.mobinfouz.entity.Article;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class LoadData extends AsyncTask<Void, Void, ArrayList<Article>> {

    MainActivity mActivity;
    int page;
    String section;
    int load = 0;


    public LoadData(MainActivity a, String s, int i, int q) {
        mActivity = a;
        page = i;
        section = s;
        load = q;
    }

    @Override
    protected void onPreExecute() {
        if (load != 1){
            mActivity.mProgress.setVisibility(View.VISIBLE);
        }
    }



    @Override
    protected ArrayList<Article> doInBackground(Void... params) {
        ArrayList<Article> arrayList = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://mobinfo.uz/" + section + "/page/" + page)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()){
                Document document = Jsoup.parse(response.body().string());
                Elements elements = document.select("div#content").select("article");
                for (Element element : elements){
                    Article article = new Article();
                    article.setHeadline(element.select("div.entry-title").select("a[href]").first().text());
                    article.setText(element.select("div.entry-short").first().text());
                    article.setDate(element.select("footer.entry-meta").select("a[href]").first().text());
                    article.setUrl(element.select("div.entry-title").select("a").attr("href"));
                    if (element.select("div.entry-image").select("figure").size() > 0){
                        String string = element.select("div.entry-image").select("figure").attr("style");
                        int begin = string.indexOf("'");
                        int end = string.indexOf(")");
                        article.setImg_adress(string.substring(begin + 1, end - 1));
                    }
                    arrayList.add(article);
                }
                Article article = arrayList.get(arrayList.size() - 1);
                article.adv = "AXAX";
                int f = 3;
                for (int i = 0; i < arrayList.size(); i++){
                    if (i == f){
                        arrayList.add(f, article);
                        f += 4;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles) {
        page = 1;
        mActivity.mAdapter.addAll(articles);
        mActivity.mProgress.setVisibility(View.GONE);
        mActivity = null;
    }
}
