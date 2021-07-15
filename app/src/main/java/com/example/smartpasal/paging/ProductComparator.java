package com.example.smartpasal.paging;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.smartpasal.model.ProductItems;

public class ProductComparator extends DiffUtil.ItemCallback<ProductItems> {
    @Override
    public boolean areItemsTheSame(@NonNull ProductItems oldItem, @NonNull ProductItems newItem) {
        return oldItem.getProductId() == newItem.getProductId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull ProductItems oldItem, @NonNull ProductItems newItem) {
        return oldItem.equals(newItem);
    }
}
