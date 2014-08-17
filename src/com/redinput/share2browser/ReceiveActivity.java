package com.redinput.share2browser;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.redinput.share2browser.adapter.UrlsAdapter;

public class ReceiveActivity extends Activity {
	private ArrayList<String> listUrl = new ArrayList<>();

	@Override
	protected void onStart() {
		super.onStart();

		Utils.applySharedTheme(this);

		Intent intent = getIntent();
		if (Intent.ACTION_SEND.equalsIgnoreCase(intent.getAction())) {
			String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
			listUrl = Utils.extractUrls(sharedText);

		} else if (Intent.ACTION_VIEW.equalsIgnoreCase(intent.getAction())) {
			listUrl.add(intent.getDataString());
		}

		if (listUrl.size() > 0) {
			new AsyncExpand().execute();

		} else {
			Toast.makeText(this, R.string.no_links, Toast.LENGTH_SHORT).show();
		}
	}

	private class AsyncExpand extends AsyncTask<Void, Void, Void> {
		private final ArrayList<ArrayList<String>> expandedUrls;
		private final ProgressDialog dialog;

		public AsyncExpand() {
			expandedUrls = new ArrayList<>();
			dialog = new ProgressDialog(ReceiveActivity.this);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog.setMessage("Extracting and expanding urls");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			for (String url : listUrl) {
				while ((url.startsWith("http://www.google.com/url"))
						|| (url.startsWith("https://www.google.com/url"))) {
					url = url.substring(url.indexOf("?q=") + 3, url.indexOf("&"));
				}
				expandedUrls.add(Utils.expandUrl(url));
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			Builder builder = new Builder(ReceiveActivity.this);
			builder.setAdapter(new UrlsAdapter(ReceiveActivity.this, expandedUrls),
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ArrayList<String> url = expandedUrls.get(which);
							String urlFinal = url.get(url.size() - 1);

							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlFinal)));
							ReceiveActivity.this.finish();
						}
					});
			builder.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					ReceiveActivity.this.finish();
				}
			});

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			SharedPreferences sPref = PreferenceManager
					.getDefaultSharedPreferences(ReceiveActivity.this);
			if (sPref.getBoolean("vibrate", false)) {
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(200);
			}

			builder.create().show();
		}
	}

}
