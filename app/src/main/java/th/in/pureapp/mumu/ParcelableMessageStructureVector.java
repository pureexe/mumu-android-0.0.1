package th.in.pureapp.mumu;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Vector;

/**
 * Created by Pakkapon on 7/3/2558.
 */
class ParcelableMessageStructureVector implements Parcelable {
    Vector<MessageStructure> vex = new Vector<MessageStructure>();
    ParcelableMessageStructureVector(){
        vex = new Vector<MessageStructure>();
    }
    ParcelableMessageStructureVector(Vector<MessageStructure> a){
        vex = a;
    }
    public Vector<MessageStructure> get(){
        return vex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
