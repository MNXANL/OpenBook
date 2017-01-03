package com.example.pr_idi.mydatabaseexample;

import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;
import android.support.annotation.NonNull;
import android.widget.AbsListView;
import android.widget.ListView;
/**
 * Created by marta on 2/01/17.
 */

public class OnScrollUpDownListener implements AbsListView.OnScrollListener {
    private int previousScrollPosition;
    private int previousItemPosition;
    private int minDistance;
    private RecyclerView recview;
    private Action action;

    public OnScrollUpDownListener(@NonNull RecyclerView recview, int minDistance, @NonNull Action action) {
        this.recview = recview;
        this.minDistance = minDistance;
        this.action = action;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //nothing here
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int currentScrollPosition = getCurrentScrollPosition();
        if (firstVisibleItem == previousItemPosition) {
            int scrolled = Math.abs(previousScrollPosition - currentScrollPosition);
            if (scrolled > minDistance) {
                if (previousScrollPosition > currentScrollPosition) {
                    action.up();
                } else {
                    action.down();
                }
            }
        } else if (firstVisibleItem > previousItemPosition) {
            action.up();
        } else {
            action.down();
        }
        previousScrollPosition = currentScrollPosition;
        previousItemPosition = firstVisibleItem;

    }
    private int getCurrentScrollPosition() {
        int pos = 0;
        if (recview.getChildAt(0) != null) {
            pos = recview.getChildAt(0).getTop();
        }
        return pos;
    }
}
