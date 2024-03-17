package com.property.manager.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class Property implements Parcelable {

    private UUID propertyId;
    private String propertyName;
    private String propertyDescription;
    private Date availabilityDate;

    private String owner;

    private String ownerEMail;

    private String ownerNumber;

    private String ownerDetails;


  private File mPropertyPhoto;

    protected Property(Parcel in) {
        propertyId=UUID.fromString(in.readString());
        propertyName = in.readString();
        propertyDescription = in.readString();
        ownerDetails = in.readString();
        availabilityDate=new Date(in.readLong());
        owner=in.readString();
        ownerNumber=in.readString();
        ownerEMail=in.readString();

    }

    public File getmPropertyPhoto() {
        return mPropertyPhoto;
    }

    public void setmPropertyPhoto(File mPropertyPhoto) {
        this.mPropertyPhoto = mPropertyPhoto;
    }

    public Property(UUID propertyId, String propertyName, String propertyDescription, Date availabilityDate, String ownerDetails,String owner,String ownerNumber,String ownerEMail) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.propertyDescription = propertyDescription;
        this.availabilityDate = availabilityDate;
        this.ownerDetails = ownerDetails;
        this.owner=owner;
        this.ownerNumber=ownerNumber;
        this.ownerEMail=ownerEMail;
    }

    public static final Creator<Property> CREATOR = new Creator<Property>() {
        @Override
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        @Override
        public Property[] newArray(int size) {
            return new Property[size];
        }
    };

    public String getOwnerDetails() {
        return ownerDetails;
    }

    public void setOwnerDetails(String ownerDetails) {
        this.ownerDetails = ownerDetails;
    }

    public Property() {
        propertyId = UUID.randomUUID();
        availabilityDate = new Date();
    }

    public UUID getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(UUID propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public Date getAvailabilityDate() {
        return availabilityDate;
    }

    public void setAvailabilityDate(Date availabilityDate) {
        this.availabilityDate = availabilityDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerEMail() {
        return ownerEMail;
    }

    public void setOwnerEMail(String ownerEMail) {
        this.ownerEMail = ownerEMail;
    }

    public String getOwnerNumber() {
        return ownerNumber;
    }

    public void setOwnerNumber(String ownerNumber) {
        this.ownerNumber = ownerNumber;
    }


    public String getPhotoFilename(){
        return "IMG_" + getPropertyId().toString() + ".jpg";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(propertyId.toString());
        dest.writeString(propertyName);
        dest.writeString(propertyDescription);
        dest.writeString(ownerDetails);
        dest.writeLong(availabilityDate.getTime());
        dest.writeString(owner);
        dest.writeString(ownerEMail);
        dest.writeString(ownerNumber);

        //dest.write
    }
}
