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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private String displayText;
    private WriteCardDialog writeCardDialog;
    private WriteWalletDialog writeWalletDialog;
    public Button cancel,ok,write_later;
    private String cardName;
    private EditText wallet_name,wallet_key;
    private NfcAdapter nfcAdapter;

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
            myCardsItem = new MyCardsItem(walletList.get(j).getWallet_name(), walletList.get(j).getWallet_key());
            myCardsArrayList.add(myCardsItem);
        }
        mAdapter = new AddorRemoveAdapter(myCardsArrayList, CardView.this, walletList, cardName, card_id, displayText);
        mAdapter.notifyDataSetChanged();
        listView.setAdapter(mAdapter);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            Toast.makeText(this,"NFC Available!",Toast.LENGTH_SHORT).show();
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
                wallet_name.setText(walletList.get(i).getWallet_name());
                wallet_key.setText(walletList.get(i).getWallet_key());

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        writeWalletDialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.updateWallet(walletList.get(i).getId(), wallet_name.getText().toString(), wallet_key.getText().toString());
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

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        writeWalletDialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        wallet = new Wallet(wallet_name.getText().toString(),wallet_key.getText().toString());
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
            }
        });

        mAdapter.notifyDataSetChanged();
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
        Log.e("NFC", "Closing Activity...");
//        disableForegroundDispath();
        writeCardDialog.dismiss();
        Log.e("NFC", "check2");
        intent = new Intent(CardView.this, MainActivity.class);
        startActivity(intent);
    }
}
