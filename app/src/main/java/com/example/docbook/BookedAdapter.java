package com.example.docbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookedAdapter extends RecyclerView.Adapter<BookedAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public BookedAdapter(Context context, List<Product> productList) {
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
        holder.textViewdate.setText(product.getDate());
        holder.textViewspec.setText(product.getSpecialization());


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewname, textViewdate,textViewspec;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewname = itemView.findViewById(R.id.product_name);
            textViewdate= itemView.findViewById(R.id.product_price);
            textViewspec = itemView.findViewById(R.id.product_quantity);

        }
    }

    public static class Product {
        private String name;
        private String date;
        private String specialization;

        public Product(String name,String specialization, String date) {
            this.name = name;
            this.date =date;
            this.specialization=specialization;

        }

        public String getName() {
            return name;
        }

        public String getDate() {
            return date;
        }
        public String getSpecialization() {
            return specialization;
        }
    }
}
