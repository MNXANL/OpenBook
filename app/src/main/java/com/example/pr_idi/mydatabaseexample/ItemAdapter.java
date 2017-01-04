package com.example.pr_idi.mydatabaseexample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private ArrayList<Book> dades = new ArrayList<Book>();
    private OnItemClickListener onItemClickListener;

    public ItemAdapter(List<Book> values){
        dades.clear();
        for(Book i: values){
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
        holder.mcategory.setOnClickListener(listener);
        holder.mnum.setOnClickListener(listener);
        holder.mstar.setOnClickListener(listener);
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
        private TextView mcategory;
        private TextView mnum;
        private ImageView mstar;

        private ItemViewHolder(View itemView){
            super(itemView);
            mtitol = (TextView) itemView.findViewById(R.id.titol_row);
            mautor = (TextView) itemView.findViewById(R.id.autor_row);
            mcategory = (TextView) itemView.findViewById(R.id.cat_row);
            mnum = (TextView) itemView.findViewById(R.id.num_row);
            mstar= (ImageView) itemView.findViewById(R.id.star_row);
        }

        private void bindEntry (Book b){
            mtitol.setText(b.getTitle());
            mautor.setText(b.getAuthor());
            mcategory.setText(b.getCategory());
            mnum.setText("3");
            /*String val = b.getPersonal_evaluation();
            switch (val){
                case "molt bo":
                    mnum.setText("5");
                    break;
                case "bo":
                    mnum.setText("4");
                    break;
                case "regular":
                    mnum.setText("3");
                    break;
                case "dolent":
                    mnum.setText("2");
                    break;
                case "molt dolent":
                    mnum.setText("1");
                    break;
                default:
                    mnum.setText("0");
                    break;
            }*/
        }
    }

    public void setFilter(ArrayList<Book>newList){
        dades = new ArrayList<>();
        dades.addAll(newList);
        notifyDataSetChanged();
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