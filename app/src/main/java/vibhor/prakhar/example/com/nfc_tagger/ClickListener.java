package vibhor.prakhar.example.com.nfc_tagger;

import android.view.View;

/**
 * Created by Prakhar Gupta on 23/11/2016.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
