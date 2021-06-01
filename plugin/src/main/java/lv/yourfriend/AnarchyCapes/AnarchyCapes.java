package lv.yourfriend.AnarchyCapes;

import lv.yourfriend.AnarchyCapes.commands.SetCape;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.bukkit.plugin.java.JavaPlugin;

public class AnarchyCapes extends JavaPlugin {
    public static String key = "";
    
    @Override
    public void onEnable() {
        try {
            File authFile = new File("AnarchyCapes_auth.txt");
            authFile.createNewFile();

            key = new String(Files.readAllBytes(authFile.toPath()));

            this.getCommand("setcape").setExecutor(new SetCape());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}