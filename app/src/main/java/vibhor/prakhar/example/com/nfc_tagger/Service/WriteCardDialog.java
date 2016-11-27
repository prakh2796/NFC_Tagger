package vibhor.prakhar.example.com.nfc_tagger.Service;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.List;

import vibhor.prakhar.example.com.nfc_tagger.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Prakhar Gupta on 27/11/2016.
 */

public class WriteCardDialog extends Dialog implements View.OnClickListener {

    public Context context;
    public Activity activity;
    public Button writeLater;

    public WriteCardDialog(Context context) {
        super(context);
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.write_card_dialog);
        writeLater = (Button) findViewById(R.id.write_later);
        writeLater.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        activity.setResult(RESULT_OK,intent);
        activity.finish();
        dismiss();
    }
}
