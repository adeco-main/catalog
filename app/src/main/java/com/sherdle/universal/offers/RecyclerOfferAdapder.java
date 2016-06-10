package com.sherdle.universal.offers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sherdle.universal.R;
import com.sherdle.universal.db.objects.OfferObject;
import com.sherdle.universal.util.Helper;

import java.util.List;


public class RecyclerOfferAdapder extends RecyclerView.Adapter<RecyclerOfferAdapder.ViewHolder> {


    private Context context;
    private List<OfferObject> values;
    public static final String TAG = "RecyclerOfferAdapder";

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewThumbl;
        private TextView textViewTitle;
        private CardView cardView;
        private RatingBar ratingBar;
        private Drawable drawable;
        private Button button;

        public ViewHolder(View v) {
            super(v);
            textViewTitle = (TextView) v.findViewById(R.id.textViewTitle);
            imageViewThumbl = (ImageView) v.findViewById(R.id.imageViewThumbl);
            ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
            cardView = (CardView) v.findViewById(R.id.cardView);
            button = (Button) v.findViewById(R.id.button);
        }
    }

    public RecyclerOfferAdapder(Context context, List<OfferObject> values) {
        this.context = context;
        this.values = values;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_offer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        OfferObject currentOffer = values.get(position);
        ImageLoader.getInstance().displayImage(currentOffer.getThumbUrl(), holder.imageViewThumbl);
        holder.textViewTitle.setText(currentOffer.getName());
        holder.drawable = holder.ratingBar.getProgressDrawable();
        DrawableCompat.setTint(holder.drawable, Color.GRAY);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Helper.onVerificationUrl(values.get(position).getDownloadUrl()))));
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Helper.onVerificationUrl(values.get(position).getDownloadUrl()))));
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}