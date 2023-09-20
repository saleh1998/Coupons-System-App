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
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CompanyCouponsLvAdapter extends ArrayAdapter {
    Context ctx;
    int res;
    ArrayList<Coupon> companyCoupons;

    CompanyFacade companyFacade;

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

        Date stardate = c.getStartDate();
        Date endate = c.getEndDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");

        tvCouponID.setText(c.getId()+"");
        companyFacade = new CompanyFacade(c.getCompanyID(),ctx);
        Company company = companyFacade.getCompanyDetails(c.getCompanyID());

if(company!=null)
{tvCompanyID.setText(company.getName());  }
        tvCategory.setText(c.getCategory().toString());
        tvTitle.setText(c.getTitle());
        tvDescription.setText(c.getDescription());
        tvStartDate.setText(simpleDateFormat.format(stardate));
        tvEndDate.setText(simpleDateFormat.format(endate));
        tvAmount.setText(c.getAmount()+"");
        tvPrice.setText(c.getPrice()+"");

//        Uri imgUri = Uri.parse(c.getImage());
//        cpnImage.setImageURI(imgUri);

//        Bitmap bitmap = BitmapFactory.decodeFile(c.getImage());
//        cpnImage.setImageBitmap(bitmap);

        String imagePathFromDB = c.getImage();
                Bitmap imageBitmap = loadImageFromInternalStorage(imagePathFromDB);
        if (imageBitmap != null) {
            cpnImage.setImageBitmap(imageBitmap);
        }


        return view;
    }
    private Bitmap loadImageFromInternalStorage(String path) {
        try {
            File file = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
