package com.agmcs.ssuussee.Adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agmcs.ssuussee.R;
import com.agmcs.ssuussee.model.Newsdata;

import java.util.List;

/**
 * Created by agmcs on 2015/5/20.
 */
public class NewsContentDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Newsdata> list;

    public NewsContentDataAdapter(List<Newsdata> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.news_p,parent,false);

        return new NewsContentDataAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Newsdata newsdata = list.get(position);
        switch (newsdata.getNews_type()){
            case Newsdata.Type.CONTENT:
                ((NewsContentDataAdapter.MyViewHolder) holder).text.setText(Html.fromHtml(newsdata.getContent()));
                break;
            case Newsdata.Type.IMG:
                ((NewsContentDataAdapter.MyViewHolder) holder).text.setText(Html.fromHtml(newsdata.getImageLink()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    public void reAdd(List<Newsdata> datas){
        list.clear();
        list.addAll(datas);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            text = (TextView)itemView.findViewById(R.id.news_text);
            image = (ImageView)itemView.findViewById(R.id.news_image);
        }
    }
}
