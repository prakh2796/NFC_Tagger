package vibhor.prakhar.example.com.nfc_tagger.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vibhor.prakhar.example.com.nfc_tagger.Adapter.AddorRemoveAdapter;
import vibhor.prakhar.example.com.nfc_tagger.Model.Card;
import vibhor.prakhar.example.com.nfc_tagger.Model.MyCardsItem;
import vibhor.prakhar.example.com.nfc_tagger.Model.Wallet;
import vibhor.prakhar.example.com.nfc_tagger.R;
import vibhor.prakhar.example.com.nfc_tagger.Service.DatabaseHelper;
import vibhor.prakhar.example.com.nfc_tagger.Service.WriteCardDialog;
import vibhor.prakhar.example.com.nfc_tagger.Service.WriteWalletDialog;

/**
 * Created by Prakhar Gupta on 12/11/2016.
 */

public class AddOrRemoveWallet extends AppCompatActivity {

    private FloatingActionButton floatingActionButtown;
    private Button cancelButton,writeButton,writeCardButton;
    private DatabaseHelper db;
    private long card_id;
    private Card card;
    private EditText tagName;
    private Intent intent;
    private List<MyCardsItem> myCardsArrayList = new ArrayList<>();
    private List<Wallet> walletList = new ArrayList<>();
    private ListView listView;
    private AddorRemoveAdapter mAdapter;
    private Wallet wallet;
    private long wallet_id;
    private WriteCardDialog writeCardDialog;
    private WriteWalletDialog writeWalletDialog;
    public Button cancel,ok, write_later;
    private ImageButton scanQRbtn;
    private EditText wallet_name,wallet_key;
    private Spinner spinner;
    private String displayText, wallet_type = "bitcoin";
    private MyCardsItem myCardsItem;
    private NfcAdapter nfcAdapter;
    private IntentIntegrator qrScan;
    int requestCode = 200;
    String[] permission = {"android.permission.CAMERA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_remove_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = new DatabaseHelper(getApplicationContext());
        displayText = "Are you sure you want to delete this Card?";

        listView = (ListView) findViewById(R.id.add_remove_wallet);
        floatingActionButtown = (FloatingActionButton) findViewById(R.id.add_wallet);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        writeButton = (Button) findViewById(R.id.write_button);
        writeCardButton = (Button) findViewById(R.id.write_button2);
        tagName = (EditText) findViewById(R.id.tag_name);
        writeCardButton.setVisibility(View.GONE);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null) {
            if(nfcAdapter.isEnabled()) {
                Toast.makeText(this, "NFC Available!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "NFC is off!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this,"NFC Not Available!",Toast.LENGTH_SHORT).show();
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                myCardsArrayList = new ArrayList<>();
//                intent = new Intent(AddOrRemoveWallet.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tagName.getText().toString().trim().length() != 0) {
                    card = new Card(tagName.getText().toString());
                    card_id = db.createCard(card);
                    card.setId(card_id);

                    for (int i = 0; i < myCardsArrayList.size(); i++) {
                        wallet = new Wallet(
                                myCardsArrayList.get(i).getType(),
                                myCardsArrayList.get(i).getTitle(),
                                myCardsArrayList.get(i).getContent());
                        wallet_id = db.createWallet(card_id, wallet);
                        Log.e("check", String.valueOf(wallet_id));
                        wallet.setId(wallet_id);
                        walletList.add(wallet);
                    }

                    myCardsArrayList = new ArrayList<>();
                    writeCardDialog = new WriteCardDialog(AddOrRemoveWallet.this);
                    writeCardDialog.show();
                    if (nfcAdapter != null) {
                        if(nfcAdapter.isEnabled()) {
                            enableForegroundDispath();
                        }else {
                            Toast.makeText(AddOrRemoveWallet.this, "Turn on NFC!", Toast.LENGTH_SHORT).show();
                            enableForegroundDispath();
                        }
                    }else {
                        Toast.makeText(AddOrRemoveWallet.this,"NFC Not Available!",Toast.LENGTH_SHORT).show();
                    }


                    write_later = (Button) writeCardDialog.findViewById(R.id.write_later);
                    write_later.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

//                            disableForegroundDispath();
//                            writeCardDialog.dismiss();
//                            finish();
                            closeActivity();
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), "Card Name cannot be EMPTY!",Toast.LENGTH_LONG).show();
                }
            }
        });

        floatingActionButtown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeWalletDialog = new WriteWalletDialog(AddOrRemoveWallet.this, card_id);
                writeWalletDialog.show();
                cancel = (Button) writeWalletDialog.findViewById(R.id.cancel_button);
                ok = (Button) writeWalletDialog.findViewById(R.id.ok_button);
                wallet_name = (EditText) writeWalletDialog.findViewById(R.id.wallet_name);
                wallet_key = (EditText) writeWalletDialog.findViewById(R.id.wallet_key);
                scanQRbtn = (ImageButton) writeWalletDialog.findViewById(R.id.scanBtn);

                spinner = (Spinner) writeWalletDialog.findViewById(R.id.spinner1);
                ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(AddOrRemoveWallet.this,
                        R.array.wallet_type, android.R.layout.simple_spinner_item);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        Toast.makeText(AddOrRemoveWallet.this, "Position: " + i, Toast.LENGTH_SHORT).show();
                        if (i==0){
                            scanQRbtn.setEnabled(true);
                            scanQRbtn.setImageResource(R.drawable.camera_50);
                        }else if(i==1){
                            scanQRbtn.setEnabled(false);
                            scanQRbtn.setImageResource(R.drawable.no_camera_50);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
//                        Toast.makeText(AddOrRemoveWallet.this, "Position: ", Toast.LENGTH_SHORT).show();
                    }
                });

                scanQRbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkCallingOrSelfPermission(permission[0]) == PackageManager.PERMISSION_GRANTED) {
                                scanQRCode();
                            } else {
                                ActivityCompat.requestPermissions(AddOrRemoveWallet.this, permission, requestCode);
                            }
                        }else {
                            scanQRCode();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        writeWalletDialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (spinner.getSelectedItemPosition() == 0){
                            wallet_type = "bitcoin";
                        }else if(spinner.getSelectedItemPosition() == 1){
                            wallet_type = "blockchain";
                        }else {
                            Toast.makeText(AddOrRemoveWallet.this, "Check Position: " + spinner.getSelectedItemPosition(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        myCardsItem = new MyCardsItem(wallet_type, wallet_name.getText().toString(), wallet_key.getText().toString());
                        myCardsArrayList.add(myCardsItem);
                        Log.e("bolbol", myCardsItem.getContent());
                        mAdapter.notifyDataSetChanged();
                        writeWalletDialog.dismiss();
                    }
                });
            }
        });

        mAdapter = new AddorRemoveAdapter(myCardsArrayList, getApplicationContext(), walletList);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void scanQRCode(){
        qrScan = new IntentIntegrator(this);
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        qrScan.setPrompt("Scan Bitcoin Address");
        qrScan.setCameraId(0);
        qrScan.setBeepEnabled(true);
        qrScan.setBarcodeImageEnabled(false);
        qrScan.initiateScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200:
                boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (camera) {
                    scanQRCode();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(AddOrRemoveWallet.this,"Scan Cancelled",Toast.LENGTH_SHORT).show();
            }else {
                String bitAdd = result.getContents();
                bitAdd = bitAdd.replace("bitcoin:","");
                wallet_key.setText(bitAdd);
                Toast.makeText(AddOrRemoveWallet.this,"すごい!",Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)){
            Toast.makeText(this,"NFC Intent",Toast.LENGTH_SHORT).show();

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            NdefMessage ndefMessage = createNdefMessage("my message test");
//            NdefMessage ndefMessage = createNdefMessage(tagName.getText().toString());

            NdefMessage ndefMessage = createNdefMessage(tagName.getText().toString());

            writeNdefMessage(tag,ndefMessage);
            //closeActivity();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private void enableForegroundDispath(){

        Log.e("NFC","foreground enable");
        intent = new Intent(this,AddOrRemoveWallet.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);

    }

    private void disableForegroundDispath(){

        Log.e("NFC","foreground disable");
        nfcAdapter.disableForegroundDispatch(this);
        Log.e("NFC", "check1");

    }

    private void formatTag(Tag tag, NdefMessage ndefMessage){

        try {

            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null){
                Toast.makeText(this,"Tag is not NdefFormatable!",Toast.LENGTH_SHORT).show();
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
            Toast.makeText(this,"Message Written1",Toast.LENGTH_SHORT).show();
            disableForegroundDispath();
            closeActivity();

        }catch (Exception e){
            Log.e("formatTag", e.getMessage());
        }

    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage){

        try {

            if (tag == null){
                Toast.makeText(this,"Tag object cannot be null",Toast.LENGTH_SHORT).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if (ndef == null){
                //format tag with the ndef format and writes the message
                formatTag(tag, ndefMessage);
            }else {
                ndef.connect();

                if (!ndef.isWritable()){
                    Toast.makeText(this,"Tag is not writable!",Toast.LENGTH_SHORT).show();
                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Toast.makeText(this,"Message Written2",Toast.LENGTH_SHORT).show();
                disableForegroundDispath();
            }

        }catch (Exception e){
            Log.e("writeNdefMessage", e.getMessage());
        }

        closeActivity();

    }

    private NdefRecord createTextRecord(String content) {
        try {
            // Get UTF-8 byte
            byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
            byte[] text = content.getBytes("UTF-8"); // Content in UTF-8

            int langSize = lang.length;
            int textLength = text.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
            payload.write((byte) (langSize & 0x1F));
            payload.write(lang, 0, langSize);
            payload.write(text, 0, textLength);

            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
            return record;
        }
        catch (Exception e) {
            Log.e("createTextRecord", e.getMessage());
        }

        return null;
    }

    private NdefMessage createNdefMessage(String content){

        NdefRecord ndefRecord1 = createTextRecord(content);
        NdefRecord ndefRecord2;
        List<NdefRecord> ndefList = new ArrayList<NdefRecord>();
        ndefList.add(ndefRecord1);
        if(walletList.size()!=0) {
            for (int i = 0; i < walletList.size(); i++) {
                ndefRecord1 = createTextRecord(walletList.get(i).getWallet_name().toString());

                String url = walletList.get(i).getWallet_key().toString();
                String type = walletList.get(i).getWallet_type().toString();
                Log.e("Type",type);
                if (type.matches("bitcoin")){
//                    Log.e("URL",url);
                    url = "bitcoin:" + url;
//                    Log.e("URL",url);
                }else if(type.matches("blockchain")){
//                    Log.e("URL",url);
                    url = "http://blockchain.info/address/" + url;
//                    Log.e("URL",url);
                }else {
                    url = "*something went wrong*";
                    Log.e("URL",url);
                }
                Uri uri = Uri.parse(url);
                ndefRecord2 = NdefRecord.createUri(uri);

                ndefList.add(ndefRecord1);
                ndefList.add(ndefRecord2);
            }
        }

        NdefRecord[] ndefRecordsArray = ndefList.toArray(new NdefRecord[ndefList.size()]);
//        Log.e("ndefArray",ndefRecordsArray.length + "");
        NdefMessage ndefMessage = new NdefMessage(ndefRecordsArray);
//        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
        return ndefMessage;
    }

    private void closeActivity(){
        Log.e("NFC", "Closing Activity...");
//        disableForegroundDispath();
        writeCardDialog.dismiss();
        Log.e("NFC", "check2");
        intent = new Intent(AddOrRemoveWallet.this, MainActivity.class);
        startActivity(intent);
        setResult(2, null);
        finish();
    }
}
