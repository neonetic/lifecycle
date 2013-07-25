package docDoku.DocDokuPLM;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: martindevillers
 * Date: 22/07/13
 * To change this template use File | Settings | File Templates.
 */
public class HttpGetTask extends AsyncTask<String, Void, String>{

    public static String baseUrl;
    public static byte[] id;
    private HttpGetListener httpGetListener;

    public static final String CONNECTION_ERROR = "Connection Error";
    public static final String URL_ERROR = "Url error";

    public HttpGetTask(HttpGetListener httpGetListener){
        super();
        this.httpGetListener = httpGetListener;
    }

    public HttpGetTask(String url, String username, String password, HttpGetListener httpGetListener) throws UnsupportedEncodingException {
        super();
        baseUrl = url;
        id = Base64.encode((username + ":" + password).getBytes("ISO-8859-1"), Base64.DEFAULT);
        this.httpGetListener = httpGetListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = CONNECTION_ERROR;
        String pURL = baseUrl + strings[0];
        Log.i("docDoku.DocDokuPLM","Sending HttpGet request to url: " + pURL);

        try {
            URL url = new URL(pURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Basic " + new String(id, "US-ASCII"));
            conn.connect();

            int responseCode = conn.getResponseCode();
            Log.i("docDoku.DocDokuPLM","Response code: " + responseCode);
            if (responseCode == 200){
                Log.i("docDoku.DocDokuPLM", "Response headers: " + conn.getHeaderFields());
                Log.i("docDoku.DocDokuPLM", "Response message: " + conn.getResponseMessage());
                InputStream in = (InputStream) conn.getContent();
                result = inputStreamToString(in);
                Log.i("docDoku.DocDokuPLM", "Response content: " + result);
                in.close();
            }

            conn.disconnect();
        } catch (MalformedURLException e) {
            Log.e("docDoku.DocDokuPLM","ERROR: MalformedURLException");
            result = URL_ERROR;
            e.printStackTrace();
        } catch (ProtocolException e) {
            Log.e("docDoku.DocDokuPLM","ERROR: ProtocolException");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.e("docDoku.DocDokuPLM", "ERROR: UnsupportedEncodingException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("docDoku.DocDokuPLM","ERROR: IOException");
            e.printStackTrace();
            Log.e("docDoku.DocDokuPLM", "Exception message: " + e.getMessage());
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        httpGetListener.onHttpGetResult(result);
    }

    private String inputStreamToString(InputStream in) throws IOException {
        String string;
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader bf = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = bf.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        string = sb.toString();
        reader.close();
        bf.close();
        return string;
    }

}