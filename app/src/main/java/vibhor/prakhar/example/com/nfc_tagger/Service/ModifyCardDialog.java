package vibhor.prakhar.example.com.nfc_tagger.Service;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import vibhor.prakhar.example.com.nfc_tagger.R;

/**
 * Created by Prakhar Gupta on 01/01/2017.
 */

public class ModifyCardDialog extends Dialog {

    public Context context;

    public ModifyCardDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.delete_card_dialog);
    }
}
