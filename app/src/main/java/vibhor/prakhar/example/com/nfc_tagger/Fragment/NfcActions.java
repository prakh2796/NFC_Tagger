package vibhor.prakhar.example.com.nfc_tagger.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vibhor.prakhar.example.com.nfc_tagger.Adapter.NfcActionsAdapter;
import vibhor.prakhar.example.com.nfc_tagger.Adapter.SettingsListAdapter;
import vibhor.prakhar.example.com.nfc_tagger.Model.SettingsRowItem;
import vibhor.prakhar.example.com.nfc_tagger.R;

/**
 * Created by Pewds on 18-Jul-17.
 */

public class NfcActions extends Fragment {

    private View view;
    private ListView listView;
    private List<SettingsRowItem> nfcActionItemList;
    private NfcActionsAdapter nfcActionsAdapter;
    private String[] actions = {"Lock Tag", "Copy Tag", "Erase Tag", "Set Password", "Remove Password"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.nfc_actions, container, false);

        listView = (ListView) view.findViewById(R.id.nfcactions_listview);
        nfcActionItemList = new ArrayList<SettingsRowItem>();
        nfcActionsAdapter = new NfcActionsAdapter(getActivity(), nfcActionItemList);
        listView.setAdapter(nfcActionsAdapter);

        for(int i = 0; i < 5; i++){
            SettingsRowItem settingsRowItem = new SettingsRowItem();
            settingsRowItem.setSettings_menu(actions[i]);
            nfcActionItemList.add(settingsRowItem);
        }
        nfcActionsAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),actions[position],Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
