package com.property.manager.recycler_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.property.manager.MainActivity;

import com.property.manager.R;
import com.property.manager.model.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertyViewHolder extends RecyclerView.ViewHolder {

    private TextView propertyIdText;

    private TextView propertyNameText;

    private Context context;

    public List<Property> properties;

    private  Property property;

    public static String EXTRA_PROPERTY_ID = "property_id";


    private ImageView imageView;

    public PropertyViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        propertyIdText = (TextView) itemView.findViewById(R.id.list_item_property_id_text_view);
        propertyNameText = (TextView) itemView.findViewById(R.id.list_item_property_name_text_view);
        //imageView= (ImageView) itemView.findViewById(R.id.property_photo);
        //prope


        itemView.setOnClickListener(listItemClickListener);
    }

    public void bindProperty (Property property){
        this.property = property;
        propertyIdText.setText(property.getPropertyId().toString());
        propertyNameText.setText(property.getPropertyName());
        Log.i("Property Name ###########  ",property.getPropertyName());
        //if(property.getmPropertyPhoto()!=null)
            //imageView.setImageDrawable(Drawable.createFromPath(property.getmPropertyPhoto().getAbsolutePath()));

    }

    private final View.OnClickListener listItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(EXTRA_PROPERTY_ID, property.getPropertyId());
            intent.putParcelableArrayListExtra("properties", (ArrayList<? extends Parcelable>) properties);
            context.startActivity(intent);
        }
    };
}
