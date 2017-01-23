package vibhor.prakhar.example.com.nfc_tagger.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vibhor.prakhar.example.com.nfc_tagger.Activity.AddOrRemoveWallet;
import vibhor.prakhar.example.com.nfc_tagger.Activity.CardView;
import vibhor.prakhar.example.com.nfc_tagger.Activity.MainActivity;
import vibhor.prakhar.example.com.nfc_tagger.Model.MyCardsItem;
import vibhor.prakhar.example.com.nfc_tagger.Model.Wallet;
import vibhor.prakhar.example.com.nfc_tagger.R;
import vibhor.prakhar.example.com.nfc_tagger.Service.DatabaseHelper;
import vibhor.prakhar.example.com.nfc_tagger.Service.ModifyCardDialog;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Prakhar Gupta on 23/11/2016.
 */

public class AddorRemoveAdapter extends BaseAdapter {

    private List<MyCardsItem> myCardsItemList;
    private LayoutInflater inflater;
    private Context context;
    private List<Wallet> walletList;
    private String cardName;
    private Long card_id;

    private ModifyCardDialog modifyCardDialog;
    private TextView no,yes;
    private DatabaseHelper db;

    public AddorRemoveAdapter(List<MyCardsItem> myCardsItemList, Context context, List<Wallet> walletList) {
        this.myCardsItemList = myCardsItemList;
        this.context = context;
        this.walletList = walletList;
    }

    public AddorRemoveAdapter(List<MyCardsItem> myCardsItemList, Context context, List<Wallet> walletList, String cardName, Long card_id) {
        this.myCardsItemList = myCardsItemList;
        this.context = context;
        this.walletList = walletList;
        this.cardName = cardName;
        this.card_id = card_id;
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
        view = inflater.inflate(R.layout.add_or_remove_wallet_item, null);
        holder = new ViewHolder();
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.content = (TextView) view.findViewById(R.id.content);
        holder.context_dots = (ImageView) view.findViewById(R.id.context_dots);
        if(walletList == null){
            holder.context_dots.setVisibility(View.GONE);
        }
        view.setTag(holder);

        MyCardsItem myCardsItem = myCardsItemList.get(i);
        holder.title.setText(myCardsItem.getTitle());
        holder.content.setText(myCardsItem.getContent());

        holder.context_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new DatabaseHelper(getApplicationContext());
                Log.e("abcxyzprakhar","abcxyzprakhar");
                Toast.makeText(context,"db done",Toast.LENGTH_SHORT).show();
                modifyCardDialog = new ModifyCardDialog(context);
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
                        db.deleteWallet(walletList.get(i).getId());
                        Intent intent = new Intent(context, CardView.class);
                        intent.putExtra("name", cardName);
                        intent.putExtra("id", card_id);
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

    /*
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
        }
    }

    public AddorRemoveAdapter(List<MyCardsItem> myCardsItemList) {
        this.myCardsItemList = myCardsItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_or_remove_wallet_item, parent, false);

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
    */
}
