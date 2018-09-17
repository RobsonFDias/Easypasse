package br.com.easypasse.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import br.com.easypasse.R;
import br.com.easypasse.model.HistoricoModelo;
import br.com.easypasse.view.NavDrawerItem;

/**
 * Created by Jargs on 13/01/2018.
 */
public class HistoricoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HistoricoModelo> historicoItems;

    public HistoricoAdapter(Context context, ArrayList<HistoricoModelo> navDrawerItems) {
        this.context = context;
        this.historicoItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return historicoItems.size();
    }

    @Override
    public Object getItem(int position) {
        return historicoItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.historico_list_item, null);
        }

        /*
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        imgIcon.setImageDrawable(VectorDrawableCompat.create(context.getResources(), navDrawerItems.get(position).getIcon(), null));
        txtTitle.setText(navDrawerItems.get(position).getTitle());
        */
        return convertView;
    }

}
