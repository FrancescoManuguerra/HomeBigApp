package com.manuguerra.homebigapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.manuguerra.homebigapp.R;
import com.manuguerra.homebigapp.datamodels.Product;
import com.manuguerra.homebigapp.datamodels.Restaurant;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<Product> products;
    private float minimoOrdine;
    private float status;
    private float basket;
    private Context context;
    private Restaurant restaurant;
    private com.manuguerra.homebigapp.interfaces.onQuantityChangedListener onQuantityChangedListener;

    public ProductAdapter(ArrayList<Product> products, Context context) {
        this.context = context;
        this.products = products;
    }

    public void setDataProduct(ArrayList<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void setDataRestaurant(Restaurant restaurant) {
    this.restaurant=restaurant;

    notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_restaurant_product, viewGroup, false);
        return new ProductViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder viewHolder, int i) {

        viewHolder.product_price.setText(String.valueOf(products.get(i).getPrice()) + " €");
        viewHolder.product_name.setText(products.get(i).getName());
        viewHolder.product_quantity.setText(String.valueOf(products.get(i).getQuantity()));

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public com.manuguerra.homebigapp.interfaces.onQuantityChangedListener getOnQuantityChangedListener() {
        return onQuantityChangedListener;
    }

    public void setOnQuantityChangedListener(com.manuguerra.homebigapp.interfaces.onQuantityChangedListener onQuantityChangedListener) {
        this.onQuantityChangedListener = onQuantityChangedListener;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        final TextView product_name;
        final TextView product_price;
        final TextView product_quantity;
        final Button add;
        final Button subtract;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_quantity = itemView.findViewById(R.id.product_quantity);

            add = itemView.findViewById(R.id.product_add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    products.get(getAdapterPosition()).increaseQuantity();
                    onQuantityChangedListener.onChange(products.get(getAdapterPosition()).getPrice());
                    notifyItemChanged(getAdapterPosition());
                }
            });
            subtract = itemView.findViewById(R.id.product_subtract);
            subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (products.get(getAdapterPosition()).getQuantity() == 0) return;
                    else products.get(getAdapterPosition()).decreaseQuantity();
                    onQuantityChangedListener.onChange(products.get(getAdapterPosition()).getPrice() * -1);
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }
}
