package vibhor.prakhar.example.com.nfc_tagger.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
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
                                ActivityCompat.requestPermissions(Settings.this, permission, 200);
                            }
                        }else {
                            createDBbackup();
                        }
                        break;

                    case 2:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkCallingOrSelfPermission(permission[0]) == PackageManager.PERMISSION_GRANTED
                                    && checkCallingOrSelfPermission(permission[1]) == PackageManager.PERMISSION_GRANTED) {
                                openFileExplorer();
                            } else {
                                ActivityCompat.requestPermissions(Settings.this, permission, 201);
                            }
                        }else {
                            openFileExplorer();
                        }
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
            Toast.makeText(Settings.this,"Backup created at: " + result,Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }else {
            Toast.makeText(Settings.this,"Could not create backup :\\",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    void openFileExplorer(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("application/x-sqlite3");
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select the Backup File to Restore"),201);
    }

    void restoreDBbackup(String path){
        progressDialog = new ProgressDialog(Settings.this);
        progressDialog.setTitle("Restoring Backup");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        boolean res = db.restoreBackup(path);
        if (!res){
            Toast.makeText(Settings.this,"Could not restore from backup :\\",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(Settings.this,"Backup Restored!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.this, MainActivity.class);
            startActivity(intent);
            setResult(2, null);
            finish();
        }

        progressDialog.dismiss();

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
            case 201:
                boolean rd = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean wr = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (rd && wr) {
                    openFileExplorer();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==201 && resultCode==RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            String filename = cursor.getString(nameIndex);
//            String tempPath = uri.getPath();
//            Log.e("file path", tempPath);
//            tempPath = uri.getEncodedPath();
//            Log.e("enc path", tempPath);

            String tempPath = "";
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];
            if ("primary".equalsIgnoreCase(type)) {
                tempPath = Environment.getExternalStorageDirectory() + "/" + split[1];
            }
            Log.e("doc path", tempPath);


//            String[] proj = { MediaStore.Images.Media.DATA };
//            cursor = getContentResolver().query(uri,  proj, null, null, null);
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            tempPath = cursor.getString(column_index);

            if (cursor != null) {
                cursor.close();
            }

//            tempPath = "";
//            List<String> tem = uri.getPathSegments();
//            for (int i=0; i<tem.size(); i++){
//                tempPath = tempPath + "/" + tem.get(i);
//            }
//            Log.e("file path seg", tempPath);

            final String path = tempPath;
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Are you sure you want to restore backup using \"" + filename + "\"?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    restoreDBbackup(path);
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();

//            String type = null;
//            String extension = MimeTypeMap.getFileExtensionFromUrl(selectedfile.getPath());
//            Log.e("check path", uri.getPath());
//            File file = new File(uri.getPath());
//            Log.e("check path", file.getAbsolutePath());
//            if (extension != null) {
//                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//            }
//            Toast.makeText(Settings.this,"type: " + type ,Toast.LENGTH_LONG).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }

}
