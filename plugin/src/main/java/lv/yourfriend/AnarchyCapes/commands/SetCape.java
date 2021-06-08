package lv.yourfriend.AnarchyCapes.commands;


import lv.yourfriend.AnarchyCapes.AnarchyCapes;
import lv.yourfriend.AnarchyCapes.util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

import static lv.yourfriend.AnarchyCapes.util.isImg;

public class SetCape implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String url = String.join(" ", args);

        if (sender instanceof Player) {
            if (isImg(url)) {
                sender.sendMessage("Trying to set cape.");

                try {
                    util.APIResponse lol = util.post("http://localhost:20012/v1/update", "{\"username\": \"" + sender.getName() + "\", \"image\":\"" + url + "\",\"auth\":\""+ AnarchyCapes.key + "\"}");

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
}
