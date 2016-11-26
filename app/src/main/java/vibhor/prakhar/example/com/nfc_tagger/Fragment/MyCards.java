package vibhor.prakhar.example.com.nfc_tagger.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vibhor.prakhar.example.com.nfc_tagger.Adapter.MyCardsAdapter;
import vibhor.prakhar.example.com.nfc_tagger.Interface.ClickListener;
import vibhor.prakhar.example.com.nfc_tagger.Model.Card;
import vibhor.prakhar.example.com.nfc_tagger.Model.MyCardsItem;
import vibhor.prakhar.example.com.nfc_tagger.Model.Wallet;
import vibhor.prakhar.example.com.nfc_tagger.R;
import vibhor.prakhar.example.com.nfc_tagger.Service.DatabaseHelper;
import vibhor.prakhar.example.com.nfc_tagger.Service.RecyclerTouchListener;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Prakhar Gupta on 17/11/2016.
 */

public class MyCards extends Fragment {
    private DatabaseHelper db;
    private View view;
    private List<MyCardsItem> myCardsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyCardsAdapter mAdapter;
    private MyCardsItem movie;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mycards, container, false);

        db = new DatabaseHelper(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.mycard);

        mAdapter = new MyCardsAdapter(myCardsArrayList);
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

        return view;
    }

    private void prepareCardData() {

        String content = "\0";

//        MyCardsItem movie = new MyCardsItem("Mad Max: Fury Road", "Action & Adventure\nAction & Adventure\nAction & Adventure");
//        myCardsArrayList.add(movie);

//        Card card = new Card("Card 1");
//        long card_id = db.createCard(card);
//        card.setId(card_id);
//
//        Wallet wallet = new Wallet("Wallet 1","KEY 1");
//        long wallet_id = db.createWallet(card_id, wallet);
//
//        wallet = new Wallet("Wallet 2","KEY 2");
//        wallet_id = db.createWallet(card_id, wallet);

        List<Card> cardList = db.getAllCards();
        for(int i=0;i<cardList.size();i++){
            Log.e("cardId", String.valueOf(cardList.get(i).getId()));
            content = "\0";
            List<Wallet> walletList = db.getWallets(cardList.get(i).getId());
            for(int j=0;j<walletList.size();j++){
                Log.e("cardId", walletList.get(j).getWallet_name());
                content = content + walletList.get(j).getWallet_name() + "\n";
            }
            movie = new MyCardsItem(cardList.get(i).getCardName(), content);
            myCardsArrayList.add(movie);
        }

//        movie = new MyCardsItem("Mad Max: Fury Road", "Paytm\nFreecharge");
//        myCardsArrayList.add(movie);

        mAdapter.notifyDataSetChanged();
    }
}
