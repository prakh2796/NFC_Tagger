package vibhor.prakhar.example.com.nfc_tagger.Service;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import vibhor.prakhar.example.com.nfc_tagger.R;

/**
 * Created by Prakhar Gupta on 27/11/2016.
 */

public class WriteWalletDialog extends Dialog {

    public Context context;
    public Activity activity;
    private long card_id;

    public WriteWalletDialog(Context context, long card_id) {
        super(context);
        this.context = context;
        this.card_id = card_id;
        activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.write_wallet_dialog);

    }
}
