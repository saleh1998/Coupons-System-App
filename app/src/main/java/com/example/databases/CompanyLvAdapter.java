package com.example.databases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CompanyLvAdapter extends ArrayAdapter {
    Context ctx;
    int res;
    ArrayList<Company> companies;
    public CompanyLvAdapter(@NonNull Context context, int resource, ArrayList<Company> companies) {
        super(context, resource,companies);

        ctx = context;
        res = resource;
        this.companies = companies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        view = inflater.inflate(res,parent,false);
        Company c = companies.get(position);
        TextView tvCompanyCode = view.findViewById(R.id.companyLine_tvComCode);
        TextView tvCompanyName = view.findViewById(R.id.companyLine_tvComName);
        TextView tvCompanyPassword = view.findViewById(R.id.companyLine_tvComPassword);
        TextView tvCompanyMail = view.findViewById(R.id.companyLine_tvComMail);
        TextView tv = view.findViewById(R.id.companyLine_tvComPassword);

        tvCompanyCode.setText(c.getId()+"");
        tvCompanyName.setText(c.getName());
        tvCompanyPassword.setText(c.getPassword());
        tvCompanyMail.setText(c.getEmail());


        return view;
    }

    public void refreshAllCompanies(ArrayList<Company> arrayList) {
        clear();
        addAll(arrayList);
        notifyDataSetChanged();
    }
    public void refreshCompanyAdded(Company c) {
        add(c);
        notifyDataSetChanged();
    }
}
