package com.example.user.mobinfouz.Shopping;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.mobinfouz.R;
import com.example.user.mobinfouz.entity.ShopItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ShoppingHolder> {

    final static int COMPANY = 0, REGULAR = 1;

    public List<ShopItem> mList = new ArrayList<>();

    public void addAll(ArrayList<ShopItem> q){
        mList.addAll(q);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 ){
            return COMPANY;
        }else {
            return REGULAR;
        }
    }

    @Override
    public ShoppingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == REGULAR){
            return new EsavdoHolder(inflater.inflate(R.layout.shopping_list_item, parent, false));
        }else {
            return new CompanyLogo(inflater.inflate(R.layout.selling_company, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(ShoppingHolder holder, int position) {
        holder.onBind(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static abstract class ShoppingHolder extends RecyclerView.ViewHolder{
        public ShoppingHolder(View itemView) {super(itemView);}
        public abstract void onBind(ShopItem esavdo, int i);
    }

    public class EsavdoHolder extends ShoppingHolder{
        TextView mHeader, mPrice;
        ImageView mImage;
        public EsavdoHolder(View itemView) {
            super(itemView);
            mHeader = (TextView)itemView.findViewById(R.id.shop_header);
            mPrice = (TextView)itemView.findViewById(R.id.shop_price);
            mImage = (ImageView)itemView.findViewById(R.id.shop_image);

        }

        @Override
        public void onBind(final ShopItem esavdo, final int k) {
            mHeader.setText(esavdo.title);
            mPrice.setText(esavdo.price);
            if (esavdo.img != null){
                Picasso.with(itemView.getContext()).load(esavdo.img).into(mImage);
            }
            mHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://esavdo.uz" + esavdo.url));
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public class CompanyLogo extends ShoppingHolder{
        ImageView mImage;
        public CompanyLogo(View itemView) {
            super(itemView);

            mImage = (ImageView)itemView.findViewById(R.id.company_logo);

        }
        @Override
        public void onBind(ShopItem esavdo, int k) {
            Picasso.with(itemView.getContext()).load("http://esavdo.uz"
                    + esavdo.logo).into(mImage);
        }
    }

    public class CompanySeller extends ShoppingHolder{
        public CompanySeller(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(ShopItem esavdo, int k) {

        }
    }
}
