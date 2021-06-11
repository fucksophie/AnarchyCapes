package lv.yourfriend.AnarchyCapes;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.plugin.Plugin;

public class DiscordSRVListener {
    private final Plugin plugin;

    public DiscordSRVListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void discordReadyEvent(DiscordReadyEvent event) {
        DiscordUtil.getJda().addEventListener(new JDAListener(plugin));
    }
}
