package com.agmcs.ssuussee.Adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.agmcs.ssuussee.R;
import com.agmcs.ssuussee.model.NewsItem;

import java.util.List;

/**
 * Created by agmcs on 2015/5/20.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private List<NewsItem> list;
    public interface onItemClick{
        void onClick(View view, int position);
        boolean onLongClick(View view, int position);
    }

    private NewsAdapter.onItemClick mOnItemClick;

    public void setmOnItemClick(NewsAdapter.onItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
    }

    public NewsAdapter(List<NewsItem> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if(position + 1 == getItemCount()){
            return TYPE_FOOTER;
        }else{
            return TYPE_ITEM;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i == TYPE_ITEM){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item,viewGroup,false);
            return new NewsAdapter.MyViewHolder(view);
        }else{
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.footview,viewGroup,false);
            return new NewsAdapter.FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,final int i) {
        if(viewHolder instanceof NewsAdapter.MyViewHolder){
            ((NewsAdapter.MyViewHolder)viewHolder).title.setText(list.get(i).getTitle());
            ((NewsAdapter.MyViewHolder)viewHolder).date.setText(list.get(i).getDate());
            if(i%2 == 0){
                ((MyViewHolder)viewHolder).fram_parent.setBackgroundColor(Color.parseColor("#FAFAFA"));
            }else {
                ((MyViewHolder)viewHolder).fram_parent.setBackgroundColor(Color.parseColor("#EEEEEE"));
            }
            if(mOnItemClick != null){
                ((MyViewHolder)viewHolder).fram_parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClick.onClick(v,i);
                    }
                });
                ((NewsAdapter.MyViewHolder)viewHolder).fram_parent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return mOnItemClick.onLongClick(v,i);
                    }
                });
            }
            ((MyViewHolder)viewHolder).fram_parent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            view.setAlpha(0.5f);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            view.setAlpha(1.0f);
                            break;

                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<NewsItem> datas){
        list.addAll(datas);
        this.notifyDataSetChanged();
    }

    public void reAdd(List<NewsItem> datas) {
        list.clear();
        list.addAll(datas);
        this.notifyDataSetChanged();
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView date;
        private FrameLayout fram_parent;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.item_title);
            date = (TextView)itemView.findViewById(R.id.item_date);
            fram_parent = (FrameLayout)itemView.findViewById(R.id.fram_parent);
        }
    }
}
