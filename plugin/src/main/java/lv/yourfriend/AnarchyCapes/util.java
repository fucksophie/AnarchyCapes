package lv.yourfriend.AnarchyCapes;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class util {
    static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    static Gson gson = new Gson();

    static OkHttpClient client = new OkHttpClient();

    public static APIResponse post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return gson.fromJson(response.body().string(), APIResponse.class);
        }
    }

    public static boolean isImg(String URLName){
        boolean isAlive;

        try {
            URL url = new URL(URLName);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");

            int responseCode = huc.getResponseCode();

            isAlive = responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return Pattern.matches("(\\b(https?)://)?[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]", URLName) && URLName.endsWith(".png") && isAlive;
    }

    public static class APIResponse {
        public String message;
        public boolean error;
    }
}
