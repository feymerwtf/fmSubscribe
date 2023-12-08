package ru.feymer.fmsubscribe.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import ru.feymer.fmsubscribe.FmSubscribe;

import java.util.List;

public class Utils {

    public static FileConfiguration getConfig() {
        return FmSubscribe.getInstance().getConfig();
    }

    public static String getString(String path) {
        return Hex.color(getConfig().getString(path));
    }

    public static List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }

    public static void sendMessage(CommandSender player, String message) {
        player.sendMessage(Hex.color(message));
    }
}
