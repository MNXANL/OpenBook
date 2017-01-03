package com.example.pr_idi.mydatabaseexample;

<<<<<<< HEAD
=======


/* todo: Esta clase por alguna razón... no va. :(


>>>>>>> 4046bac43c76db33d022d1b9e2af9a7151fc1d5d
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
/**
 * Created by marta on 27/12/16.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private ArrayList<Book> dades = new ArrayList<Book>();
    private OnItemClickListener onItemClickListener;
=======
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private ArrayList<Book> dades = new ArrayList<Book>();
    //private View.OnClickListener listener;
    private AdapterViewCompat.OnItemClickListener onItemClickListener;
>>>>>>> 4046bac43c76db33d022d1b9e2af9a7151fc1d5d
    public ItemAdapter(List<Book> values){
        for(Book i:values){
            dades.add(i);
        }
    }
<<<<<<< HEAD
    public void add(Book b){
        dades.add(b);
    }
=======
>>>>>>> 4046bac43c76db33d022d1b9e2af9a7151fc1d5d
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity__item, parent, false);
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
        holder.mpublisher.setOnClickListener(listener);
        holder.myear.setOnClickListener(listener);
        holder.mcategory.setOnClickListener(listener);
        holder.mval.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return dades.size();
    }

    /*public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
<<<<<<< HEAD
    }*/
=======
    }
>>>>>>> 4046bac43c76db33d022d1b9e2af9a7151fc1d5d

    /*@Override
    public void onClick(View view){
        if(listener!= null) listener.onClick(view);
<<<<<<< HEAD
    }*/
=======
    }
>>>>>>> 4046bac43c76db33d022d1b9e2af9a7151fc1d5d

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        protected TextView mtitol;
        protected TextView mautor;
        protected TextView mpublisher;
        protected TextView myear;
        protected TextView mcategory;
        protected RatingBar mval;

        public ItemViewHolder(View itemView){
            super(itemView);
            mtitol = (TextView) itemView.findViewById(R.id.titol);
            mautor = (TextView) itemView.findViewById(R.id.autor);
            mpublisher = (TextView) itemView.findViewById(R.id.pub);
            myear = (TextView) itemView.findViewById(R.id.year);
            mcategory = (TextView) itemView.findViewById(R.id.cat);
<<<<<<< HEAD
            mval = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }

=======
            mval = (RatingBar) itemView.findViewById(R.id.val);
        }
>>>>>>> 4046bac43c76db33d022d1b9e2af9a7151fc1d5d
        public void bindEntry (Book b){
            mtitol.setText(b.getTitle());
            mautor.setText(b.getAuthor());
            mpublisher.setText(b.getPublisher());
<<<<<<< HEAD
            String y = String.valueOf(b.getYear());
            myear.setText( y ); //EXCEPTION!
            mcategory.setText(b.getCategory());
            String val = b.getPersonal_evaluation(); //EXCEPTION!

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
=======
            myear.setText(b.getYear());
            mcategory.setText(b.getCategory());
            String val = b.getPersonal_evaluation().toString();
            switch (val){
                case "molt bo":
                    mval.setRating(5);
                    break;
                case "bo":
                    mval.setRating(4);
                    break;
                case "regular":
                    mval.setRating(3);
                    break;
                case "dolent":
                    mval.setRating(2);
                    break;
                case "molt dolent":
                    mval.setRating(1);
>>>>>>> 4046bac43c76db33d022d1b9e2af9a7151fc1d5d
                    break;
            }
        }
    }
    public OnItemClickListener getOnItemClickListener(){
        return onItemClickListener;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
<<<<<<< HEAD
    public void setOnScrollListener(RecyclerView mrec, Action scrollAction){
        new OnScrollUpDownListener(mrec, 8, scrollAction);
    }

}
=======

}*/
>>>>>>> 4046bac43c76db33d022d1b9e2af9a7151fc1d5d
