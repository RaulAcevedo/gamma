package com.gamma.test.raul.myapplication.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.gamma.test.raul.myapplication.R;
import com.gamma.test.raul.myapplication.model.Beer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.BeerViewHolder>{

    private Context context;
    private List<Beer> items;

    public BeerAdapter(Context context, List<Beer> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public BeerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_beer,viewGroup,false);
        return new BeerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeerViewHolder beerViewHolder, int i) {
        Beer item = items.get(i);

        Glide.with(context)
                .load(item.getImage_url())
                .apply(new RequestOptions().error(R.drawable.broken_image))
                .transition(DrawableTransitionOptions.withCrossFade()).into(beerViewHolder.beer_image);

        beerViewHolder.beer_name.setText(item.getName());
        beerViewHolder.beer_tagline.setText(item.getTagline());
        beerViewHolder.beer_description.setText(item.getDescription());
        beerViewHolder.beer_card.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items!= null?items.size():0;
    }

    class BeerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.beer_card)
        public View beer_card;

        @BindView(R.id.beer_image)
        public ImageView beer_image;

        @BindView(R.id.beer_name)
        public TextView beer_name;

        @BindView(R.id.beer_tagline)
        public TextView beer_tagline;

        @BindView(R.id.beer_description)
        public TextView beer_description;

        public BeerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
