package ru.feymer.fmsubscribe;

import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.feymer.fmsubscribe.commands.FmSubscribeCommand;
import ru.feymer.fmsubscribe.commands.FmSubscribeTabCompleter;
import ru.feymer.fmsubscribe.tasks.Clear;
import ru.feymer.fmsubscribe.utils.*;

public final class FmSubscribe extends JavaPlugin {

    public static FmSubscribe instance;
    private static Chat chat = null;

    public void onEnable() {
        instance = this;
        this.setupChat();
        Bukkit.getConsoleSender().sendMessage(Hex.color(""));
        Bukkit.getConsoleSender().sendMessage(Hex.color("&a» &fПлагин &a" + getPlugin(FmSubscribe.class).getName() + " &fвключился&f!"));
        Bukkit.getConsoleSender().sendMessage(Hex.color("&a» &fВерсия: &av" + getPlugin(FmSubscribe.class).getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(Hex.color(""));
        this.getCommand("fmsubscribe").setExecutor(new FmSubscribeCommand());
        this.getCommand("fmsubscribe").setTabCompleter(new FmSubscribeTabCompleter());
        this.saveDefaultConfig();
        Data.load();
        Updater updater = new Updater(this);
        updater.start();
        PlaceholderAPI.registerPlaceholderHook("fmsubscribe", new Placeholders());
        (new Placeholders()).register();
        Clear clear = new Clear();
        clear.runTaskTimerAsynchronously(FmSubscribe.getInstance(), 0, 60);
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Hex.color(""));
        Bukkit.getConsoleSender().sendMessage(Hex.color("&a» &fПлагин &a" + getPlugin(FmSubscribe.class).getName() + " &fвыключился&f!"));
        Bukkit.getConsoleSender().sendMessage(Hex.color("&a» &fВерсия: &av" + getPlugin(FmSubscribe.class).getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(Hex.color(""));
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = this.getServer().getServicesManager().getRegistration(Chat.class);
        chat = (Chat)rsp.getProvider();
        return chat != null;
    }

    public static FmSubscribe getInstance() {
        return instance;
    }
}

