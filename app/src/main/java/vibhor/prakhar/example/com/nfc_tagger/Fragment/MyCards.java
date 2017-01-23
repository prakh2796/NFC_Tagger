package vibhor.prakhar.example.com.nfc_tagger.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vibhor.prakhar.example.com.nfc_tagger.Activity.AddOrRemoveWallet;
import vibhor.prakhar.example.com.nfc_tagger.Activity.CardView;
import vibhor.prakhar.example.com.nfc_tagger.Activity.MainActivity;
import vibhor.prakhar.example.com.nfc_tagger.Adapter.MyCardsAdapter;
import vibhor.prakhar.example.com.nfc_tagger.Interface.ClickListener;
import vibhor.prakhar.example.com.nfc_tagger.Model.Card;
import vibhor.prakhar.example.com.nfc_tagger.Model.MyCardsItem;
import vibhor.prakhar.example.com.nfc_tagger.Model.Wallet;
import vibhor.prakhar.example.com.nfc_tagger.R;
import vibhor.prakhar.example.com.nfc_tagger.Service.DatabaseHelper;
import vibhor.prakhar.example.com.nfc_tagger.Service.ModifyCardDialog;
import vibhor.prakhar.example.com.nfc_tagger.Service.RecyclerTouchListener;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Prakhar Gupta on 17/11/2016.
 */

public class MyCards extends Fragment {
    private DatabaseHelper db;
    private View view;
    private List<MyCardsItem> myCardsArrayList = new ArrayList<>();
//    private RecyclerView recyclerView;
    private ListView listView;
    private MyCardsAdapter mAdapter;
    private MyCardsItem myCardsItem;
    private Intent intent;
    private List<Card> cardList;
    private List<Wallet> walletList;
    private String content = "\0";
    private ModifyCardDialog modifyCardDialog;
    private TextView no,yes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mycards, container, false);

        db = new DatabaseHelper(getActivity());

//        recyclerView = (RecyclerView) view.findViewById(R.id.mycard);
        listView = (ListView) view.findViewById(R.id.mycard);
        prepareCardData();
        mAdapter = new MyCardsAdapter(myCardsArrayList, getActivity(), cardList);
        mAdapter.notifyDataSetChanged();
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyCardsItem myCardsItem = myCardsArrayList.get(i);
//                Toast.makeText(getApplicationContext(), myCardsItem.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), CardView.class);
                intent.putExtra("name", cardList.get(i).getCardName());
                intent.putExtra("id", cardList.get(i).getId());
                startActivity(intent);
                getActivity().finish();
//                Log.d("hell", String.valueOf(view.getId()));
//                Log.d("hell", String.valueOf(R.id.context_dots));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                db.updateCard(cardList.get(i).getId(), "ALF");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                return true;
            }
        });

        /*
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MyCardsItem myCardsItem = myCardsArrayList.get(position);
//                Toast.makeText(getApplicationContext(), myCardsItem.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), CardView.class);
                intent.putExtra("name", cardList.get(position).getCardName());
                intent.putExtra("id", cardList.get(position).getId());
//                Log.d("hell", String.valueOf(view.getId()));
//                Log.d("hell", String.valueOf(R.id.context_dots));
//                if(view.getId() == R.id.context_dots){
//                    Log.d("hell", "");
//                } else {
                if(view.getId() != R.id.context_dots) {
                    startActivity(intent);
                }
//                }
            }


            @Override
            public void onLongClick(View view, final int position) {
                final MyCardsItem myCardsItem = myCardsArrayList.get(position);
                Toast.makeText(getApplicationContext(),cardList.get(position).getCardName(),Toast.LENGTH_SHORT).show();
                modifyCardDialog = new ModifyCardDialog(getActivity());
                modifyCardDialog.show();
                no = (TextView) modifyCardDialog.findViewById(R.id.no_button);
                yes = (TextView) modifyCardDialog.findViewById(R.id.yes_button);

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modifyCardDialog.dismiss();
                    }
                });

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modifyCardDialog.dismiss();
                        db.deleteCard(cardList.get(position).getId());
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
        }));*/
//        prepareCardData();

        return view;
    }

    private void prepareCardData() {

//        String content = "\0";

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

        cardList = db.getAllCards();
        for(int i=0;i<cardList.size();i++){
//            Log.e("cardId", String.valueOf(cardList.get(i).getId()));
            content = "\0";
            walletList = db.getWallets(cardList.get(i).getId());
            for(int j=0;j<walletList.size();j++){
//                Log.e("cardId", walletList.get(j).getWallet_name() + walletList.get(j).getWallet_key());
                content = content + walletList.get(j).getWallet_name() + "\n";
            }
            myCardsItem = new MyCardsItem(cardList.get(i).getCardName(), content);
            myCardsArrayList.add(myCardsItem);
        }

//        movie = new MyCardsItem("Mad Max: Fury Road", "Paytm\nFreecharge");
//        myCardsArrayList.add(movie);

//        mAdapter.notifyDataSetChanged();
    }
}
