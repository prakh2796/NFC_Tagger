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
    private RecyclerView recyclerView;
    private ListView listView;
    private AddorRemoveAdapter mAdapter;
    private Intent intent;
    private List<Wallet> walletList;
    private MyCardsItem myCardsItem;
    private WriteWalletDialog writeWalletDialog;
    public Button cancel,ok;
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

        getSupportActionBar().setTitle(intent.getStringExtra("name"));
        card_id = intent.getLongExtra("id", -1);

        db = new DatabaseHelper(getApplicationContext());

//        recyclerView = (RecyclerView) findViewById(R.id.add_remove_wallet);
        listView = (ListView) findViewById(R.id.add_remove_wallet);
        floatingActionButtown = (FloatingActionButton) findViewById(R.id.add_wallet);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        writeButton = (Button) findViewById(R.id.write_button);
        tagName = (EditText) findViewById(R.id.tag_name);

        tagName.setVisibility(View.GONE);
        floatingActionButtown.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        writeButton.setVisibility(View.GONE);

//        mAdapter = new AddorRemoveAdapter(myCardsArrayList, getApplicationContext(), walletList);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
//        listView.setAdapter(mAdapter);

        walletList = db.getWallets(card_id);
        for(int j=0;j<walletList.size();j++){
            Log.e("lololo", String.valueOf(walletList.get(j).getId()) + String.valueOf(walletList.get(j).getWallet_name()));
            myCardsItem = new MyCardsItem(walletList.get(j).getWallet_name(), walletList.get(j).getWallet_key());
            myCardsArrayList.add(myCardsItem);
        }
        mAdapter = new AddorRemoveAdapter(myCardsArrayList, CardView.this, walletList, intent.getStringExtra("name"), card_id);
        mAdapter.notifyDataSetChanged();
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("hell", String.valueOf(view.getId()));
            }
        });

        /*
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MyCardsItem myCardsItem = myCardsArrayList.get(position);
                Toast.makeText(getApplicationContext(), myCardsItem.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                writeWalletDialog = new WriteWalletDialog(CardView.this, card_id);
                writeWalletDialog.show();
                cancel = (Button) writeWalletDialog.findViewById(R.id.cancel_button);
                ok = (Button) writeWalletDialog.findViewById(R.id.ok_button);
                wallet_name = (EditText) writeWalletDialog.findViewById(R.id.wallet_name);
                wallet_key = (EditText) writeWalletDialog.findViewById(R.id.wallet_key);

                wallet_name.setText(walletList.get(position).getWallet_name());
                wallet_key.setText(walletList.get(position).getWallet_key());

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        writeWalletDialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        }));*/

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
