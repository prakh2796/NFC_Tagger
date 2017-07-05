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

public class CardView extends AppCompatActivity {

    private FloatingActionButton floatingActionButtown;
    private Button cancelButton,writeButton,writeCardButton;
    private DatabaseHelper db;
    private long card_id;
    private Card card;
    private EditText tagName;
    private List<MyCardsItem> myCardsArrayList = new ArrayList<>();
    private ListView listView;
    private AddorRemoveAdapter mAdapter;
    private Intent intent;
    private List<Wallet> walletList;
    private Wallet wallet;
    private long wallet_id;
    private MyCardsItem myCardsItem;
    private String displayText, wallet_type = "bitcoin";
    private WriteCardDialog writeCardDialog;
    private WriteWalletDialog writeWalletDialog;
    public Button cancel,ok,write_later;
    private ImageButton scanQRbtn;
    private String cardName;
    private EditText wallet_name,wallet_key;
    private NfcAdapter nfcAdapter;
    private Spinner spinner;
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
        intent = getIntent();
        cardName = intent.getStringExtra("name");
        getSupportActionBar().setTitle(cardName);
        card_id = intent.getLongExtra("id", -1);

        db = new DatabaseHelper(getApplicationContext());
        displayText = "Are you sure you want to delete this Wallet ?";

        listView = (ListView) findViewById(R.id.add_remove_wallet);
        floatingActionButtown = (FloatingActionButton) findViewById(R.id.add_wallet);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        writeButton = (Button) findViewById(R.id.write_button);
        writeCardButton = (Button) findViewById(R.id.write_button2);
        tagName = (EditText) findViewById(R.id.tag_name);

        tagName.setVisibility(View.GONE);
//        floatingActionButtown.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        writeButton.setVisibility(View.GONE);

        walletList = db.getWallets(card_id);
        for(int j=0;j<walletList.size();j++){
            Log.e("lololo", String.valueOf(walletList.get(j).getId()) + String.valueOf(walletList.get(j).getWallet_name()));
            myCardsItem = new MyCardsItem(
                    walletList.get(j).getWallet_type(),
                    walletList.get(j).getWallet_name(),
                    walletList.get(j).getWallet_key());
            myCardsArrayList.add(myCardsItem);
        }
        mAdapter = new AddorRemoveAdapter(myCardsArrayList, CardView.this, walletList, cardName, card_id, displayText);
        mAdapter.notifyDataSetChanged();
        listView.setAdapter(mAdapter);

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                writeWalletDialog = new WriteWalletDialog(CardView.this, card_id);
                writeWalletDialog.show();
                cancel = (Button) writeWalletDialog.findViewById(R.id.cancel_button);
                ok = (Button) writeWalletDialog.findViewById(R.id.ok_button);
                wallet_name = (EditText) writeWalletDialog.findViewById(R.id.wallet_name);
                wallet_key = (EditText) writeWalletDialog.findViewById(R.id.wallet_key);
                scanQRbtn = (ImageButton) writeWalletDialog.findViewById(R.id.scanBtn);

                spinner = (Spinner) writeWalletDialog.findViewById(R.id.spinner1);
                ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(CardView.this,
                        R.array.wallet_type, android.R.layout.simple_spinner_item);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);

                String type = walletList.get(i).getWallet_type();
                if (type.matches("bitcoin")){
                    spinner.setSelection(0);
//                    scanQRbtn.setEnabled(true);
//                    scanQRbtn.setImageResource(R.drawable.camera_50);
                }else if(type.matches("blockchain")){
                    spinner.setSelection(1);
//                    scanQRbtn.setEnabled(false);
//                    scanQRbtn.setImageResource(R.drawable.no_camera_50);
                }

                wallet_name.setText(walletList.get(i).getWallet_name());
                wallet_key.setText(walletList.get(i).getWallet_key());

//                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                        Toast.makeText(CardView.this, "Position: " + i, Toast.LENGTH_SHORT).show();
//                        if (i==0){
//                            scanQRbtn.setEnabled(true);
//                            scanQRbtn.setImageResource(R.drawable.camera_50);
//                        }else if(i==1){
//                            scanQRbtn.setEnabled(false);
//                            scanQRbtn.setImageResource(R.drawable.no_camera_50);
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
////                        Toast.makeText(CardView.this, "Position: ", Toast.LENGTH_SHORT).show();
//                    }
//                });

                scanQRbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkCallingOrSelfPermission(permission[0]) == PackageManager.PERMISSION_GRANTED) {
                                scanQRCode();
                            } else {
                                ActivityCompat.requestPermissions(CardView.this, permission, requestCode);
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
                            Toast.makeText(CardView.this, "Check Position: " + spinner.getSelectedItemPosition(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        db.updateWallet(walletList.get(i).getId(),
                                wallet_type,
                                wallet_name.getText().toString(),
                                wallet_key.getText().toString());
                        Intent intent = new Intent(CardView.this, CardView.class);
                        intent.putExtra("name", cardName);
                        intent.putExtra("id", card_id);
                        writeWalletDialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                });
                return true;
            }
        });

        floatingActionButtown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeWalletDialog = new WriteWalletDialog(CardView.this, card_id);
                writeWalletDialog.show();
                cancel = (Button) writeWalletDialog.findViewById(R.id.cancel_button);
                ok = (Button) writeWalletDialog.findViewById(R.id.ok_button);
                wallet_name = (EditText) writeWalletDialog.findViewById(R.id.wallet_name);
                wallet_key = (EditText) writeWalletDialog.findViewById(R.id.wallet_key);
                scanQRbtn = (ImageButton) writeWalletDialog.findViewById(R.id.scanBtn);

                spinner = (Spinner) writeWalletDialog.findViewById(R.id.spinner1);
                ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(CardView.this,
                        R.array.wallet_type, android.R.layout.simple_spinner_item);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);

//                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                        Toast.makeText(CardView.this, "Position: " + i, Toast.LENGTH_SHORT).show();
//                        if (i==0){
//                            scanQRbtn.setEnabled(true);
//                            scanQRbtn.setImageResource(R.drawable.camera_50);
//                        }else if(i==1){
//                            scanQRbtn.setEnabled(false);
//                            scanQRbtn.setImageResource(R.drawable.no_camera_50);
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
////                        Toast.makeText(CardView.this, "Position: ", Toast.LENGTH_SHORT).show();
//                    }
//                });

                scanQRbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkCallingOrSelfPermission(permission[0]) == PackageManager.PERMISSION_GRANTED) {
                                scanQRCode();
                            } else {
                                ActivityCompat.requestPermissions(CardView.this, permission, requestCode);
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
                            Toast.makeText(CardView.this, "Check Position: " + spinner.getSelectedItemPosition(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        wallet = new Wallet(wallet_type,wallet_name.getText().toString(),wallet_key.getText().toString());
                        wallet_id = db.createWallet(card_id, wallet);
                        Log.e("check", String.valueOf(wallet_id));
                        wallet.setId(wallet_id);

                        Intent intent = new Intent(CardView.this, CardView.class);
                        intent.putExtra("name", cardName);
                        intent.putExtra("id", card_id);
                        writeWalletDialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        writeCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                writeCardDialog = new WriteCardDialog(CardView.this);
                writeCardDialog.show();
                if (nfcAdapter != null) {
                    if(nfcAdapter.isEnabled()) {
                        enableForegroundDispath();
                    }else {
                        Toast.makeText(CardView.this, "Turn on NFC!", Toast.LENGTH_SHORT).show();
                        enableForegroundDispath();
                    }
                }else {
                    Toast.makeText(CardView.this,"NFC Not Available!",Toast.LENGTH_SHORT).show();
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
            }
        });

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
                Toast.makeText(CardView.this,"Scan Cancelled",Toast.LENGTH_SHORT).show();
            }else {
                String bitAdd = result.getContents();
                bitAdd = bitAdd.replace("bitcoin:","");
                bitAdd = bitAdd.replace("https://blockchain.info/address/","");
                wallet_key.setText(bitAdd);
                Toast.makeText(CardView.this,"すごい!",Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        intent = new Intent(CardView.this, MainActivity.class);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)){
            Toast.makeText(this,"NFC Intent",Toast.LENGTH_SHORT).show();

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            NdefMessage ndefMessage = createNdefMessage("my message test");
//            NdefMessage ndefMessage = createNdefMessage(tagName.getText().toString());

            NdefMessage ndefMessage = createNdefMessage(cardName.toString());

            writeNdefMessage(tag,ndefMessage);
            //closeActivity();
        }
    }

    private void enableForegroundDispath(){

        Log.e("NFC","foreground enable");
        intent = new Intent(this,CardView.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
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

//        NdefRecord ndefRecord1 = createTextRecord(content);
        NdefRecord ndefRecord1,ndefRecord2;
        List<NdefRecord> ndefList = new ArrayList<NdefRecord>();
//        ndefList.add(ndefRecord1);
        if(walletList.size()!=0) {
            for (int i = 0; i < walletList.size(); i++) {
                ndefRecord1 = createTextRecord(walletList.get(i).getWallet_name().toString());

                String url = walletList.get(i).getWallet_key().toString();
                String type = walletList.get(i).getWallet_type().toString();
                if (type.matches("bitcoin")){
                    url = "bitcoin:" + url;
                }else if(type.matches("blockchain")){
                    url = "https://blockchain.info/address/" + url;
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
        intent = new Intent(CardView.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
