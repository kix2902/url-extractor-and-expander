package com.redinput.share2browser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

public class Utils {
	private static final int DEFAULT_CONNECT_TIMEOUT = 1000;
	private static final int DEFAULT_READ_TIMEOUT = 1000;

	public static ArrayList<String> expandUrl(String urlArg) {
		if (!urlArg.contains("http")) {
			urlArg = "http://" + urlArg;
		}

		ArrayList<String> visitedUrls = new ArrayList<>();
		String originalUrl = urlArg;
		visitedUrls.add(originalUrl);

		Pair<Integer, String> expanded = expand(originalUrl);
		while ((expanded.first >= 300) && (expanded.first < 400)) {
			originalUrl = expanded.second;
			visitedUrls.add(originalUrl);
			expanded = expand(originalUrl);
		}
		return visitedUrls;
	}

	private static Pair<Integer, String> expand(String origUrl) {
		Pair<Integer, String> response = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(origUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.setReadTimeout(DEFAULT_READ_TIMEOUT);
			conn.setRequestProperty(
					"User-agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4");
			conn.connect();

			String expandedURL = conn.getHeaderField("Location");
			int statusCode = conn.getResponseCode();
			if (expandedURL != null) {
				response = new Pair<Integer, String>(statusCode, expandedURL);

			} else {
				response = new Pair<Integer, String>(statusCode, origUrl);
			}

		} catch (MalformedURLException e) {
			// e.printStackTrace();
			response = new Pair<Integer, String>(-1, "");

		} catch (UnknownHostException e) {
			// e.printStackTrace();
			response = new Pair<Integer, String>(-1, "");

		} catch (IOException e) {
			// e.printStackTrace();
			response = new Pair<Integer, String>(-1, "");

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return response;
	}

	private static String regex = "^(https?://)?(([\\w!~*'().&=+$%-]+: )?[\\w!~*'().&=+$%-]+@)?(([0-9]{1,3}\\.){3}[0-9]{1,3}|([\\w!~*'()-]+\\.)*([\\w^-][\\w-]{0,61})?[\\w]\\.[a-z]{2,6})(:[0-9]{1,4})?((/*)|(/+[\\w!~*'().;?:@&=+$,%#-]+)+/*)$";
	private static Pattern patt = Pattern.compile(regex);

	public static ArrayList<String> extractUrls(String text) {
		ArrayList<String> listUrl = new ArrayList<String>();

		String[] words = text.split("\\s+");
		for (String word : words) {
			if (isUrl(word)) {
				listUrl.add(word);
			}
		}

		return listUrl;
	}

	private static boolean isUrl(String s) {
		try {
			Matcher matcher = patt.matcher(s);
			return matcher.matches();

		} catch (RuntimeException e) {
			return false;
		}
	}

	public static void applySharedTheme(Activity act) {
		SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(act);
		String themeId = sPref.getString("theme", "dark");

		if ("dark".equalsIgnoreCase(themeId)) {
			act.setTheme(R.style.AppTheme);

		} else if ("light".equalsIgnoreCase(themeId)) {
			act.setTheme(R.style.AppTheme_Light);
		}
	}

}
