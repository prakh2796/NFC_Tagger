package vibhor.prakhar.example.com.nfc_tagger.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private RecyclerView recyclerView;
    private AddorRemoveAdapter mAdapter;
    private Wallet wallet;
    private long wallet_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_remove_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = new DatabaseHelper(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.add_remove_wallet);
        floatingActionButtown = (FloatingActionButton) findViewById(R.id.add_wallet);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        writeButton = (Button) findViewById(R.id.write_button);
        tagName = (EditText) findViewById(R.id.tag_name);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                }
            }
        });

        floatingActionButtown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call nfc
            }
        });

        mAdapter = new AddorRemoveAdapter(myCardsArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MyCardsItem myCardsItem = myCardsArrayList.get(position);
                Toast.makeText(getApplicationContext(), myCardsItem.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareCardData();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }

    private void prepareCardData() {
        MyCardsItem movie = new MyCardsItem("Mad Max: Fury Road", "Action & Adventure\nAction & Adventure\nAction & Adventure");
        myCardsArrayList.add(movie);

        movie = new MyCardsItem("Mad Max: Fury Road", "Action & Adventure");
        myCardsArrayList.add(movie);

        movie = new MyCardsItem("Mad Max: Fury Road", "Action & AdventureAction & Adventure\nAction & Adventure\nAction & Adventure");
        myCardsArrayList.add(movie);

        movie = new MyCardsItem("Mad Max: Fury Road", "Action & Adventure");
        myCardsArrayList.add(movie);

        mAdapter.notifyDataSetChanged();
    }
}
