package com.example.docbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public DoctorAdapter(Context context, List<Product> productList) {
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
        holder.textViewtime.setText(product.getTime());
        holder.textViewdate.setText(product.getDate());



    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewname, textViewtime,textViewdate;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewname = itemView.findViewById(R.id.product_name);
            textViewtime = itemView.findViewById(R.id.product_price);
            textViewdate = itemView.findViewById(R.id.product_quantity);

        }
    }

    public static class Product {
        private String name;
        private String time;
        private String date;

        public Product(String name, String time,String date) {
            this.name = name;
            this.time = time;
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public String getTime() {
            return time;
        }
        public String getDate() {
            return date;
        }
    }
}
