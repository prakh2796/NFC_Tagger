package vibhor.prakhar.example.com.nfc_tagger.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vibhor.prakhar.example.com.nfc_tagger.Adapter.AddorRemoveAdapter;
import vibhor.prakhar.example.com.nfc_tagger.Interface.ClickListener;
import vibhor.prakhar.example.com.nfc_tagger.Model.Card;
import vibhor.prakhar.example.com.nfc_tagger.Model.MyCardsItem;
import vibhor.prakhar.example.com.nfc_tagger.Model.Wallet;
import vibhor.prakhar.example.com.nfc_tagger.R;
import vibhor.prakhar.example.com.nfc_tagger.Service.DatabaseHelper;
import vibhor.prakhar.example.com.nfc_tagger.Service.RecyclerTouchListener;
import vibhor.prakhar.example.com.nfc_tagger.Service.WriteCardDialog;
import vibhor.prakhar.example.com.nfc_tagger.Service.WriteWalletDialog;

/**
 * Created by Prakhar Gupta on 12/11/2016.
 */

public class AddOrRemoveWallet extends AppCompatActivity {

    private FloatingActionButton floatingActionButtown;
    private Button cancelButton,writeButton;
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
    private EditText wallet_name,wallet_key;
    private String displayText;
    private MyCardsItem myCardsItem;
    private NfcAdapter nfcAdapter;

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
        tagName = (EditText) findViewById(R.id.tag_name);


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            Toast.makeText(this,"NFC Available!",Toast.LENGTH_SHORT).show();
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

                if (tagName.getText() != null) {
                    card = new Card(tagName.getText().toString());
                    card_id = db.createCard(card);
                    card.setId(card_id);

                    for (int i = 0; i < myCardsArrayList.size(); i++) {
                        wallet = new Wallet(myCardsArrayList.get(i).getTitle(), myCardsArrayList.get(i).getContent());
                        wallet_id = db.createWallet(card_id, wallet);
                        Log.e("check", String.valueOf(wallet_id));
                        wallet.setId(wallet_id);
                        walletList.add(wallet);
                    }

                    myCardsArrayList = new ArrayList<>();
                    writeCardDialog = new WriteCardDialog(AddOrRemoveWallet.this);
                    writeCardDialog.show();
                    enableForegroundDispath();

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

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        writeWalletDialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myCardsItem = new MyCardsItem(wallet_name.getText().toString(), wallet_key.getText().toString());
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
            writeCardDialog.dismiss();
            finish();

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
                closeActivity();
            }

        }catch (Exception e){
            Log.e("writeNdefMessage", e.getMessage());
        }

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
        for (int i = 0; i < walletList.size(); i++){
            Uri uri = Uri.parse(walletList.get(i).getWallet_key().toString());
            ndefRecord1 = createTextRecord(walletList.get(i).getWallet_name().toString());
            ndefRecord2 = NdefRecord.createUri(uri);
            ndefList.add(ndefRecord1);
            ndefList.add(ndefRecord2);
        }
        NdefRecord[] ndefRecordsArray = ndefList.toArray(new NdefRecord[ndefList.size()]);
//        Log.e("ndefArray",ndefRecordsArray.length + "");
        NdefMessage ndefMessage = new NdefMessage(ndefRecordsArray);
//        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
        return ndefMessage;
    }

    private void closeActivity(){
        disableForegroundDispath();
        writeCardDialog.dismiss();
        finish();
    }
}
