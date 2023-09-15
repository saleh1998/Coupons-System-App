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

public class CustomerLvAdapter extends ArrayAdapter {

    Context ctx;
    int res;
    ArrayList<Customer> customers;
    public CustomerLvAdapter(@NonNull Context context, int resource, ArrayList<Customer> customers) {
        super(context, resource,customers);

        ctx = context;
        res = resource;
        this.customers = customers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        view = inflater.inflate(res,parent,false);
        Customer c = customers.get(position);
        TextView tvCustomerCode = view.findViewById(R.id.customerLine_tvCustomerCode);
        TextView tvCustomerFName = view.findViewById(R.id.customerLine_tvFName);
        TextView tvCustomerLName = view.findViewById(R.id.customerLine_tvLName);
        TextView tvCustomerEmail = view.findViewById(R.id.customerLine_tvEmail);
        TextView tvCustomerPassword = view.findViewById(R.id.customerLine_tvPassword);


        tvCustomerCode.setText(c.getId()+"");
        tvCustomerFName.setText(c.getFirstName());
        tvCustomerLName.setText(c.getLastName());
        tvCustomerEmail.setText(c.getEmail());
        tvCustomerPassword.setText(c.getPassword());


        return view;
    }

    public void refreshAllCustomers(ArrayList<Customer> arrayList) {
        clear();
        addAll(arrayList);
        notifyDataSetChanged();
    }
    public void refreshCustomerAdded(Customer c) {
        add(c);
        notifyDataSetChanged();
    }

}
