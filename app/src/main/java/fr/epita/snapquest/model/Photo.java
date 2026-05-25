package fr.epita.snapquest.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
    public int id;
    public int questId;
    public String filePath;
    public long timestamp;
    public double latitude;
    public double longitude;

    public Photo() {}

    public Photo(int questId, String filePath, long timestamp) {
        this.questId = questId;
        this.filePath = filePath;
        this.timestamp = timestamp;
    }

    protected Photo(Parcel in) {
        id = in.readInt();
        questId = in.readInt();
        filePath = in.readString();
        timestamp = in.readLong();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(questId);
        dest.writeString(filePath);
        dest.writeLong(timestamp);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
