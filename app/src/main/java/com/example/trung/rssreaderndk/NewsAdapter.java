package com.example.trung.rssreaderndk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by trung on 3/20/2017.
 */

class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<NewsData> newsList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumb;
        public TextView title;
        public TextView date;
        public ViewHolder(View v) {
            super(v);
            thumb = (ImageView) v.findViewById(R.id.thumb);
            title = (TextView) v.findViewById(R.id.title);
            date = (TextView) v.findViewById(R.id.dateLabel);
        }
    }

    NewsAdapter(List<NewsData> newsList) {
        this.newsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsData newsData = newsList.get(position);
        new DownloadImageTask(holder.thumb).execute(newsData.getThumbUrl());
        holder.title.setText(newsData.getTitle());
        holder.date.setText(newsData.getDate());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap bitmap = null;
            try {
                InputStream is = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            this.imageView.setImageBitmap(bitmap);
        }
    }

}
