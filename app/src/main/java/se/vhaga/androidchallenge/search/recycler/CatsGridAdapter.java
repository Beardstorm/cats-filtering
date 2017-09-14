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

    private List<CatImageModel> cats;
    private OnCatClickedListener onCatClickListener;

    public CatsGridAdapter() {
        cats = new ArrayList<>();
    }

    public void setCats(List<CatImageModel> cats) {
        this.cats.clear();
        this.cats.addAll(cats);
    }

    public void setOnCatClickListener(OnCatClickedListener onCatClickListener) {
        this.onCatClickListener = onCatClickListener;
    }

    @Override
    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cats, parent, false);
        return new CatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CatViewHolder holder, int position) {
        final CatImageModel cat = cats.get(position);
        holder.setId(cat.getId());
        holder.setCategory(cat.getCategory());
        holder.setImage(cat.getUrl());

        if (onCatClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCatClickListener.onCatClicked(cat);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cats.size();
    }


    class CatViewHolder extends RecyclerView.ViewHolder {

        private TextView labelId;
        private TextView labelCategory;
        private ImageView image;

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

    public interface OnCatClickedListener {
        void onCatClicked(CatImageModel cat);
    }
}
