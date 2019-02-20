package com.manuguerra.homebigapp.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.manuguerra.homebigapp.datamodels.Product;
import com.manuguerra.homebigapp.R;
import com.manuguerra.homebigapp.interfaces.onTotalChangeListener;

import java.util.ArrayList;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {

    private ArrayList<Product> products;
    private com.manuguerra.homebigapp.interfaces.onTotalChangeListener onTotalChangeListener;
    private Context context;

        public CheckoutAdapter(ArrayList products, onTotalChangeListener listener, Context context)
        {
            this.products=products;
            this.onTotalChangeListener=listener;
            this.context=context;
        }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_checkout, viewGroup, false);
        return new CheckoutViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder checkoutViewHolder, int i) {

            Product product=products.get(i);
            checkoutViewHolder.product_name.setText(product.getName());
            checkoutViewHolder.product_price.setText(String.valueOf(product.getPrice()));
            checkoutViewHolder.product_quantity.setText(String.valueOf(product.getQuantity()));
    }

    @Override
        public int getItemCount() {
            return products.size() ;
        }

        class CheckoutViewHolder extends RecyclerView.ViewHolder {

            final TextView product_name;
            final TextView product_price;
            final TextView product_quantity;
            final ImageButton remove_product;

            public CheckoutViewHolder(@NonNull View itemView) {
                super(itemView);
                product_name = itemView.findViewById(R.id.product_name);
                product_price=itemView.findViewById(R.id.product_price);
                product_quantity=itemView.findViewById(R.id.product_quantity);
                remove_product=itemView.findViewById(R.id.remove_product);

                remove_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setMessage("Do you want cioccolato?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onTotalChangeListener.onChange(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();
                    }
                });
            }
        }
    }



