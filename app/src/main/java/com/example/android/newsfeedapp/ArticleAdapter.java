package com.example.android.newsfeedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {

    ArticleAdapter(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final Article currentArticle = getItem(position);
        TextView titleView = listItemView.findViewById(R.id.title);
        TextView authorView = listItemView.findViewById(R.id.author);
        TextView sectionView = listItemView.findViewById(R.id.section);
        TextView dateView = listItemView.findViewById(R.id.date);
        TextView timeView = listItemView.findViewById(R.id.time);
        if (currentArticle != null) {

            titleView.setText(currentArticle.getTitle());
            String author = currentArticle.getAuthor();
            if (author.hashCode() != "".hashCode()) {
                authorView.setText(currentArticle.getAuthor());
            } else {
                authorView.setVisibility(View.GONE);
            }
            sectionView.setText(currentArticle.getSection());

            if (currentArticle.getDate() != null) {
                String[] locations = currentArticle.getDate().split("T");
                dateView.setText(locations[0]);

                timeView.setText(locations[1].replace("Z", ""));
            }

        }

        return listItemView;
    }
}
