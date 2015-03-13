package th.in.pureapp.mumu;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by Pakkapon on 12/3/2558.
 */
public class HtmlTextView extends TextView {
    public HtmlTextView(Context context) {
        super(context);
    }
    private void init(){
        setText(Html.fromHtml(getText().toString()));
    }
}
