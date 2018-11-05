package bupt.astroleander.sodraggable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <h1> GParcelable </h1>
 * <p>Created by Astroleander on 2018/11/5<p>.
 *
 * <p>
 *
 * @author Astroleander
 * @version 1.0.0
 * @description using parcel to deliver message</p>
 */
public class GParcelable implements Parcelable {
    private int g;

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }


    public GParcelable(int g) {
        this.g = g;
    }

    protected GParcelable(Parcel in) {
        // MUST KEEP SEQUENCE with writeToParcel if has muiltiple params here
        this.g = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.g);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GParcelable> CREATOR = new Creator<GParcelable>() {
        @Override
        public GParcelable createFromParcel(Parcel in) {
            return new GParcelable(in);
        }

        @Override
        public GParcelable[] newArray(int size) {
            return new GParcelable[size];
        }
    };
}
