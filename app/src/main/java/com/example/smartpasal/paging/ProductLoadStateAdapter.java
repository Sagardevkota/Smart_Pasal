package com.example.smartpasal.paging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpasal.R;
import com.example.smartpasal.databinding.LoadStateItemBinding;


public class ProductLoadStateAdapter extends LoadStateAdapter<ProductLoadStateAdapter.LoadStateViewHolder> {

    // Define Retry Callback
    private View.OnClickListener mRetryCallback;

    public ProductLoadStateAdapter(View.OnClickListener retryCallback) {
        // Init Retry Callback
        mRetryCallback = retryCallback;
    }

    @Override
    public void onBindViewHolder(@NonNull LoadStateViewHolder loadStateViewHolder, @NonNull LoadState loadState) {

        loadStateViewHolder.bind(loadState);
    }

    @NonNull
    @Override
    public LoadStateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @NonNull LoadState loadState) {
        // Return new LoadStateViewHolder object
        return new LoadStateViewHolder(parent, mRetryCallback);
    }

    public static class LoadStateViewHolder extends RecyclerView.ViewHolder{

        // Define Progress bar
        private ProgressBar mProgressBar;
        // Define error TextView
        private TextView mErrorMsg;
        // Define Retry button
        private Button mRetry;

        LoadStateViewHolder(
                @NonNull ViewGroup parent,
                @NonNull View.OnClickListener retryCallback) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.load_state_item, parent, false));
            LoadStateItemBinding binding = LoadStateItemBinding.bind(itemView);
            mProgressBar = binding.progressBar;
            mErrorMsg = binding.errorMsg;
            mRetry = binding.retryButton;
            mRetry.setOnClickListener(retryCallback);
        }

        public void bind(LoadState loadState) {
            // Check load state
            if (loadState instanceof LoadState.Error) {
                // Get the error
                LoadState.Error loadStateError = (LoadState.Error) loadState;
                // Set text of Error message
                mErrorMsg.setText(loadStateError.getError().getLocalizedMessage());
            }
            // set visibility of widgets based on LoadState
            mProgressBar.setVisibility(loadState instanceof LoadState.Loading
                    ? View.VISIBLE : View.GONE);
            mRetry.setVisibility(loadState instanceof LoadState.Error
                    ? View.VISIBLE : View.GONE);
            mErrorMsg.setVisibility(loadState instanceof LoadState.Error
                    ? View.VISIBLE : View.GONE);
        }
    }
}
