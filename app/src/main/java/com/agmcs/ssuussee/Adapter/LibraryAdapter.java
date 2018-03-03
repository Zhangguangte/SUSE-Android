package com.agmcs.ssuussee.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agmcs.ssuussee.R;
import com.agmcs.ssuussee.model.Book;

import java.util.Date;
import java.util.List;

/**
 * Created by agmcs on 2015/5/21.
 */
public class LibraryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Book> book_list;

    public interface RenewClickListener{
        void onClick(View view, int position, String url);
    }

    private RenewClickListener renewClickListener;

    public void setRenewClickListener(RenewClickListener renewClickListener) {
        this.renewClickListener = renewClickListener;
    }

    public LibraryAdapter(List<Book> book_list) {
        this.book_list = book_list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Book book = book_list.get(position);
        ((MyViewHolder)holder).title.setText(book.getTitle());
        Date now = new Date();
        double days = (book.getEnd() - now.getTime())/(1000 * 60 * 60 * 24.0);
        int i = (int) Math.ceil(days);
        if(i>0){
            ((MyViewHolder)holder).days.setText(i + "days left");
        }else {
            ((MyViewHolder)holder).days.setText((-i) + "days since");
            ((MyViewHolder)holder).btn_text.setText("过期啦");
            ((MyViewHolder)holder).btn.setBackgroundColor(Color.parseColor("#e74c3c"));

        }
        if(this.renewClickListener != null  && i >0){
            ((MyViewHolder)holder).btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    renewClickListener.onClick(v,position,book_list.get(position).getUrl());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return book_list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView days;
        private RelativeLayout btn;
        private TextView btn_text;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.book_title);
            days = (TextView)itemView.findViewById(R.id.book_days);
            btn = (RelativeLayout)itemView.findViewById(R.id.book_renew);
            btn_text = (TextView)itemView.findViewById(R.id.renew_text);
        }
    }

    public void reAdd(List<Book> list){
        this.book_list.clear();
        this.book_list.addAll(list);
    }
}
