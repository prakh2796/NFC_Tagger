package vibhor.prakhar.example.com.nfc_tagger.Service;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import vibhor.prakhar.example.com.nfc_tagger.R;

/**
 * Created by Prakhar Gupta on 27/11/2016.
 */

public class WriteWalletDialog extends Dialog implements View.OnClickListener {

    public Context context;
    public Activity activity;
    public Button cancel,ok;
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
        cancel = (Button) findViewById(R.id.cancel_button);
        ok = (Button) findViewById(R.id.ok_button);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_button:
                // write to wallet table
                break;
            case R.id.cancel_button:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
