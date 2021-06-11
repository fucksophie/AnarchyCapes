package lv.yourfriend.AnarchyCapes;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.api.events.guild.GuildUnavailableEvent;
import github.scarsz.discordsrv.dependencies.jda.api.events.message.MessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JDAListener extends ListenerAdapter {
    private final Plugin plugin;

    public JDAListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        List<String> args = new LinkedList<String>(Arrays.asList(event.getMessage().getContentRaw().split(" ")));
        String command = args.get(0);
        MessageChannel channel = event.getChannel();
        args.remove(0);

        System.out.println(command);
        if(command.equals("#register")) {
            String image = String.join(" ", args);

            if(!image.isEmpty()) {
                if(DiscordSRV.getPlugin().getAccountLinkManager().getLinkedAccounts().containsKey(event.getMessage().getMember().getId())) {
                    if(util.isImg(image)) {
                        OfflinePlayer player = Bukkit.getOfflinePlayer(DiscordSRV.getPlugin().getAccountLinkManager().getUuid(event.getMessage().getMember().getId()));

                        channel.sendMessage("Attempting to set cape for" + player.getName() + "!").queue();

                        try {
                            util.APIResponse lol = util.post("http://localhost:20012/v1/update", "{\"username\": \"" + player.getName() + "\", \"image\":\"" + image + "\",\"auth\":\""+ AnarchyCapes.key + "\"}");

                            if(lol.error) {
                                channel.sendMessage("Experienced error: " + lol.message).queue();
                            } else {
                                channel.sendMessage("Set " + player.getName() + "'s cape to <" + image + ">!").queue();
                            }
                        } catch (IOException e) {
                            channel.sendMessage("Could not send API request. API has crashed!").queue();
                            e.printStackTrace();
                        }
                    } else {
                        channel.sendMessage("<"+image+"> is not a valid URL!").queue();
                    }
                } else {
                    channel.sendMessage("You are not linked!").queue();
                }
            } else {
                channel.sendMessage("Image URL is empty!").queue();
            }
        }
    }

}
