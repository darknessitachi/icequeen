package org.lst.trading.lib.util;

import java.io.IOException;
import java.util.function.Consumer;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Http {
	
    private static final Logger log = LoggerFactory.getLogger(Http.class);

    private static CloseableHttpClient client;

    public synchronized static HttpClient getDefaultHttpClient() {
        if (client == null) {
            client = HttpClients.createDefault();
        }
        return client;
    }

    public static String getResponseText(String url) {
    	try {
			HttpGet request = new HttpGet(url);
			HttpResponse response = getDefaultHttpClient().execute(request);
			
			return EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return "";
    }
    
}
