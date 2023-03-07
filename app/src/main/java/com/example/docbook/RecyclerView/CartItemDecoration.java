package com.example.docbook.RecyclerView;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CartItemDecoration extends RecyclerView.ItemDecoration {
    private final int verticalSpaceHeight;

    public CartItemDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = verticalSpaceHeight;
        outRect.bottom = verticalSpaceHeight;
    }
}
