package lv.yourfriend.AnarchyCapes.commands;


import lv.yourfriend.AnarchyCapes.AnarchyCapes;
import lv.yourfriend.AnarchyCapes.util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CopyCape implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String url = String.join(" ", args);

        if (sender instanceof Player) {
            sender.sendMessage("Trying to copy.");

            try {
                if(doesExist("http://localhost/capes/" + url + ".png")) {
                    util.APIResponse lol = util.post("http://localhost:20012/v1/update", "{\"username\": \"" + sender.getName() + "\", \"image\":\"http://localhost/capes/" + url + ".png\",\"auth\":\""+ AnarchyCapes.key + "\"}");

                    if(lol.error) {
                        sender.sendMessage("Experienced error: " + lol.message);
                    } else {
                        sender.sendMessage("Copied cape from " + url + " to " + sender.getName() + "!");
                    }
                } else {
                    sender.sendMessage("User does not have a cape!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return true;
    }


    public static boolean doesExist(String url) throws IOException
    {
        HttpURLConnection.setFollowRedirects(false);

        HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(url)).openConnection();

        httpURLConnection.setRequestMethod("HEAD");

        int responseCode = httpURLConnection.getResponseCode();

        return responseCode == HttpURLConnection.HTTP_OK;
    }
}
