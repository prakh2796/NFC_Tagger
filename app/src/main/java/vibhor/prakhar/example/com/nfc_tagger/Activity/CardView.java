package vibhor.prakhar.example.com.nfc_tagger.Activity;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

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
    private Button cancelButton,writeButton;
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
    private WriteWalletDialog writeWalletDialog;
    public Button cancel,ok;
    private String cardName;
    private EditText wallet_name,wallet_key;

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
}
