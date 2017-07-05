package vibhor.prakhar.example.com.nfc_tagger.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vibhor.prakhar.example.com.nfc_tagger.Activity.MainActivity;
import vibhor.prakhar.example.com.nfc_tagger.Adapter.SettingsListAdapter;
import vibhor.prakhar.example.com.nfc_tagger.Model.SettingsRowItem;
import vibhor.prakhar.example.com.nfc_tagger.R;
import vibhor.prakhar.example.com.nfc_tagger.Service.DatabaseHelper;

/**
 * Created by Prakhar Gupta on 03/11/2016.
 */

public class Settings extends AppCompatActivity {

    private ListView listView;
    private List<SettingsRowItem> settingsRowItemList;
    private SettingsListAdapter settingsListdAdapter;
    private String[] settings = {"General", "Create Backup", "Restore Backup", "Logins","About the app"};
    String[] permission = {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"};
    int requestCode = 201;
    DatabaseHelper db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = new DatabaseHelper(getApplicationContext());

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkCallingOrSelfPermission(permission[0]) == PackageManager.PERMISSION_GRANTED
                                    && checkCallingOrSelfPermission(permission[1]) == PackageManager.PERMISSION_GRANTED) {
                                createDBbackup();
                            } else {
                                ActivityCompat.requestPermissions(Settings.this, permission, requestCode);
                            }
                        }else {
                            createDBbackup();
                        }
                        break;
                    case 2:
                        progressDialog.setTitle("Restoring Backup");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCancelable(true);
                        progressDialog.show();
                        break;
                }
            }
        });
    }

    void createDBbackup(){
        progressDialog = new ProgressDialog(Settings.this);
        progressDialog.setTitle("Creating Backup");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        String result = db.createBackup();
        if (result.trim().length() != 0){
            Toast.makeText(Settings.this,"database created at: " + result,Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }else {
            Toast.makeText(Settings.this,"Could not create backup :\\",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200:
                boolean read = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean write = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (read && write) {
                    createDBbackup();
                }
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }

}
