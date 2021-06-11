package lv.yourfriend.AnarchyCapes;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.api.events.message.MessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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


        if(command.equals("!register")) {
            String image = String.join(" ", args);

            if(!image.isEmpty()) {
                if(DiscordSRV.getPlugin().getAccountLinkManager().getLinkedAccounts().containsKey(event.getMessage().getMember().getId())) {
                    if(util.isImg(image)) {
                        OfflinePlayer player = Bukkit.getOfflinePlayer(DiscordSRV.getPlugin().getAccountLinkManager().getUuid(event.getMessage().getMember().getId()));

                        try {
                            util.APIResponse lol = util.post("http://localhost:20012/v1/update", "{\"username\": \"" + player.getName() + "\", \"image\":\"" + image + "\",\"auth\":\""+ AnarchyCapes.key + "\"}");

                            if(lol.error) {
                                channel.sendMessage(new EmbedBuilder()
                                        .setColor(Color.red)
                                        .setTitle("Error")
                                        .setDescription("Experienced error: " + lol.message).build()).queue();
                            } else {
                                channel.sendMessage(new EmbedBuilder()
                                        .setColor(Color.green)
                                        .setTitle("Success!")
                                        .setDescription("Set " + player.getName() + "'s cape!")
                                        .setImage(image).build()).queue();
                            }
                        } catch (IOException e) {
                            channel.sendMessage(new EmbedBuilder()
                                    .setColor(Color.red)
                                    .setTitle("Error")
                                    .setDescription("Could not send API request. API has crashed!").build()).queue();
                        }
                    } else {
                        channel.sendMessage(new EmbedBuilder()
                                .setColor(Color.red)
                                .setTitle("Error")
                                .setDescription("<"+image+"> is not a valid URL!").build()).queue();
                    }
                } else {
                    channel.sendMessage(new EmbedBuilder()
                            .setColor(Color.red)
                            .setTitle("Error")
                            .setDescription("You are not linked!").build()).queue();
                }
            } else {
                channel.sendMessage(new EmbedBuilder()
                        .setColor(Color.red)
                        .setTitle("Error")
                        .setDescription("Image URL is empty!").build()).queue();
            }
        } else if(command.equals("!cape")) {
            if(args.size() > 0) {
                if (util.isImg("http://localhost:20012/capes/" + args.get(0) + ".png")) {
                    channel.sendMessage(new EmbedBuilder()
                            .setColor(Color.green)
                            .setTitle("Success!")
                            .setImage("http://54.37.139.51/capes/" + args.get(0) + ".png?v=" + UUID.randomUUID().toString()).build()).queue();
                } else {
                    channel.sendMessage(new EmbedBuilder()
                            .setColor(Color.red)
                            .setTitle("Error")
                            .setDescription(args.get(0) + " doesn't exist!").build()).queue();
                }
            } else {
                channel.sendMessage(new EmbedBuilder()
                        .setColor(Color.red)
                        .setTitle("Error")
                        .setDescription("Input a username!").build()).queue();
            }
        }
    }

}
