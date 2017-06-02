package com.example.trung.rssreaderndk;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<NewsData> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new NewsAdapter(newsList);
        mRecyclerView.setAdapter(mAdapter);
//        preNewsData();
        HttpDownloadTask hdt = new HttpDownloadTask();
        hdt.execute("http://vnexpress.net/rss/the-thao.rss");

    }

    private void preNewsData() {
        NewsData news = new NewsData("http://img.f1.thethao.vnecdn.net/2017/04/01/640x640-11-jpeg-3419-149105961-3890-2459-1491061117_180x108.jpg", "Man Utd bất lực trước West Brom, mất cơ hội áp sát top 4", "Sat, 01 Apr 2017 20:30:00 +0700");
        newsList.add(news);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native NewsData testParseXml(String xml);

    public native ArrayList<NewsData> parseXml(String xml);

    private class HttpDownloadTask extends AsyncTask<String, Void, ArrayList<NewsData>> {
        private String currentTag = "";
        private String xmlStr;

        @Override
        protected ArrayList<NewsData> doInBackground(String... params) {
            String urlStr = params[0];
            InputStream is = null;
            ArrayList<NewsData> newsDataList = null;
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                is = connection.getInputStream();
                //read string
                xmlStr = readStream(is);
                newsDataList = parseXml(xmlStr);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newsDataList;
        }

        private String readStream(InputStream is) throws IOException {
            String result = "";
            final int bufferSize = 1;
            byte[] buffer = new byte[1];
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            while(true) {
                int count = is.read(buffer, 0, bufferSize);
                if(count == -1) {
                    break;
                }
                os.write(buffer);
            }
            os.close();
            result = new String(os.toByteArray(), "UTF-8");
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsData> newsDatas) {
            for (int i = 0; i<newsDatas.size(); i++) {
                newsList.add(newsDatas.get(i));
            }
            mAdapter.notifyDataSetChanged();
        }


    }
}
