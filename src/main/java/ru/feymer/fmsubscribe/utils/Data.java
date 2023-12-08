package ru.feymer.fmsubscribe.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.feymer.fmsubscribe.FmSubscribe;

import java.io.File;
import java.io.IOException;

public class Data {
    private static File file;
    public static FileConfiguration customFile;

    public static void load() {
        FmSubscribe.getInstance().getConfig().options().copyDefaults();
        setup();
        get().options().copyDefaults(true);
        save();
    }

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("fmSubscribe").getDataFolder(), "data.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var1) {
            }
        }

        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return customFile;
    }

    public static void save() {
        try {
            customFile.save(file);
        } catch (IOException var1) {
            System.out.println("Не могу сохранить файл.");
        }

    }

    public static void setData(Player player, String date) {
        get().set("data." + player.getName().toLowerCase() + ".date", date);
        save();
    }

    public static void removeData(Player player) {
        get().set("data." + player.getName().toLowerCase() + ".date", (Object) null);
        save();
    }

    public static String getData(Player player) {
        Object object = get().get("data." + player.getName().toLowerCase() + ".date");
        String result = "";
        if (object != null) {
            result = object.toString();
        }

        return result;
    }

    public static void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }
}

