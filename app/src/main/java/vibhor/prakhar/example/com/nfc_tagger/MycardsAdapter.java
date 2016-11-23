package vibhor.prakhar.example.com.nfc_tagger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Prakhar Gupta on 23/11/2016.
 */

public class MycardsAdapter extends RecyclerView.Adapter<MycardsAdapter.MyViewHolder> {
    private List<MyCardsItem> myCardsItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
        }
    }

    public MycardsAdapter(List<MyCardsItem> myCardsItemList) {
        this.myCardsItemList = myCardsItemList;
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
}
