package se.vhaga.androidchallenge.search.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import se.vhaga.androidchallenge.R;
import se.vhaga.androidchallenge.network.models.CatImageModel;

/**
 * Created by vhaga on 2017-09-14.
 */

public class CatsGridAdapter extends RecyclerView.Adapter<CatsGridAdapter.CatViewHolder> {

    List<CatImageModel> cats;

    public CatsGridAdapter() {
        cats = new ArrayList<>();
    }

    public void setCats(List<CatImageModel> cats) {
        this.cats.clear();
        this.cats.addAll(cats);
    }

    @Override
    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cats, parent, false);

//        int height = parent.getMeasuredWidth() / 2;
//        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
//        layoutParams.height = height;
//        itemView.setLayoutParams(layoutParams);
        return new CatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CatViewHolder holder, int position) {
        CatImageModel cat = cats.get(position);
        holder.setId(cat.getId());
        holder.setCategory(cat.getCategory());
        holder.setImage(cat.getUrl());
    }

    @Override
    public int getItemCount() {
        return cats.size();
    }


    class CatViewHolder extends RecyclerView.ViewHolder {

        TextView labelId;
        TextView labelCategory;
        ImageView image;

        public CatViewHolder(View itemView) {
            super(itemView);
            labelId = itemView.findViewById(R.id.id);
            labelCategory = itemView.findViewById(R.id.category);
            image = itemView.findViewById(R.id.image);
        }

        public void setId(String id) {
            this.labelId.setText(String.format("id: %s", id));
        }

        public void setCategory(String category) {
            this.labelCategory.setText(String.format("category: %s", category));
        }

        public void setImage(String url) {
            Picasso.with(itemView.getContext()).load(url).into(image);
        }
    }
}
