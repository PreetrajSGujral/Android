package com.example.android.barcode;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.android.barcode.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        result = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  getWebsite();

                new doit().execute();
            }
        });
    }
        public class doit extends AsyncTask<Void, Void, Void> {
            String words;
            String words1;
            ArrayList<String> w= new ArrayList<>();
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    org.jsoup.nodes.Document doc = Jsoup.connect("https://www.google.com/search?q=amazon+pepsi&num=5").get();
                    words = doc.text();
                    Elements links= doc.select("a[href]");
                    int i=0;
                    for(Element link: links) {
                        if (link.attr("href").contains("https://www.amazon.com/") && link.attr("href").startsWith("https://www.amazon")) {
                            w.add(link.attr("href"));
                            Log.i("LINK FOUND", link.attr("href"));

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void avoid) {
                super.onPostExecute(avoid);
                //result.setText(words);
                result.setText(w.get(0));
            }
        }

    }
   /* private void getWebsite()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder= new StringBuilder();

                try{
                    org.jsoup.nodes.Document doc= (org.jsoup.nodes.Document) Jsoup.connect("https://www.google.com/search?q=amazon&num=2").get();
                    String title=doc.title();
                    Elements links= doc.select("a[href]");

                    builder.append(title).append("\n");

                    for(Element link: links)
                    {
                        builder.append("\n").append("Link: ").append(link.attr("href"));
                    }
                }catch( IOException e)
                {
                    builder.append(e.getMessage()).append("\n");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(builder.toString());
                    }
                });
            }
        });
    }*/
