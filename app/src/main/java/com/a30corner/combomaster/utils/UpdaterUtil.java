package com.a30corner.combomaster.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.List;
import java.util.Locale;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Request;
import retrofit.client.UrlConnectionClient;
import android.content.Context;
import android.content.SharedPreferences;

import com.a30corner.combomaster.R;
import com.a30corner.combomaster.pad.monster.MonsterVO;
import com.a30corner.combomaster.utils.http.NewPets;
import com.a30corner.combomaster.utils.http.WebData;
import com.a30corner.combomaster.utils.http.WebData_Test;

public class UpdaterUtil {

    private static final boolean TEST = false;

    //private static final String HTTP_URL = "http://52.68.127.4";
    
    private static final String NEW_HTTP_URL = "http://188.166.227.62/";

    private static class NetworkErrorHandler implements ErrorHandler {

        @Override
        public Throwable handleError(RetrofitError cause) {
            return cause;
        }

    }

    // getMd5 only connect 3sec. to prevent long period black screen
    private static final class QuickUrlConnectionClient extends
            UrlConnectionClient {
        @Override
        protected HttpURLConnection openConnection(Request request)
                throws IOException {
            HttpURLConnection connection = super.openConnection(request);
            connection.setConnectTimeout(1 * 1000);
            //connection.setReadTimeout(30 * 1000);
            return connection;
        }
    }
    
	public static boolean checkUpdate(Context context) {
		if (NetworkUtils.isNetworkAvailable(context)) {
			SharedPreferences pref = context.getSharedPreferences("update",
					Context.MODE_PRIVATE);
			
			String lastVersion = pref.getString("version", "");
			String version = context.getString(R.string.support_version);
			if (!lastVersion.equals(version)) {
				return true;
			}

			String lastUpdate = pref.getString("lastUpdate",
					context.getString(R.string.modified_time));
			long count = UpdaterUtil.getPetsCount(version, lastUpdate);
			if (count > 0L) {
				return true;
			}
		}
		return false;
	}
    
    public class HttpResponse {
    	public long result;
    }
    
    public static long getPetsCount(String version, String lastUpdate) {
    	try {
	    	RestAdapter adapter = new RestAdapter.Builder().setEndpoint(NEW_HTTP_URL)
	    					.setErrorHandler(new NetworkErrorHandler()).setClient(new QuickUrlConnectionClient()).build();
	    	HttpResponse response = null;
	    	if (TEST) {
	    		response = adapter.create(NewPets.class).getPetsCount0(version, lastUpdate);
	    	} else {
	    		response = adapter.create(NewPets.class).getPetsCount(version, lastUpdate);
	    	}

    		return response.result;
    	} catch(Exception e) {
    		return 0;
    	}
    }
    
    public static long getModifiedTime() {
    	try {
	    	RestAdapter adapter = new RestAdapter.Builder().setEndpoint(NEW_HTTP_URL)
					.setErrorHandler(new NetworkErrorHandler()).setClient(new QuickUrlConnectionClient()).build();
	    	HttpResponse response = adapter.create(NewPets.class).getModifiedTime();
	    	return response.result;
    	} catch(Exception e) {
    		return 0L;
    	}
    }
    
    public static List<MonsterVO> getPetsList(String version, String lastUpdate) {
    	RestAdapter adapter = new RestAdapter.Builder().setEndpoint(NEW_HTTP_URL)
    					.setErrorHandler(new NetworkErrorHandler()).setClient(new QuickUrlConnectionClient()).build();
    	if (TEST) {
    		return adapter.create(NewPets.class).getPetsJson0(version, lastUpdate);
    	} else {
    		return adapter.create(NewPets.class).getPetsJson(version, lastUpdate);
    	}
    }


    public static String getMessage() {
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(NEW_HTTP_URL)
                .setErrorHandler(new NetworkErrorHandler()).build();
        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage();
        if ("zh".equals(lang)) {
            lang += "-" + locale.getCountry();
        }
        try {
            String message = null;
            if ( TEST ) {
                message = adapter.create(WebData_Test.class).getMessage(lang);
            } else {
                message = adapter.create(WebData.class).getMessage(lang);
            }
            try {
                return URLDecoder.decode(message, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return URLDecoder.decode(message);
            }
        } catch (Exception e) {
            return e.toString();
        }
    }
}
