package vibhor.prakhar.example.com.nfc_tagger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Prakhar Gupta on 03/11/2016.
 */

public class SettingsListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<SettingsRowItem> settingsRowItemList;

    public SettingsListAdapter(Context context, List<SettingsRowItem> settingsRowItemList) {
        this.context = context;
        this.settingsRowItemList = settingsRowItemList;
    }

    @Override
    public int getCount() {
        return settingsRowItemList.size();
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
        SettingsRowItem settingsRowItem = settingsRowItemList.get(i);
        settings_menu.setText(settingsRowItem.getSettings_menu());
        return view;
    }
}
