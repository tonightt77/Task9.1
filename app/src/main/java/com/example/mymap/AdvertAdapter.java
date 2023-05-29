package com.example.mymap;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.AdvertViewHolder> {
    private LayoutInflater inflater;
    private List<Advert> adverts;

    @NonNull
    @Override
    public AdvertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View itemView = inflater.inflate(R.layout.item_advert, parent, false);
        return new AdvertViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertViewHolder holder, int position) {
        Advert advert = adverts.get(position);
        holder.typeTV.setText(advert.getType());
        holder.descriptionTV.setText(advert.getDescription());

        // Set the OnClickListener here
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AdvertDetailsActivity.class);
                intent.putExtra(AdvertDetailsActivity.EXTRA_ADVERT_ID, advert.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return adverts == null ? 0 : adverts.size();
    }

    public void setAdverts(List<Advert> adverts) {
        this.adverts = adverts;
    }

    public static class AdvertViewHolder extends RecyclerView.ViewHolder {
        public TextView typeTV;
        public TextView descriptionTV;

        public AdvertViewHolder(@NonNull View itemView) {
            super(itemView);
            typeTV = itemView.findViewById(R.id.typeTextView);
            descriptionTV = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}


