package com.property.manager.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.property.manager.R;
import com.property.manager.model.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PropertyEditFragment extends DialogFragment {

    public static final String ARG_PROPERTY_ID = "property_id";

    public static final String ARG_PROPERTY_NAME = "property_name";

    public static final String ARG_PROPERTY = "property";

    public static final String ARG_PROPERTY_DESCRIPTION = "property_description";

    //public static final String EXTRA_EDIT = "property_edit_dialog_extra";

    private EditText propertyNameText;

    private EditText propertyDescriptionText;

    private EditText propertyOwnerText;
    private EditText propertyOwnerEmailText;

    public PropertyEditFragment() {
        // Required empty public constructor
    }

    public static PropertyEditFragment newInstance(UUID propertyId, List<Property> properties) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROPERTY_ID, propertyId);
        args.putParcelableArrayList("properties", (ArrayList<? extends Parcelable>) properties);
        PropertyEditFragment fragment = new PropertyEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        UUID propertyId = (UUID)getArguments().getSerializable(ARG_PROPERTY_ID);

        //TODO:Update the property based on new property lab logic
        //Property property = PropertyLab.get().getProperty(propertyId);
        List<Property> properties= getArguments().getParcelableArrayList("properties");
        Property property =null;

        for(Property p:properties)
        {
            if(p.getPropertyId().equals(propertyId))
            {
                property = p;
            }
        }
        View v = getLayoutInflater().inflate(R.layout.fragment_property_edit, null);
        propertyNameText = (EditText) v.findViewById(R.id.property_name);
        propertyDescriptionText = (EditText) v.findViewById(R.id.property_description);

        //TODO: Update the values once completed deletion of property Lab
        assert property != null;
        propertyNameText.setText(property.getPropertyName());
        propertyDescriptionText.setText(property.getPropertyDescription());

        propertyOwnerText = (EditText) v.findViewById(R.id.property_owner_name);
        propertyOwnerEmailText = (EditText) v.findViewById(R.id.property_owner_email);

        propertyOwnerText.setText(property.getOwner());
        propertyOwnerEmailText.setText(property.getOwnerEMail());

        return new AlertDialog.Builder(requireActivity())
                .setView(v)
                .setTitle(R.string.edit_property)
                .setPositiveButton(android.R.string.ok, null)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String propertyName = propertyNameText.getText().toString();
                                String propertyDescription = propertyDescriptionText.getText().toString();
                                String propertyOwner = propertyOwnerText.getText().toString();
                                String propertyOwnerEmail = propertyOwnerEmailText.getText().toString();
                                Property p= new Property();

                                p.setPropertyName(propertyName);
                                p.setPropertyDescription(propertyDescription);
                                p.setOwner(propertyOwner);
                                p.setOwnerEMail(propertyOwnerEmail);

                                sendResult(PropertyDetailsFragment.REQUEST_EDIT, p);
                            }
                        }).create();

    }

    private void sendResult(String resultCode, Property property) {
        Bundle bundle = new Bundle();
        //bundle.put
        bundle.putParcelable(ARG_PROPERTY, property);
        //bundle.putString(ARG_PROPERTY_DESCRIPTION, propertyDescription);
        getParentFragmentManager().setFragmentResult(resultCode, bundle);
    }
}
