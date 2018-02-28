package com.example.user.mobinfouz.Search;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.user.mobinfouz.LoadData;
import com.example.user.mobinfouz.MainActivity;
import com.example.user.mobinfouz.R;
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
import java.util.ArrayList;

public class LoadSearch extends AsyncTask<Void, Void, ArrayList<Article>>{

    SearchableActivity mActivity;
    int page = 1, progress_code = -1, error = 1;
    String section;

    public LoadSearch(SearchableActivity searchableActivity, String query, int number, int f) {
        mActivity = searchableActivity;
        section = query;
        page = number;
        progress_code = f;
    }

    @Override
    protected void onPreExecute() {
        if (progress_code != 1){
            mActivity.Loading.setText(mActivity.getResources().getString(R.string.loading));
        }
    }

    @Override
    protected ArrayList<Article> doInBackground(Void... params) {
        ArrayList<Article> arrayList = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://mobinfo.uz/page/" + page + "?s=" + section)
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
                    try {
                        article.setHeadline(element.select("div.entry-title").select("a[href]").first().text());
                        article.setText(element.select("div.entry-short").first().text());
                        article.setDate(element.select("footer.entry-meta").select("a[href]").first().text());
                        article.setUrl(element.select("div.entry-title").select("a").attr("href"));
                        if(element.select("div.entry-image").select("figure").size() > 0){
                            String string = element.select("div.entry-image").select("figure").attr("style");
                            int begin = string.indexOf("'");
                            int end = string.indexOf(")");
                            article.setImg_adress(string.substring(begin + 1, end - 1));
                        }
                    }catch (Exception e){
                        error = -1;
                        break;
                    }
                    arrayList.add(article);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles) {
        if (error != -1){
            mActivity.Error.setText("");
            mActivity.mAdapter.addAll(articles);
            mActivity.Loading.setText("");
        }else{
            mActivity.Loading.setText("");
            mActivity.Error.setText(mActivity.getResources().getString(R.string.error));
        }
        mActivity = null;
    }
}
