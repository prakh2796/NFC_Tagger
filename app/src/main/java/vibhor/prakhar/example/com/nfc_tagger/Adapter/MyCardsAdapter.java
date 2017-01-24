package vibhor.prakhar.example.com.nfc_tagger.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vibhor.prakhar.example.com.nfc_tagger.Activity.MainActivity;
import vibhor.prakhar.example.com.nfc_tagger.Interface.IMyViewHolderClicks;
import vibhor.prakhar.example.com.nfc_tagger.Model.Card;
import vibhor.prakhar.example.com.nfc_tagger.Model.MyCardsItem;
import vibhor.prakhar.example.com.nfc_tagger.R;
import vibhor.prakhar.example.com.nfc_tagger.Service.DatabaseHelper;
import vibhor.prakhar.example.com.nfc_tagger.Service.ModifyCardDialog;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Prakhar Gupta on 23/11/2016.
 */

public class MyCardsAdapter extends BaseAdapter {

    private List<MyCardsItem> myCardsItemList;
    private Context context;
    private List<Card> cardList;
    private LayoutInflater inflater;

    private ModifyCardDialog modifyCardDialog;
    private TextView no,yes,textView;
    private DatabaseHelper db;
    private String displayText;

    public MyCardsAdapter(List<MyCardsItem> myCardsItemList, Context context, List<Card> cardList, String displayText) {
        this.myCardsItemList = myCardsItemList;
        this.context = context;
        this.cardList = cardList;
        this.displayText = displayText;
    }

    @Override
    public int getCount() {
        return myCardsItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return myCardsItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        view = inflater.inflate(R.layout.mycard_item, null);
        holder = new ViewHolder();
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.content = (TextView) view.findViewById(R.id.content);
        holder.context_dots = (ImageView) view.findViewById(R.id.context_dots);
        view.setTag(holder);

        final MyCardsItem myCardsItem = myCardsItemList.get(i);
        holder.title.setText(myCardsItem.getTitle());
        holder.content.setText(myCardsItem.getContent());

        holder.context_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new DatabaseHelper(getApplicationContext());
                Toast.makeText(context,myCardsItem.getTitle(),Toast.LENGTH_SHORT).show();
                modifyCardDialog = new ModifyCardDialog(context);
                modifyCardDialog.show();
                no = (TextView) modifyCardDialog.findViewById(R.id.no_button);
                yes = (TextView) modifyCardDialog.findViewById(R.id.yes_button);
                textView = (TextView) modifyCardDialog.findViewById(R.id.textView);
                textView.setText(displayText);
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
                        db.deleteCard(cardList.get(i).getId());
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                });
            }
        });

        return view;
    }

    static class ViewHolder
    {
        public TextView title, content;
        public ImageView context_dots;
    }
}
