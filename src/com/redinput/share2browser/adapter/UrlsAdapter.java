package com.redinput.share2browser.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.redinput.share2browser.R;

public class UrlsAdapter extends BaseAdapter {

	private final ArrayList<ArrayList<String>> listUrls;
	private final Context context;

	public UrlsAdapter(Context context, ArrayList<ArrayList<String>> listUrls) {
		this.context = context;
		this.listUrls = listUrls;
	}

	@Override
	public int getCount() {
		return listUrls.size();
	}

	@Override
	public Object getItem(int position) {
		return listUrls.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.dialog_url_item, parent,
					false);
		}

		TextView txtFinal = ViewHolder.get(convertView, R.id.txtFinalUrl);
		TextView txtOriginal = ViewHolder.get(convertView, R.id.txtOriginalUrl);

		txtFinal.setSelected(true);

		ArrayList<String> url = listUrls.get(position);
		String urlFinal = url.get(url.size() - 1);
		String urlOriginal = url.get(0);

		txtFinal.setText(urlFinal);

		if (!urlFinal.equalsIgnoreCase(urlOriginal)) {
			txtOriginal.setText(urlOriginal);
			txtOriginal.setSelected(true);
			txtOriginal.setVisibility(View.VISIBLE);

		} else {
			txtOriginal.setVisibility(View.GONE);
		}

		return convertView;
	}

}
