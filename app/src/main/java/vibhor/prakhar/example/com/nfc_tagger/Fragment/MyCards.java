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
import android.widget.Button;
import android.widget.EditText;
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
import vibhor.prakhar.example.com.nfc_tagger.Service.EditCardDialog;
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
    private TextView no,yes,cardName;
    private String displayText;
    private Button edit,cancel;
    private EditCardDialog editCardDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mycards, container, false);

        db = new DatabaseHelper(getActivity());

//        recyclerView = (RecyclerView) view.findViewById(R.id.mycard);
        listView = (ListView) view.findViewById(R.id.mycard);
        prepareCardData();
        displayText = "Are you sure you want to delete this Card ?";
        mAdapter = new MyCardsAdapter(myCardsArrayList, getActivity(), cardList, displayText);
        mAdapter.notifyDataSetChanged();
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyCardsItem myCardsItem = myCardsArrayList.get(i);
                intent = new Intent(getActivity(), CardView.class);
                intent.putExtra("name", cardList.get(i).getCardName());
                intent.putExtra("id", cardList.get(i).getId());
                startActivity(intent);
                getActivity().finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                editCardDialog = new EditCardDialog(getActivity());
                editCardDialog.show();
                cancel = (Button) editCardDialog.findViewById(R.id.cancel_button);
                edit = (Button) editCardDialog.findViewById(R.id.edit_button);
                cardName = (EditText) editCardDialog.findViewById(R.id.cardName);
                cardName.setText(cardList.get(i).getCardName());

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editCardDialog.dismiss();
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.updateCard(cardList.get(i).getId(), cardName.getText().toString());
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        editCardDialog.dismiss();
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                return true;
            }
        });
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
            content = "\0";
            walletList = db.getWallets(cardList.get(i).getId());
            for(int j=0;j<walletList.size();j++){
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
