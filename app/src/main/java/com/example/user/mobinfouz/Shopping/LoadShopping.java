package com.example.user.mobinfouz.Shopping;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.user.mobinfouz.entity.ShopItem;
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

public class LoadShopping extends AsyncTask<Void, Void, ArrayList<ShopItem>> {

    ShoppingActivity mActivity;
    String url;
    int error = 0;

    public LoadShopping(ShoppingActivity q, String f){
        mActivity  = q;
        url = f;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        error = 0;
        mActivity.mError.setVisibility(View.GONE);
        mActivity.mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<ShopItem> doInBackground(Void... params) {
        ArrayList<ShopItem> arrayList = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://esavdo.uz/product/search?q=" + url)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()){
                Document document = Jsoup.parse(response.body().string());
                Elements elements = document.select("div.master-wrapper-main").select("div.search-results")
                        .select("div.product-list").select("div.item-box");
                for (Element element : elements){
                    ShopItem esavdo = new ShopItem();
                    try {
                        if (element.select("div.details").select("h2.product-title")
                                .text() != null){
                            esavdo.title = element.select("div.details").select("h2.product-title")
                                    .text();
                        }
                        esavdo.price = element.select("div.product-price").select("span[class=price]").text();
                        esavdo.img = element.select("div.picture")
                                .select("img").first().attr("data-original");
                        esavdo.url = element.select("div.picture img").attr("src");
                        esavdo.logo  = document.select("div.header-logo").select("img").attr("src");
                        arrayList.add(esavdo);
                    }catch (Exception exception){
                        error = -1;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!arrayList.isEmpty()){
            ShopItem item = arrayList.get(0);
            arrayList.add(0, item);
        }
        if (arrayList.isEmpty()){
            error = -1;
        }
        return arrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<ShopItem> list) {
        mActivity.mProgress.setVisibility(View.GONE);
        if (error == 0){
            mActivity.mError.setVisibility(View.GONE);
            mActivity.mAdapter.addAll(list);
            mActivity.mAdapter.notifyDataSetChanged();
            mActivity = null;
        }else {
            mActivity.mError.setVisibility(View.VISIBLE);
        }
    }
}
