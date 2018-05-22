package com.example.tipix1998.recetasfinal.Main.Usuario;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tipix1998.recetasfinal.R;

import java.util.ArrayList;


public class UsrIngredientAdapter extends RecyclerView.Adapter<UsrIngredientAdapter.ViewHolder> {
    Context context;
    private static ArrayList<String> product = new ArrayList<String>();
    public  UsrIngredientAdapter(Context context){
        this.context = context;
    }
    @NonNull
    @Override
    public UsrIngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_ingredient, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsrIngredientAdapter.ViewHolder holder, final int position) {
        holder.txtIngredient.setText( product.get( position ) );

        holder.btnRemove.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.remove( position );
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder {
        TextView txtIngredient;
        FloatingActionButton btnRemove;
        public ViewHolder(View itemView) {
            super(itemView);
            txtIngredient = (TextView) itemView.findViewById( R.id.txtIngredient );
            btnRemove = (FloatingActionButton) itemView.findViewById( R.id.btnDelet );
        }
    }

    public void setIngredient(String ingrediente){
        product.add( ingrediente );
    }

    public static ArrayList<String> getIngredient() {
        return product;
    }

    public void clearIngredient(){
        product.clear();
    }

}
