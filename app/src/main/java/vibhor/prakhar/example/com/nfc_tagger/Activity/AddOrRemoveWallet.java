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
    public Button cancel,ok;
    private EditText wallet_name,wallet_key;
    private String displayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_remove_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = new DatabaseHelper(getApplicationContext());
        displayText = "Are you sure you want to delete this Card ?";

        listView = (ListView) findViewById(R.id.add_remove_wallet);
        floatingActionButtown = (FloatingActionButton) findViewById(R.id.add_wallet);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        writeButton = (Button) findViewById(R.id.write_button);
        tagName = (EditText) findViewById(R.id.tag_name);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCardsArrayList = new ArrayList<>();
                intent = new Intent(AddOrRemoveWallet.this, MainActivity.class);
                startActivity(intent);
            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card = new Card(tagName.getText().toString());
                card_id = db.createCard(card);
                card.setId(card_id);

                for(int i=0;i<myCardsArrayList.size();i++){
                    wallet = new Wallet(myCardsArrayList.get(i).getTitle(),myCardsArrayList.get(i).getContent());
                    wallet_id = db.createWallet(card_id, wallet);
                    Log.e("check", String.valueOf(wallet_id));
                    wallet.setId(wallet_id);
                    walletList.add(wallet);
                }

                myCardsArrayList = new ArrayList<>();
                writeCardDialog = new WriteCardDialog(AddOrRemoveWallet.this);
                writeCardDialog.show();
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
                        MyCardsItem myCardsItem = new MyCardsItem(wallet_name.getText().toString(), wallet_key.getText().toString());
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

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
}
