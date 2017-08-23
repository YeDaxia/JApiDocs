package io.github.yedaxia.apidocs.http;

import io.github.yedaxia.apidocs.Utils;

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
public class DHttpUtils {

    private DHttpUtils(){

    }

    /**
     * send post request
     *
     * @return
     * @throws IOException
     */
    public static DHttpResponse httpPost(DHttpRequest request) throws IOException{

        URL url = new URL(request.getUrl());
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.setInstanceFollowRedirects(request.isAutoRedirect());

        Map<String,String> headers = request.getHeaders();
        if(headers != null){
            for(Map.Entry<String, String> headerEntry : headers.entrySet()){
                httpConn.addRequestProperty(headerEntry.getKey(),  headerEntry.getValue());
            }
        }

        StringBuffer requestParams = new StringBuffer();
        Map<String, String> params = request.getParams();
        if (params != null && params.size() > 0) {
            httpConn.setDoOutput(true);

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


        return toResponse(httpConn);
    }

    /**
     * send get request
     *
     * @param requestURL
     * @return
     * @throws IOException
     */
    public static DHttpResponse httpGet(String requestURL) throws IOException{
        URL url = new URL(requestURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setDoOutput(true);
        httpConn.setRequestMethod("GET");
        return toResponse(httpConn);
    }

    private static DHttpResponse toResponse(HttpURLConnection httpConn) throws IOException {
        DHttpResponse response = new DHttpResponse();
        response.setCode(httpConn.getResponseCode());
        response.setStream(httpConn.getInputStream());
        for(int i = 0, size = httpConn.getHeaderFields().size(); i != size; i++ ){
            response.addHeader(httpConn.getHeaderFieldKey(i), httpConn.getHeaderField(i));
        }
        return response;
    }
}
