package eu.napcode.gonoteit.ui.about;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Demonstrantion of using AsyncTask
 *
 * nanodegree requirement
 */
public class GithubDescriptionDownloadAsyncTask extends AsyncTask<String, Void, String> {

    private static String GITHUB_URL = "https://api.github.com/repos/nani92/gonoteit";

    private GithubDescriptionListener githubDescriptionListener;

    @Override
    protected String doInBackground(String... strings) {
        String description = "";

        try {
            HttpURLConnection urlConnection = openConnection();

            JSONObject topLevel = new JSONObject(getStringResponse(urlConnection));
            description = topLevel.getString("description");

            urlConnection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();

            if (githubDescriptionListener != null) {
                githubDescriptionListener.onDescriptionFetchFailed();
            }
        }

        return description;
    }

    private HttpURLConnection openConnection() throws IOException {
        URL url = new URL(GITHUB_URL);

        return (HttpURLConnection) url.openConnection();
    }

    private BufferedReader getReader(HttpURLConnection urlConnection) throws IOException {
        InputStream stream = new BufferedInputStream(urlConnection.getInputStream());

        return new BufferedReader(new InputStreamReader(stream));
    }

    private String getStringResponse(HttpURLConnection urlConnection) throws IOException {
        BufferedReader bufferedReader = getReader(urlConnection);
        StringBuilder builder = new StringBuilder();

        String responseString;

        while ((responseString = bufferedReader.readLine()) != null) {
            builder.append(responseString);
        }

        return builder.toString();
    }

    @Override
    protected void onPostExecute(String description) {
        super.onPostExecute(description);

        if (githubDescriptionListener != null) {
            githubDescriptionListener.onDescriptionReceived(description);
        }
    }

    public void attachListener(GithubDescriptionListener listener) {
        this.githubDescriptionListener = listener;
    }

    public interface GithubDescriptionListener {
        void onDescriptionReceived(String description);

        void onDescriptionFetchFailed();
    }
}
