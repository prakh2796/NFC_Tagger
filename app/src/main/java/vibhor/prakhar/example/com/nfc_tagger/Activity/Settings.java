package vibhor.prakhar.example.com.nfc_tagger.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import vibhor.prakhar.example.com.nfc_tagger.Activity.MainActivity;
import vibhor.prakhar.example.com.nfc_tagger.Adapter.SettingsListAdapter;
import vibhor.prakhar.example.com.nfc_tagger.Model.SettingsRowItem;
import vibhor.prakhar.example.com.nfc_tagger.R;

/**
 * Created by Prakhar Gupta on 03/11/2016.
 */

public class Settings extends AppCompatActivity {

    private ListView listView;
    private List<SettingsRowItem> settingsRowItemList;
    private SettingsListAdapter settingsListdAdapter;
    private String[] settings = {"General", "Backup & Restore", "Settings Item", "Logins","About the app"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = (ListView) findViewById(R.id.settings_listview);
        settingsRowItemList = new ArrayList<SettingsRowItem>();
        settingsListdAdapter = new SettingsListAdapter(this, settingsRowItemList);
        listView.setAdapter(settingsListdAdapter);

        for(int i = 0; i < 5; i++){
            SettingsRowItem settingsRowItem = new SettingsRowItem();
            settingsRowItem.setSettings_menu(settings[i]);
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
