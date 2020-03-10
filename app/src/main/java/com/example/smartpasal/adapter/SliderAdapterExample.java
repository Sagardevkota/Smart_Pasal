package com.example.smartpasal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartpasal.R;
import com.example.smartpasal.model.SliderItems;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterExample extends
        SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private List<SliderItems> mSliderItems = new ArrayList<>();

    public SliderAdapterExample(Context context) {
        this.context = context;
    }

    public void renewItems(List<SliderItems> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(SliderItems sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        SliderItems sliderItem = mSliderItems.get(position);

        viewHolder.textViewDescription.setText(sliderItem.getTvName());
        viewHolder.textViewDescription.setTextSize(16);
        viewHolder.textViewDescription.setTextColor(Color.WHITE);



        try{
            String url=sliderItem.getPicture_path();

            Picasso.get()
                    .load(url)
                    .fit()
                    .into(viewHolder.imageViewBackground, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Load","Successfull");

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Load",e.getMessage());
                        }
                    });}
        catch (Exception e){
            Log.d("error",e.getMessage());
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
             textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}