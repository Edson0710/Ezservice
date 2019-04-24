package com.example.edson0710.ezservice;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DivideRecycler extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public DivideRecycler(Resources context) {
        mDivider = context.getDrawable(R.drawable.divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //divider padding give some padding whatever u want or disable
        int left = parent.getPaddingLeft() + 1;
        int right = parent.getWidth() - parent.getPaddingRight() - 1;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
