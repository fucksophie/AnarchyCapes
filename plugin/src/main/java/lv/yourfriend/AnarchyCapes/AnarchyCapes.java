package lv.yourfriend.AnarchyCapes;

import github.scarsz.discordsrv.DiscordSRV;
import lv.yourfriend.AnarchyCapes.commands.SetCape;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import github.scarsz.discordsrv.DiscordSRV.*;

public class AnarchyCapes extends JavaPlugin {
    public static String key = "";
    private DiscordSRVListener discordsrvListener = new DiscordSRVListener(this);

    @Override
    public void onEnable() {
        DiscordSRV.api.subscribe(discordsrvListener);
        try {
            File authFile = new File("AnarchyCapes_auth.txt");
            authFile.createNewFile();

            key = new String(Files.readAllBytes(authFile.toPath()));

            Reflections reflections = new Reflections("lv.yourfriend.AnarchyCapes.commands");
            for(Class <? extends CommandExecutor> command: reflections.getSubTypesOf(CommandExecutor.class)) {
                try {
                    this.getCommand(command.getSimpleName().toLowerCase()).setExecutor(command.getDeclaredConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
            }
            this.getCommand("setcape").setExecutor(new SetCape());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        DiscordSRV.api.unsubscribe(discordsrvListener);
    }
}