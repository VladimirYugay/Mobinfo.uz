package com.example.user.mobinfouz;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.util.Util;
import com.example.user.mobinfouz.entity.Article;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.BinderHolder> {

    final int REGULAR = 0, ADV = 1;
    public List<Article> mList = new ArrayList<>();
    int previous = -1;

    public void addAll(ArrayList<Article> s){
        mList.addAll(s);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (!TextUtils.isEmpty(mList.get(position).adv)){
            return ADV;
        }else {
            return REGULAR;
        }
    }

    @Override
    public BinderHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if (i == REGULAR){
            return new RegularHolder(inflater.inflate(R.layout.main_card_view, viewGroup, false));
        }else {
            return new AdvHolder(inflater.inflate(R.layout.adv_text, viewGroup, false));
        }

    }

    @Override
    public void onBindViewHolder(BinderHolder binderHolder, int i) {
        binderHolder.onBind(mList.get(i));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static abstract class BinderHolder extends RecyclerView.ViewHolder {
        public BinderHolder(View itemView) {
            super(itemView);
        }
        public abstract void onBind(Article article);
    }

    public class RegularHolder extends BinderHolder{
        TextView mText, mHeaderText, mDate;
        ImageView mImage;
        ProgressBar mProgress;
        public RegularHolder(View itemView) {
            super(itemView);
            mText = (TextView)itemView.findViewById(R.id.text);
            mHeaderText = (TextView)itemView.findViewById(R.id.header_text);
            mImage = (ImageView)itemView.findViewById(R.id.image);
            mDate = (TextView)itemView.findViewById(R.id.date);

            mProgress = (ProgressBar)itemView.findViewById(R.id.image_progress);
        }

        @Override
        public void onBind(final Article article) {
            mHeaderText.setText(article.getHeadline());
            mText.setText(article.getText());
            mDate.setText("Запись опубликована: " + article.getDate());

            mProgress.setVisibility(View.VISIBLE);
            Picasso.with(itemView.getContext()).load(article.getImg_adress())
                    .into(mImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            mProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });


            mHeaderText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PageLoader.class);
                    intent.putExtra("url", article.getUrl());
                    v.getContext().startActivity(intent);
                }
            });

            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PageLoader.class);
                    intent.putExtra("url", article.getUrl());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public class AdvHolder extends BinderHolder{
        TextView mText;
        public AdvHolder(View itemView) {
            super(itemView);
            mText = (TextView)itemView.findViewById(R.id.adv_text);
            mText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://mobile.beeline.uz/ru/main/action/action.wbp?id=efdcaf09-7674-42da-9fbc-24c79811c3ea"));
                    v.getContext().startActivity(intent);
                }
            });
        }
        @Override
        public void onBind(Article article) {
        }
    }
}
