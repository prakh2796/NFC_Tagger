package vibhor.prakhar.example.com.nfc_tagger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import vibhor.prakhar.example.com.nfc_tagger.Model.SettingsRowItem;
import vibhor.prakhar.example.com.nfc_tagger.R;

/**
 * Created by Pewds on 18-Jul-17.
 */

public class NfcActionsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<SettingsRowItem> nfcActionItemList;

    public NfcActionsAdapter(Context context, List<SettingsRowItem> nfcActionItemList) {
        this.context = context;
        this.nfcActionItemList = nfcActionItemList;
    }

    @Override
    public int getCount() {
        return nfcActionItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        view = inflater.inflate(R.layout.settings_row_item, null);
        TextView settings_menu = (TextView) view.findViewById(R.id.settings_menu);
        SettingsRowItem settingsRowItem = nfcActionItemList.get(i);
        settings_menu.setText(settingsRowItem.getSettings_menu());
        return view;
    }
}
