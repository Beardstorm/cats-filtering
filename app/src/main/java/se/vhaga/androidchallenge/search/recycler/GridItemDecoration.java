package se.vhaga.androidchallenge.search.recycler;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vhaga on 2017-09-14.
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int numColumns;
    private int spacing;

    public GridItemDecoration(int numColumns, int spacing) {
        this.numColumns = numColumns;
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % numColumns; // item column

        outRect.left = spacing - column * spacing / numColumns; // spacing - column * ((1f / numColumns) * spacing)
        outRect.right = (column + 1) * spacing / numColumns; // (column + 1) * ((1f / numColumns) * spacing)

        if (position < numColumns) { // top edge
            outRect.top = spacing;
        }

        outRect.bottom = spacing; // item bottom
    }
}