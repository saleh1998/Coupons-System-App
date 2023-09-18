package com.example.databases;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.ContentResolver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class CompanyCouponsLvAdapter extends ArrayAdapter {
    Context ctx;
    int res;
    ArrayList<Coupon> companyCoupons;

    public CompanyCouponsLvAdapter(@NonNull Context context, int resource, ArrayList<Coupon> companyCoupons) {
        super(context, resource,companyCoupons);

        ctx = context;
        res = resource;
        this.companyCoupons = companyCoupons;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        view = inflater.inflate(res,parent,false);
        Coupon c = companyCoupons.get(position);

        ImageView cpnImage = view.findViewById(R.id.couponLine_image);
        TextView tvCouponID = view.findViewById(R.id.couponLine_tvID);
        TextView tvCompanyID = view.findViewById(R.id.copunLine_tvCompanyID);
        TextView tvAmount = view.findViewById(R.id.copunLine_tvAmount);
        TextView tvPrice = view.findViewById(R.id.copunLine_tvPrice);
        TextView tvTitle = view.findViewById(R.id.copunLine_tvTitle);
        TextView tvStartDate = view.findViewById(R.id.copunLine_tvStartDate);
        TextView tvEndDate = view.findViewById(R.id.copunLine_tvEndDate);
        TextView tvCategory = view.findViewById(R.id.copunLine_tvCategory);
        TextView tvDescription = view.findViewById(R.id.copunLine_tvDescription);

/*

      Uri ImgUri = MediaStore.getMediaUri(getContext(),Uri.parse(c.getImage()));
      cpnImage.setImageURI(ImgUri);*/
        tvCouponID.setText(c.getId()+"");
        tvCompanyID.setText(c.getCompanyID()+"");
        tvCategory.setText(c.getCategory().toString());
        tvTitle.setText(c.getTitle());
        tvDescription.setText(c.getDescription());
        tvStartDate.setText(c.getStartDate().toString());
        tvEndDate.setText(c.getEndDate().toString());
        tvAmount.setText(c.getAmount()+"");
        tvPrice.setText(c.getPrice()+"");

//        Uri imgUri = Uri.parse(c.getImage());
//        cpnImage.setImageURI(imgUri);

//        Bitmap bitmap = BitmapFactory.decodeFile(c.getImage());
//        cpnImage.setImageBitmap(bitmap);

        Picasso.get().load(c.getImage()).into(cpnImage);




        return view;
    }
    public void refreshAllCoupons(ArrayList<Coupon> coupons)
    {
        clear();
        addAll(coupons);
        notifyDataSetChanged();
    }

    public void refreshCouponAdded(Coupon c)
    {
        add(c);
        notifyDataSetChanged();
    }
}
