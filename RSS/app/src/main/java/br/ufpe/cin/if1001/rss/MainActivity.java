package br.ufpe.cin.if1001.rss;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private final String RSS_FEED = "http://leopoldomt.com/if1001/g1brasil.xml";

    //OUTROS LINKS PARA TESTAR...
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    //use ListView ao invés de TextView - deixe o atributo com o mesmo nome
    private ListView conteudoRSS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //use ListView ao invés de TextView - deixe o ID no layout XML com o mesmo nome conteudoRSS
        //isso vai exigir o processamento do XML baixado da internet usando o ParserRSS
        conteudoRSS = (ListView) findViewById(R.id.conteudoRSS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new CarregaRSStask().execute(RSS_FEED);
    }

    private class CarregaRSStask extends AsyncTask<String, Void, List<ItemRSS>> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "iniciando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<ItemRSS> doInBackground(String... params) {
            //String conteudo = "provavelmente deu erro...";
            List<ItemRSS> listContent = new ArrayList<>();

            try {
                listContent = getRssFeed(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return listContent;
        }

        @Override
        protected void onPostExecute(List<ItemRSS> rows) {
            Toast.makeText(getApplicationContext(), "terminando...", Toast.LENGTH_SHORT).show();
            //ajuste para usar uma ListView
            //o layout XML a ser utilizado esta em res/layout/itemlista.xml
            conteudoRSS.setAdapter(new NewsAdapter(getApplicationContext(), rows));
            conteudoRSS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NewsAdapter adapter = (NewsAdapter) parent.getAdapter();
                    ItemRSS selected = (ItemRSS) adapter.getItem(position);

                    Uri uri = Uri.parse(selected.getLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
    }

    //Opcional - pesquise outros meios de obter arquivos da internet
    private List<ItemRSS> getRssFeed(String feed) throws IOException, XmlPullParserException {
        InputStream in = null;
        String rssFeed;
        List<ItemRSS> parser;

        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
            parser = ParserRSS.parse(rssFeed);

        } finally {
            if (in != null) {
                in.close();
            }
        }
        return parser;
    }
}
