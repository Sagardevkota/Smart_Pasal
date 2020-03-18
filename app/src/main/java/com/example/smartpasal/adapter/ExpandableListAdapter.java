package com.example.smartpasal.adapter;



import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpasal.R;
import com.example.smartpasal.fragment.Profile;
import com.example.smartpasal.view.HomeActivity;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    List<String> header_titles;
    HashMap<String,List<String>> child_titles;
    Context context;
     FragmentManager fragmentManager;
     Resources resources;


    public ExpandableListAdapter(Context context, Resources resources, List<String> header_titles, HashMap<String,List<String>> child_titles){
        this.resources=resources;
        this.context=context;
        this.header_titles=header_titles;
        this.child_titles=child_titles;

    }
    @Override
    public int getGroupCount() {
        return header_titles.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return child_titles.get(header_titles.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return header_titles.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return child_titles.get(header_titles.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final String title=(String) this.getGroup(i);
        if (view==null){
            LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.layout_parent,null);
        }
        TextView textView=view.findViewById(R.id.heading_item);
        textView.setText(title);


        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String title=(String) this.getChild(i,i1);
        if (view==null){
            LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.layout_child,null);



        }
        TextView textView=view.findViewById(R.id.child_items);
        textView.setText(title);
//        String lowTitle=title.trim().toLowerCase();
//
//        int res =resources.getIdentifier(lowTitle, "drawable", "com.example.smartpasal.R");
//        String reso="com.example.smartpasal.R.drawable."+lowTitle;
//        textView.setCompoundDrawablesWithIntrinsicBounds(Integer.parseInt(reso),0,0,0);
//        textView.setCompoundDrawablePadding(10);
//        Log.d("resname",reso);


        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
