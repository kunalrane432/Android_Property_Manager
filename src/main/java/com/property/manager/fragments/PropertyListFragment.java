package com.property.manager.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.property.manager.R;
import com.property.manager.model.Property;
import com.property.manager.recycler_view.PropertyListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PropertyListFragment extends Fragment {

    private RecyclerView mPropertyRecyclerView;
    private PropertyListAdapter mAdapter;

    private List<Property> mProperties;

    public PropertyListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_list, container, false);
        mPropertyRecyclerView = (RecyclerView) view
                .findViewById(R.id.property_recycler_view);
        mPropertyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        generateProperty();
        return view;
    }




    @SuppressLint("NotifyDataSetChanged")
    private void updateUI(){
        //PropertyLab crimeLab = PropertyLab.get();
        //List<Property> properties = crimeLab.getProperties();
        generateProperty();
        if (mAdapter == null) {
            mAdapter = new PropertyListAdapter(getActivity(), mProperties);
            mPropertyRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    private void generateProperty()
    {
        mProperties = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Property property = new Property();
            property.setPropertyId(UUID.randomUUID());
            property.setPropertyName("Property Name #" + i);
            property.setPropertyDescription("Property Description #" + i);
            mProperties.add(property);
        }
    }
}
