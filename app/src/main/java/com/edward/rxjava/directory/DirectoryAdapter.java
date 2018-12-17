package com.edward.rxjava.directory;

import com.edward.rxjava.directory.DirectoryBean;
import com.edward.rxjava.R;
import com.edward.rxjava.WebViewActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能描述：
 *
 * @author (作者) edward（冯丰枫）
 * @link http://www.jianshu.com/u/f7176d6d53d2
 * 创建时间： 2018/12/15
 */
public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder> {
    private final List<DirectoryBean> mList;

    DirectoryAdapter() {
        mList = new ArrayList<>();
    }

    void notifyDataSetChanged(List<DirectoryBean> directoryBeans) {
        mList.clear();
        mList.addAll(directoryBeans);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(mList.get(i));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.directory_item_text)
        TextView directory_item_text;

        ViewHolder(@NonNull ViewGroup viewGroup) {
            super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_directory_item, viewGroup, false));
            ButterKnife.bind(this, itemView);
        }

        void setData(DirectoryBean directoryBean) {
            directory_item_text.setText(directoryBean.getTitle());
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), WebViewActivity.class);
                intent.putExtra("url", directoryBean.getUrl());
                v.getContext().startActivity(intent);
            });
        }
    }
}
