package io.github.yedaxia.apidocs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class HttpUtils {


    /**
     * send post request
     *
     * @param requestURL
     * @param params
     * @return
     * @throws IOException
     */
    public static String sendPost(String requestURL, Map<String, String> params) throws IOException{
        URL url = new URL(requestURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoInput(true); // true indicates the server returns response
        StringBuffer requestParams = new StringBuffer();
        if (params != null && params.size() > 0) {
            httpConn.setDoOutput(true); // true indicates POST request
            // creates the params string, encode them using URLEncoder
            Iterator<String> paramIterator = params.keySet().iterator();
            while (paramIterator.hasNext()) {
                String key = paramIterator.next();
                String value = params.get(key);
                requestParams.append(URLEncoder.encode(key, "UTF-8"));
                requestParams.append("=").append(
                        URLEncoder.encode(value, "UTF-8"));
                requestParams.append("&");
            }

            // sends POST data
            OutputStreamWriter writer = new OutputStreamWriter(
                    httpConn.getOutputStream());
            writer.write(requestParams.toString());
            writer.flush();
        }

        InputStream inputStream = httpConn.getInputStream();
        return Utils.streamToString(inputStream);
    }

}
