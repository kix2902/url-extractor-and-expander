package com.redinput.share2browser.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.redinput.share2browser.R;

@SuppressWarnings("deprecation")
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

	@SuppressLint("NewApi")
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
		final String urlFinal = url.get(url.size() - 1);
		String urlOriginal = url.get(0);

		txtFinal.setText(urlFinal);

		if (!urlFinal.equalsIgnoreCase(urlOriginal)) {
			txtOriginal.setText(urlOriginal);
			txtOriginal.setSelected(true);
			txtOriginal.setVisibility(View.VISIBLE);

		} else {
			txtOriginal.setVisibility(View.GONE);
		}

		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
					android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
							.getSystemService(Context.CLIPBOARD_SERVICE);
					clipboard.setText(urlFinal);

				} else {
					android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
							.getSystemService(Context.CLIPBOARD_SERVICE);
					android.content.ClipData clip = android.content.ClipData.newPlainText(context
							.getResources().getString(R.string.app_name), urlFinal);
					clipboard.setPrimaryClip(clip);
				}

				Toast.makeText(context, R.string.copied_clipboard, Toast.LENGTH_SHORT).show();

				return true;
			}
		});

		return convertView;
	}

}
