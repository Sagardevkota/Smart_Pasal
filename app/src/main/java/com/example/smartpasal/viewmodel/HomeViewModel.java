package com.example.smartpasal.viewmodel;



import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.example.smartpasal.Session.Session;
import com.example.smartpasal.SmartAPI.SmartAPI;
import com.example.smartpasal.model.ProductItems;
import com.example.smartpasal.paging.ProductPagingDataSource;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class HomeViewModel extends ViewModel {
    // Define Flowable for movies
    public Flowable<PagingData<ProductItems>> pagingDataFlow;

    public HomeViewModel(){
    }


    // Init ViewModel Data
    public void init(Session session) {
        // Define Paging Source
        ProductPagingDataSource productPagingDataSource =
                new ProductPagingDataSource(SmartAPI.getApiService(),session.getJWT());

        // Create new Pager
        Pager<Integer, ProductItems> pager = new Pager(
                // Create new paging config
                new PagingConfig(
                        6, // pageSize - Count of items in one page
                        2, // prefetchDistance - Number of items to prefetch
                        false, // enablePlaceholders - Enable placeholders for data which is not yet loaded
                        6, // initialLoadSize - Count of items to be loaded initially
                        20 * 499),// maxSize - Count of total items to be shown in recyclerview
                () -> productPagingDataSource); // set paging source

        // inti Flowable
        pagingDataFlow = PagingRx.getFlowable(pager);
        CoroutineScope coroutineScope = ViewModelKt.getViewModelScope(this);
        PagingRx.cachedIn(pagingDataFlow, coroutineScope);

    }
}

