package vibhor.prakhar.example.com.nfc_tagger;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Prakhar Gupta on 17/11/2016.
 */

public class MyCards extends Fragment {
    private View view;
    private List<MyCardsItem> myCardsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MycardsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mycards, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mAdapter = new MycardsAdapter(myCardsArrayList);
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

        prepareMovieData();

        return view;
    }

    private void prepareMovieData() {
        MyCardsItem movie = new MyCardsItem("Mad Max: Fury Road", "Action & Adventure");
        myCardsArrayList.add(movie);

        movie = new MyCardsItem("Mad Max: Fury Road", "Action & Adventure");
        myCardsArrayList.add(movie);

        movie = new MyCardsItem("Mad Max: Fury Road", "Action & Adventure");
        myCardsArrayList.add(movie);

        movie = new MyCardsItem("Mad Max: Fury Road", "Action & Adventure");
        myCardsArrayList.add(movie);

        mAdapter.notifyDataSetChanged();
    }
}
