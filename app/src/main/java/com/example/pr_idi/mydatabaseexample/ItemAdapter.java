package com.example.pr_idi.mydatabaseexample;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private ArrayList<Book> dades = new ArrayList<Book>();
    MainActivity ctx;
    private int longPosition;
    private boolean edit = false;
    private boolean delete = false;
    private List<Long> IndexList;

    public ItemAdapter(List<Book> values, MainActivity ctx){
        this.ctx = ctx;
        for(Book i: values){
            dades.add(i);
        }
    }

    public void setEdit(boolean edit){
        this.edit = edit;
    }

    public boolean getEdit() {
        return edit;
    }

    public void setDelete(boolean del){
        this.delete = del;
    }

    public boolean getDelete(){
        return delete;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listed_item, parent, false);
        ItemViewHolder ivh = new ItemViewHolder(itemView, ctx, dades);
        return ivh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final Book b = dades.get(position);
        holder.bindEntry(b); //emplenem dades
        holder.setLongClickListener(new LongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                //Toast.makeText(ctx, "Book is: "+ dades.get(position).getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                int req = 0;
                Book b = dades.get(position);
                if (!edit) {
                    //Toast.makeText(ctx, "Into BOOK [" + b.getTitle() + "] in POS : " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                    i = new Intent(ctx, Activity_Item.class);
                    req = 1;
                }
                else /*if (edit)*/ {
                    edit = false;
                    //Toast.makeText(ctx, "Editar " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                    i = new Intent(ctx, NewActivity.class);
                    req = 2;
                }
                i.putExtra("mid", b.getId());
                i.putExtra("mtitol", b.getTitle());
                i.putExtra("mautor", b.getAuthor());
                i.putExtra("myear", b.getYear());
                i.putExtra("mpublisher", b.getPublisher());
                i.putExtra("mcategory", b.getCategory());
                i.putExtra("mval", b.getPersonal_evaluation());
                ctx.startActivityForResult(i, req);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dades.size();
    }

    public Book getItemSelected(MenuItem item){
        Book b = dades.get(longPosition);
        //Toast.makeText(ctx, "Item seleccionat " + item.getTitle() + " del llibre " + b.getTitle(), Toast.LENGTH_SHORT).show();
        return b;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener{
        private TextView mtitol;
        private TextView mautor;
        private TextView mcategory;
        private TextView mnum;
        private ImageView mstar;
        private CheckBox checkBox;
        ArrayList<Book> dades = new ArrayList<>();
        MainActivity ctx;
        LongClickListener longClickListener;
        private ItemViewHolder(View itemView, final MainActivity ctx, ArrayList<Book> dades){
            super(itemView);
            this.ctx = ctx;
            this.dades = dades;
            itemView.setOnClickListener(this); //si clica a un item anem a on click
            mtitol = (TextView) itemView.findViewById(R.id.titol_row);
            mautor = (TextView) itemView.findViewById(R.id.autor_row);
            mcategory = (TextView) itemView.findViewById(R.id.cat_row);
            mnum = (TextView) itemView.findViewById(R.id.num_row);
            mstar= (ImageView) itemView.findViewById(R.id.star_row);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ctx.prepare_Selection(view, getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        private void bindEntry (Book b){
            b.getId();
            mtitol.setText(b.getTitle());
            mautor.setText(b.getAuthor());
            mcategory.setText(b.getCategory());
            String val = b.getPersonal_evaluation();
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
            }
            if(!delete) checkBox.setVisibility(View.GONE); //si no som en mode delete els checkbox no son visibles
            else{
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(false);
            }
        }

        @Override  public void onClick(View view) {   }

        public void setLongClickListener(LongClickListener longClickListener){
            this.longClickListener = longClickListener;
        }

        @Override
        public boolean onLongClick(View view) {
            this.longClickListener.onItemLongClick(getLayoutPosition());
            longPosition = getAdapterPosition();
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle(R.string.select);
            contextMenu.add(0,0,0,R.string.edit);
            contextMenu.add(0,1,0,R.string.delete);
        }

    }



   public void setFilter(ArrayList<Book>newList){
        dades = new ArrayList<>();
        dades.addAll(newList);
        notifyDataSetChanged();
    }
    public void updateAdapter(ArrayList<Book> list){
        for(Book i: list){
            dades.remove(i);
        }
        notifyDataSetChanged();
    }

}