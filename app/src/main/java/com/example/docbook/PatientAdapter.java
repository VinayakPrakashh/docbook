package com.example.docbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public PatientAdapter(Context context, List<Product> productList) {


        this.context = context;
        this.productList = productList;


    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = productList.get(position);
        holder.textViewname.setText(product.getName());
        holder.textViewNumber.setText(product.getNumber());



    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewname,textViewNumber;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewname = itemView.findViewById(R.id.product_name);
            textViewNumber = itemView.findViewById(R.id.product_price);

        }
    }

    public static class Product {
        private String name;

        private String number;

        public Product(String name,String number) {
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }


        public String getNumber() {
            return number;
        }
    }
}
