package ba.unsa.etf.rma.zerina;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by zerin on 5/19/2018.
 */

public class KnjigePoznanika extends IntentService {


    ArrayList<Knjiga> listaKnjiga​ = new ArrayList<Knjiga>();
    public static int STATUS_START = 0;
    public static int STATUS_FINISH = 1;
    public static int STATUS_ERROR = 2;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public KnjigePoznanika(String name) {
        super(name);
    }

    public KnjigePoznanika(){
        super(null);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String idKorisnika = intent.getStringExtra("idKorisnika");


        String url1 = "https://www.googleapis.com/books/v1/users/" +idKorisnika + "/bookshelves";
        ArrayList<String> idBookshelves = new ArrayList<String>();

        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);

            JSONObject jo = new JSONObject(rezultat);



            if(jo.has("items")) {

                JSONArray items = jo.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject bookshelve = items.getJSONObject(i);
                    String id = "";
                    if (bookshelve.has("id")) {
                        id = bookshelve.getString("id");
                    }
                    idBookshelves.add(id);
                }
            }
        } catch (MalformedURLException e) {
            receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
        } catch (IOException e) {
            receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
        } catch (JSONException e) {
            receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
        }


        for(int l=0; l<idBookshelves.size(); l++) {

            String idPolice = idBookshelves.get(l);
            String url2 = "https://www.googleapis.com/books/v1/users/" + idKorisnika + "/bookshelves/" + idPolice + "/volumes";

            try {
                URL url = new URL(url2);
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
                        listaKnjiga​.add(k);
                    }
                }
            } catch (MalformedURLException e) {
                receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
            } catch (IOException e) {
                receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
            } catch (JSONException e) {
                receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
            }
        }

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listaKnjiga", listaKnjiga​);
        receiver.send(KnjigePoznanika.STATUS_FINISH, bundle);


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

}
