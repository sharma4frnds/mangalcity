package net.clamour.mangalcity.Home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;
    private static final String TAG = "Post";
    /**
     * Supporting only LinearLayoutManager for now.
     *
     * @param layoutManager
     */
    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();


        if(firstVisibleItemPosition == 0) showForm();
        else hideForm();


        if (!recyclerView.canScrollVertically(1)) {
            if (!isLoading() && !isLastPage()) {
                onScrolledToEnd();
            }
        }

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= getTotalPageCount()) {
                loadMoreItems();
            }
        }

    }

    protected abstract void showForm();

    protected abstract void hideForm();

    protected abstract void onScrolledToEnd();

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();

}
