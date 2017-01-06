package vibhor.prakhar.example.com.nfc_tagger.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vibhor.prakhar.example.com.nfc_tagger.Model.MyCardsItem;
import vibhor.prakhar.example.com.nfc_tagger.R;

/**
 * Created by Prakhar Gupta on 23/11/2016.
 */

public class MyCardsAdapter extends RecyclerView.Adapter<MyCardsAdapter.MyViewHolder> {

    private List<MyCardsItem> myCardsItemList;
    private Context mContext;

    public MyCardsAdapter(List<MyCardsItem> myCardsItemList, Context context) {
        this.myCardsItemList = myCardsItemList;
        this.mContext = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, content;
        public ImageView context_dots;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            context_dots = (ImageView) view.findViewById(R.id.context_dots);
            context_dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"abc",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mycard_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyCardsItem myCardsItem = myCardsItemList.get(position);
        holder.title.setText(myCardsItem.getTitle());
        holder.content.setText(myCardsItem.getContent());
    }

    @Override
    public int getItemCount() {
        return myCardsItemList.size();
    }

    public void showPopup(){
        final View popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_window, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ListView listView = (ListView) popupView.findViewById(R.id.popup_listview);
        ArrayList<String> data = new ArrayList<>();
        data.add("Edit");
        data.add("Delete");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_1, android.R.id.text1, data);
        listView.setAdapter(adapter);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

    }
}
