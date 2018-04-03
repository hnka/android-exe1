package br.ufpe.cin.if1001.rss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ceciliahunka on 02/04/18.
 */

public class NewsAdapter extends BaseAdapter {

    Context c;
    List<ItemRSS> items;

    public NewsAdapter(Context c, List<ItemRSS> items) {
        this.c = c;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.
                from(c).
                inflate(R.layout.itemlista, parent, false);

        TextView title = (TextView) v.findViewById(R.id.item_titulo);
        TextView data = (TextView) v.findViewById(R.id.item_data);

        String titleText = ((ItemRSS) getItem(position)).getTitle();
        String dataText = ((ItemRSS) getItem(position)).getPubDate();

        title.setText(titleText);
        data.setText(dataText);

        return v;
    }
}