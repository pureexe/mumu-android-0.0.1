package th.in.pureapp.mumu;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Vector;

/**
 * Created by Pakkapon on 7/3/2558.
 */
class ParcelableHistoryAdapter implements Parcelable {
    HistoryAdapter historyAdapter;
    ParcelableHistoryAdapter(){

    }
    ParcelableHistoryAdapter(HistoryAdapter a){
        historyAdapter = a;
    }
    public HistoryAdapter get(){
        return historyAdapter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
