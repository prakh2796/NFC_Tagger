package vibhor.prakhar.example.com.nfc_tagger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prakhar Gupta on 03/11/2016.
 */

public class Settings extends AppCompatActivity {

    ListView listView;
    List<SettingsRowItem> settingsRowItemList;
    SettingsListdAdapter settingsListdAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        listView = (ListView) findViewById(R.id.settings_listview);
        settingsRowItemList = new ArrayList<SettingsRowItem>();
        settingsListdAdapter = new SettingsListdAdapter(this, settingsRowItemList);
        listView.setAdapter(settingsListdAdapter);

        for(int i = 1; i <= 5; i++){
            SettingsRowItem settingsRowItem = new SettingsRowItem();
            settingsRowItem.setSettings_menu("Setting " + i);
            settingsRowItemList.add(settingsRowItem);
        }
        settingsListdAdapter.notifyDataSetChanged();
    }

}
