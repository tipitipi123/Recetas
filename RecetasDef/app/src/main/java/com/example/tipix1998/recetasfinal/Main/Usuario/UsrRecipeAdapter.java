package com.example.tipix1998.recetasfinal.Main.Usuario;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tipix1998.recetasfinal.R;

public class UsrRecipeAdapter extends RecyclerView.Adapter<UsrRecipeAdapter.ViewHolder>  {
    Context context;
    public  UsrRecipeAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public UsrRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_recipe, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsrRecipeAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class  ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);

        }
    }

}
