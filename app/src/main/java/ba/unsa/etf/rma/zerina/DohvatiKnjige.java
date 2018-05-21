package ba.unsa.etf.rma.zerina;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by zerin on 5/17/2018.
 */

public class DohvatiKnjige extends AsyncTask<String, Integer, Void> {


    @Override
    protected Void doInBackground(String... strings) {

        String query​ = null;
        try {
            query​ = URLEncoder.encode(strings[0], "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url1 = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + query​+"&maxResults=5";

        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);

            JSONObject jo = new JSONObject(rezultat);



            if(jo.has("items")) {

                JSONArray items = jo.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {

                    JSONObject knjiga = items.getJSONObject(i);

                    String id = "";
                    String naziv = "";
                    ArrayList<Autor> autori = new ArrayList<Autor>();
                    String opis = "";
                    String datumObjavljivanja = "";
                    URL slika = new URL("http://books.google.com/books/content?id=VA3perfDcDIC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");
                    int brojStranica = 0;

                    if (knjiga.has("id")) {
                        id = knjiga.getString("id");
                    }
                    JSONObject volumeInfo = knjiga.getJSONObject("volumeInfo");
                    if (volumeInfo.has("title")) {
                        naziv = volumeInfo.getString("title");
                    }
                    if (volumeInfo.has("authors")) {
                        JSONArray authors = volumeInfo.getJSONArray("authors");
                        for (int j = 0; j < authors.length(); j++) {
                            Autor a = new Autor(authors.getString(j), "");
                            autori.add(a);
                        }
                    }
                    if (volumeInfo.has("description")) {
                        opis = volumeInfo.getString("description");
                    }
                    if (volumeInfo.has("publishedDate")) {
                        datumObjavljivanja = volumeInfo.getString("publishedDate");
                    }
                    if (volumeInfo.has("imageLinks")) {
                        JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                        if (imageLinks.has("thumbnail")) {
                            slika = new URL(imageLinks.getString("thumbnail"));
                        }
                    }
                    if (volumeInfo.has("pageCount")) {
                        brojStranica = Integer.parseInt(volumeInfo.getString("pageCount"));
                    }

                    Knjiga k = new Knjiga(id, naziv, autori, opis, datumObjavljivanja, slika, brojStranica);
                    knjige.add(k);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    public interface IDohvatiKnjigeDone{
        public void onDohvatiDone(ArrayList<Knjiga> k);
    }
    ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();
    private IDohvatiKnjigeDone pozivatelj;
    public  DohvatiKnjige(IDohvatiKnjigeDone p) {pozivatelj = p; };

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pozivatelj.onDohvatiDone(knjige);
    }

}
