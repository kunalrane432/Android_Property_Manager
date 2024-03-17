package com.property.manager.fragments;

import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;


import com.property.manager.R;
import com.property.manager.model.Property;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import androidx.core.content.ContextCompat;

public class PropertyDetailsFragment extends Fragment {

    private Property property;

    private TextView propertyIdText;

    private TextView propertyNameText;

    private TextView propertyDescriptionText;

    private TextView propertyOwnerText;

    private TextView propertyOwnerEmailText;

    private static final int PICK_CONTACT_REQUEST = 1;
    private static final int PERMISSION_REQUEST_READ_CONTACTS = 2;

    private Button availabilityDateBtn;
    private Button editPropertyBtn;

    public static final String ARG_PROPERTY_ID = "arg_property_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_EDIT = "DialogEdit";

    public static final String REQUEST_DATE = "0";
    public static final String REQUEST_EDIT = "1";

    public static final int REQUEST_CONTACT = 1;

    public static final int PICKIMAGE = 99;

    private ActivityResultLauncher<Intent> launcher;
    private Button mOwnerButton;

    private Button mEmailButton;

    private ImageView propertyImage;

    private ImageButton propertyImageButton;

    //List<Property> properties;

    public  PropertyDetailsFragment newInstance(UUID propertyId, List<Property> properties){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROPERTY_ID, propertyId);
        args.putParcelableArrayList("properties", (ArrayList<? extends Parcelable>) properties);
        System.out.println("property id is **********"+propertyId);
        //this.properties=properties;
        //System.out.println("properties in constructor loaded"+this.properties.get(0).getPropertyName());
        PropertyDetailsFragment fragment = new PropertyDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID propertyId = (UUID)getArguments().getSerializable(ARG_PROPERTY_ID);
        List<Property> properties= getArguments().getParcelableArrayList("properties");
        //System.out.println("propertyid from Main ++++++++"+propertyId);
        System.out.println("properties inside oncreate ***********"+properties.get(0).getPropertyId());
        //TODO:Update the Property Lab with initial dummy data
        for(Property p:properties)
        {
            if(p.getPropertyId().equals(propertyId))
            {
                property = p;
            }
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACTS);
            System.out.println("Permission Granted");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_property, container, false);
        propertyIdText = (TextView) v.findViewById(R.id.property_id);
        propertyIdText.setText(property.getPropertyId().toString());
        propertyNameText = (TextView) v.findViewById(R.id.property_name);
        propertyNameText.setText(property.getPropertyName());
        propertyDescriptionText = (TextView) v.findViewById(R.id.property_description);
        propertyDescriptionText.setText(property.getPropertyDescription());
        availabilityDateBtn = (Button)v.findViewById(R.id.availability_date);
        availabilityDateBtn.setText(property.getAvailabilityDate().toString());
        availabilityDateBtn.setOnClickListener(buttonClick);
        editPropertyBtn = (Button)v.findViewById(R.id.edit_property);
        editPropertyBtn.setOnClickListener(buttonClick);
        propertyOwnerText = (TextView) v.findViewById(R.id.property_owner_name);
        propertyOwnerText.setText(property.getOwner());
        propertyOwnerEmailText = (TextView) v.findViewById(R.id.property_owner_email);
        propertyOwnerEmailText.setText(property.getOwnerEMail());
        mOwnerButton = (Button)v.findViewById(R.id.property_owner);
        mOwnerButton.setOnClickListener(ownerButtonClick);

        mEmailButton=(Button)v.findViewById(R.id.property_email);
        mEmailButton.setOnClickListener(emailButtonClick);


        propertyImage=(ImageView)v.findViewById(R.id.property_photo);

        propertyImageButton=(ImageButton)v.findViewById(R.id.property_camera);

        propertyImageButton.setOnClickListener(photoButton);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    System.out.println(result.getResultCode() + " Result Code");
                    if (result.getResultCode() == -1) {
                        Intent data = result.getData();
                        Uri contactUri = data.getData();
                        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                        Cursor cursor = getActivity().getContentResolver().query(contactUri, projection, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            cursor.close();

                            propertyOwnerText.setText(contactName);
                            property.setOwner(contactName);
                            mOwnerButton.setText(contactName);

                            // Retrieve email address
                            Cursor emailCursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                    new String[]{contactUri.getLastPathSegment()},
                                    null);

                            if (emailCursor != null && emailCursor.moveToFirst()) {
                                String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                                emailCursor.close();
                                propertyOwnerEmailText.setText(email);
                                property.setOwnerEMail(email);
                                // Do something with the email address

                            } else {
                                // No email found for the selected contact

                            }
                        } else {
                            // No contact name found for the selected contact

                        }





                    }
                }
        );
        updateDate();
        return v;
    }

    private void updateDate() {
        availabilityDateBtn.setText(property.getAvailabilityDate().toString());
    }

    public void updatePropertyData(){
        propertyNameText.setText(property.getPropertyName());
        propertyDescriptionText.setText(property.getPropertyDescription());
        propertyOwnerText.setText(property.getOwner());
        propertyOwnerEmailText.setText(property.getOwnerEMail());
    }


    private final View.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager manager = requireActivity()
                    .getSupportFragmentManager();
            if (v.getId() == R.id.availability_date) {
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(property.getAvailabilityDate());
                manager.setFragmentResultListener(REQUEST_DATE, PropertyDetailsFragment.this, fragmentResultListener);
                dialog.show(manager, DIALOG_DATE);
            }else if (v.getId() == R.id.edit_property){
                List<Property> properties= getArguments().getParcelableArrayList("properties");
                PropertyEditFragment dialog = PropertyEditFragment.newInstance(property.getPropertyId(), properties);
                manager.setFragmentResultListener(REQUEST_EDIT, PropertyDetailsFragment.this, fragmentResultListener);
                dialog.show(manager, DIALOG_EDIT);
            }
        }
    };

    private final FragmentResultListener fragmentResultListener = new FragmentResultListener() {
        @Override
        public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
            if (REQUEST_DATE.equalsIgnoreCase(requestKey)){
                Date date = (Date) result
                        .getSerializable(DatePickerFragment.EXTRA_DATE);
                property.setAvailabilityDate(date);
                updateDate();
            }else if (REQUEST_EDIT.equalsIgnoreCase(requestKey)){
                Property prop=result.getParcelable(PropertyEditFragment.ARG_PROPERTY);
                //String propertyName = result.getString(PropertyEditFragment.ARG_PROPERTY_NAME);
                //String propertyDescription = result.getString(PropertyEditFragment.ARG_PROPERTY_DESCRIPTION);
                property.setPropertyName(prop.getPropertyName());
                property.setPropertyDescription(prop.getPropertyDescription());
                property.setOwner(prop.getOwner());
                property.setOwnerEMail(prop.getOwnerEMail());
                updatePropertyData();
            }
        }
    };

    private void pickContact() {
        // Launch intent to pick contact
        Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        launcher.launch(pickContact);
    }

    private final View.OnClickListener ownerButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            System.out.println("UPDating Owner Details");
            pickContact();

        }
    };


    private final View.OnClickListener photoButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);

            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent,PICKIMAGE);

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICKIMAGE)
        {
            if(data!=null)
            {
                Uri uri=data.getData();
                propertyImage.setImageURI(uri);

            }
        }
    }

    private final View.OnClickListener emailButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //String to=editTextTo.getText().toString();
            //String subject=editTextSubject.getText().toString();
            //String message=editTextMessage.getText().toString();


            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{ property.getOwnerEMail()});
            email.putExtra(Intent.EXTRA_SUBJECT, "Email To Property Owner");
            email.putExtra(Intent.EXTRA_TEXT, "Hello " + property.getOwner() + ", <br>This is a test email");

            //need this to prompts email client only
            email.setType("message/rfc822");

            startActivity(Intent.createChooser(email, "Choose an Email client :"));

        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the contact pick
                //pickContact();
            } else {
                // Permission denied, handle accordingly (e.g., display a message or request again)
                //Toast.makeText(this, "Permission denied to read contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
