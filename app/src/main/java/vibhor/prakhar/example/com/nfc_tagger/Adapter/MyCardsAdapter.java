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
    private TextView no,yes;
    private DatabaseHelper db;

    public MyCardsAdapter(List<MyCardsItem> myCardsItemList, Context context, List<Card> cardList) {
        this.myCardsItemList = myCardsItemList;
        this.context = context;
        this.cardList = cardList;
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
//                Log.e("abcprakhar","abcprakhar");
//                Toast.makeText(context,"abc",Toast.LENGTH_SHORT).show();
                db = new DatabaseHelper(getApplicationContext());
                Toast.makeText(context,myCardsItem.getTitle(),Toast.LENGTH_SHORT).show();
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


    /*
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title, content;
        public ImageView context_dots;
        public IMyViewHolderClicks mListener;

        public MyViewHolder(View view, IMyViewHolderClicks mListener) {
            super(view);
            this.mListener = mListener;
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            context_dots = (ImageView) view.findViewById(R.id.context_dots);
            context_dots.setOnClickListener(this);
//            context_dots.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.e("abcprakhar","abcprakhar");
//                    Toast.makeText(mContext,"abc",Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        @Override
        public void onClick(View view) {
//            if (view instanceof ImageView){
//                mListener.onTomato((ImageView)view);
//            }
            if(view.getId() == R.id.context_dots) {
                Log.d("hell", "");
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mycard_item, parent, false);

        MyCardsAdapter.MyViewHolder myViewHolder = new MyViewHolder(itemView, new IMyViewHolderClicks() {
            @Override
            public void onTomato(ImageView callerImage) {
                Log.d("VEGETABLES", "To-m8-tohs");
            }
        });

        return myViewHolder;
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

//    public void showPopup(){
//        final View popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_window, null);
//        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//
//        ListView listView = (ListView) popupView.findViewById(R.id.popup_listview);
//        ArrayList<String> data = new ArrayList<>();
//        data.add("Edit");
//        data.add("Delete");
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
//                android.R.layout.simple_list_item_1, android.R.id.text1, data);
//        listView.setAdapter(adapter);
//
//        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
//
//    }
*/
}
