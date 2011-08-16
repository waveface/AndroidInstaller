package com.waveface.installer.storage;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class InstallerEntity extends Entity implements Parcelable {
	@SerializedName("name")
	public String name;
	
	@SerializedName("version")
	public String version;
	
	@SerializedName("createdDate")
	public String createdDate;
	
	@SerializedName("image")
	public String image;
	
	@SerializedName("apk")
	public String apk;
	
	public static final Parcelable.Creator<InstallerEntity> CREATOR = new Creator<InstallerEntity>() {
		
		@Override
		public InstallerEntity[] newArray(int size) {
			return new InstallerEntity[size];
		}
		
		@Override
		public InstallerEntity createFromParcel(Parcel source) {
			return new InstallerEntity(source);
		}
	};
	/**
	 * default constructor
	 */
	public InstallerEntity(){
		name = null;
		version = null;
		createdDate = null;
		image = null;
		apk = null;
	}
	
	public InstallerEntity(Parcel in) {
		name = in.readString();
		version = in.readString();
		createdDate = in.readString();
		image = in.readString();
		apk = in.readString();
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(version);
		dest.writeString(createdDate);
		dest.writeString(image);
		dest.writeString(apk);
	}
}
