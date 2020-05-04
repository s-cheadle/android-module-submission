package com.c3469518.finalmvvmtodoapp.UI.ListAdapter;

/**
 * Generic ItemClickListener to be used in RecyclerView.
 *
 * @param <T> Item being clicked
 */
public interface ItemClickListener<T> {
    /**
     * Triggered on Item in RecyclerView being clicked.
     *
     * @param item object representing the item being clicked
     */
    void onItemClick(T item);
}