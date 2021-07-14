package com.example.smartpasal.paging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.model.ProductItems;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductPagingDataSource extends RxPagingSource<Integer, ProductItems> {

    private final SmartAPI.apiService mBackend;
    private String jwt;

    public ProductPagingDataSource(@NonNull SmartAPI.apiService backend, String jwt) {
        mBackend = backend;
        this.jwt = jwt;
    }

    @NotNull
    @Override
    public Single<LoadResult<Integer, ProductItems>> loadSingle(@NotNull LoadParams<Integer> loadParams) {
        try {
            // If page number is already there then init page variable with it otherwise we are loading fist page
            int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
            // Send request to server with page number
            return mBackend
                    .getProducts(jwt,page)
                    .delay(2, TimeUnit.SECONDS)
                    // Subscribe the result
                    .subscribeOn(Schedulers.io())
                    // Map result to LoadResult Object
                    .map(productItems -> toLoadResult(productItems, page))
                    // when error is there return error
                    .onErrorReturn(LoadResult.Error::new);
        } catch (Exception e) {
            // Request ran into error return error
            return Single.just(new LoadResult.Error(e));
        }
    }

    // Method to map Movies to LoadResult object
    private LoadResult<Integer, ProductItems> toLoadResult(List<ProductItems> productItems, int page) {
        return new LoadResult.Page(productItems, page == 1 ? null : page - 1, page + 1);
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NotNull PagingState<Integer, ProductItems> state) {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        Integer anchorPosition = state.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, ProductItems> anchorPage = state.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }

        return null;
    }

}
