package ru.feymer.fmsubscribe.tasks;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;
import ru.feymer.fmsubscribe.FmSubscribe;
import ru.feymer.fmsubscribe.utils.Data;
import ru.feymer.fmsubscribe.utils.Utils;

import java.time.LocalDateTime;

public class Clear extends BukkitRunnable {

    private static Chat chat = null;

    @Override
    public void run() {
        this.setupChat();
        Bukkit.getOnlinePlayers().forEach(player -> {

            String oldData = Data.getData(player);
            if (!oldData.isEmpty()) {
                if (!oldData.equalsIgnoreCase("forever")) {
                    oldData = oldData + "T00:00:00";
                    LocalDateTime time = LocalDateTime.parse(oldData);
                    if (time.compareTo(LocalDateTime.now()) <= 0) {
                        String prefix = chat.getPlayerPrefix(player);
                        int symbol_lenght = Utils.getString("settings.symbol").length();
                        prefix = prefix.substring(0, prefix.length() - symbol_lenght);

                        Bukkit.getScheduler().runTask(FmSubscribe.getInstance(), () -> {
                            for (String commands : Utils.getStringList("settings.commands-on-take")) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.replace("%player%", player.getName()));
                            }

                        });

                        Data.removeData(player);
                        chat.setPlayerPrefix(player, Utils.getString("settings.prefix-2").replace("%prefix%", prefix));
                    }
                }
            }
        });
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = FmSubscribe.getInstance().getServer().getServicesManager().getRegistration(Chat.class);
        chat = (Chat)rsp.getProvider();
        return chat != null;
    }
}
