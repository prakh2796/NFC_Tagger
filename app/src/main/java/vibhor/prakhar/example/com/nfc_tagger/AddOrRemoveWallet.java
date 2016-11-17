package vibhor.prakhar.example.com.nfc_tagger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prakhar Gupta on 12/11/2016.
 */

public class AddOrRemoveWallet extends AppCompatActivity {

    private ListView listView;
    private List<SettingsRowItem> settingsRowItemList;
    private SettingsListAdapter settingsListdAdapter;
    private Button cancelButton,writeButton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_remove_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = (ListView) findViewById(R.id.wallet_list);
        settingsRowItemList = new ArrayList<SettingsRowItem>();
        settingsListdAdapter = new SettingsListAdapter(this, settingsRowItemList);
        listView.setAdapter(settingsListdAdapter);

        cancelButton = (Button) findViewById(R.id.cancel_button);
        writeButton = (Button) findViewById(R.id.write_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(AddOrRemoveWallet.this, MainActivity.class);
                startActivity(intent);
            }
        });

        for(int i = 1; i <= 5; i++){
            SettingsRowItem settingsRowItem = new SettingsRowItem();
            settingsRowItem.setSettings_menu("Setting " + i);
            settingsRowItemList.add(settingsRowItem);
        }
        settingsListdAdapter.notifyDataSetChanged();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
}
