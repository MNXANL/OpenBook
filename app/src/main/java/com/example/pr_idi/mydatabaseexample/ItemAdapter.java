package com.example.pr_idi.mydatabaseexample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marta on 27/12/16.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private ArrayList<Book> dades = new ArrayList<Book>();
    private OnItemClickListener onItemClickListener;
    public ItemAdapter(List<Book> values){
        for(Book i:values){
            dades.add(i);
        }
    }
    public void add(Book b){
        dades.add(b);
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listed_item, parent, false);
        ItemViewHolder ivh = new ItemViewHolder(itemView);
        return ivh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Book b = dades.get(position);
        holder.bindEntry(b);
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onItemClickListener.onItemClick(b);
            }
        };
        holder.mtitol.setOnClickListener(listener);
        holder.mautor.setOnClickListener(listener);
        /*holder.mpublisher.setOnClickListener(listener);
        holder.myear.setOnClickListener(listener);
        holder.mcategory.setOnClickListener(listener);
        holder.mval.setOnClickListener(listener);*/

    }

    @Override
    public int getItemCount() {
        return dades.size();
    }

    /*public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }*/

    /*@Override
    public void onClick(View view){
        if(listener!= null) listener.onClick(view);
    }*/

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView mtitol;
        private TextView mautor;
        private TextView mpublisher;
        private TextView myear;
        private TextView mcategory;
        private RatingBar mval;

        private ItemViewHolder(View itemView){
            super(itemView);
            mtitol = (TextView) itemView.findViewById(R.id.titol);
            mautor = (TextView) itemView.findViewById(R.id.autor);
            mpublisher = (TextView) itemView.findViewById(R.id.pub);
            myear = (TextView) itemView.findViewById(R.id.year);
            mcategory = (TextView) itemView.findViewById(R.id.cat);
            mval = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }

        private void bindEntry (Book b){
            mtitol.setText(b.getTitle());
            mautor.setText(b.getAuthor());
            /*mpublisher.setText(b.getPublisher());
            String y = String.valueOf(b.getYear());
            myear.setText( y ); //EXCEPTION!
            mcategory.setText(b.getCategory());
            //String val = b.getPersonal_evaluation(); //EXCEPTION!
            mval.setRating(3.5f);
            switch (val){
                case "molt bo":
                    mval.setRating(5.0f);
                    break;
                case "bo":
                    mval.setRating(4.0f);
                    break;
                case "regular":
                    mval.setRating(3.0f);
                    break;
                case "dolent":
                    mval.setRating(2.0f);
                    break;
                case "molt dolent":
                    mval.setRating(1.0f);
                    break;
                default:
                    mval.setRating(0.0f);
                    break;
            }*/
        }
    }
    public OnItemClickListener getOnItemClickListener(){
        return onItemClickListener;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public void setOnScrollListener(RecyclerView mrec, Action scrollAction){
        new OnScrollUpDownListener(mrec, 8, scrollAction);
    }

}
