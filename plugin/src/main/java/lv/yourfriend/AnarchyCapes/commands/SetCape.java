package lv.yourfriend.AnarchyCapes.commands;


import com.google.gson.Gson;
import lv.yourfriend.AnarchyCapes.AnarchyCapes;
import okhttp3.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class SetCape implements CommandExecutor {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    Gson gson = new Gson();
    
    OkHttpClient client = new OkHttpClient();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String url = String.join(" ", args);

        if (sender instanceof Player) {
            if (isImg(url)) {
                sender.sendMessage("Trying to set cape.");

                try {
                    APIResponse lol = post("http://localhost:20012/v1/update", "{\"username\": \"" + sender.getName() + "\", \"image\":\"" + url + "\",\"auth\":\""+ AnarchyCapes.key+"\"}");

                    if(lol.error) {
                        sender.sendMessage("Experienced error: " + lol.message);
                    } else {
                        sender.sendMessage("Sucessfully set your cape!");
                    }
                } catch (IOException e) {
                    sender.sendMessage("Could not send API request. API has crashed.");
                    e.printStackTrace();
                }
            } else {
                sender.sendMessage("URl is not valid.");
            }
        }


        return true;
    }



    APIResponse post(String url, String json) throws IOException {
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
        String message;
        boolean error;
    }
}
