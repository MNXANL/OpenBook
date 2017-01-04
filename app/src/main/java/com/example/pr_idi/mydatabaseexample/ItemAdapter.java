package com.example.pr_idi.mydatabaseexample;

import android.content.Context;
import android.content.Intent;
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
    private Context ctx;
    public ItemAdapter(List<Book> values, Context ctx){
        this.ctx = ctx;
        for(Book i:values){
            dades.add(i);
        }
    }
    public void add(Book b){
        dades.add(b);
        notifyDataSetChanged();
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listed_item, parent, false);
        ItemViewHolder ivh = new ItemViewHolder(itemView, ctx, dades);
        return ivh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Book b = dades.get(position);
        holder.bindEntry(b); //emplenem dades
    }

    @Override
    public int getItemCount() {
        return dades.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private TextView mtitol;
        private TextView mautor;
        private TextView mcategory;
        private TextView mnum;
        private ImageView mstar;
        ArrayList<Book> dades = new ArrayList<>();
        Context ctx;
        private ItemViewHolder(View itemView, Context ctx, ArrayList<Book> dades){
            super(itemView);
            this.ctx = ctx;
            this.dades = dades;
            itemView.setOnClickListener(this); //si clica a un item anem a on click
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
                default:*/
                    mnum.setText("0");
            //        break;
            //}
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Book b = dades.get(position);
            Intent i = new Intent(this.ctx, Activity_Item.class);
            i.putExtra("mtitol", b.getTitle());
            i.putExtra("mautor", b.getAuthor());
            i.putExtra("myear", b.getYear());
            i.putExtra("mpublisher",b.getPublisher());
            i.putExtra("mcategory", b.getCategory());
            i.putExtra("mval",b.getPersonal_evaluation());
            this.ctx.startActivity(i);
        }
    }

    public void setOnScrollListener(RecyclerView mrec, Action scrollAction){
        new OnScrollUpDownListener(mrec, 8, scrollAction);
    }
    public void setFilter(ArrayList<Book>newList){
        dades = new ArrayList<>();
        dades.addAll(newList);
        notifyDataSetChanged();
    }

}
