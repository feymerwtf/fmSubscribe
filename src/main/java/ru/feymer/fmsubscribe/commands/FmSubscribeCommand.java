package ru.feymer.fmsubscribe.commands;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import ru.feymer.fmsubscribe.FmSubscribe;
import ru.feymer.fmsubscribe.utils.Data;
import ru.feymer.fmsubscribe.utils.Utils;

import java.time.LocalDateTime;

public class FmSubscribeCommand implements CommandExecutor {

    private static Chat chat = null;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        this.setupChat();
        Player player;
        String oldData;
        if (args.length > 0 && args[0].equalsIgnoreCase("give")) {
            if (args.length != 3) {
                for (String help : Utils.getStringList("messages.no-args"))
                    Utils.sendMessage(sender, help);
                return true;
            } else if (!sender.hasPermission("fmsubscribe.give")) {
                Utils.sendMessage(sender, Utils.getString("messages.no-permission"));
                return true;
            } else {
                player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    Utils.sendMessage(sender, Utils.getString("messages.null-player"));
                    return true;
                } else {
                    oldData = Data.getData(player);
                    if (!oldData.isEmpty()) {
                        Utils.sendMessage(sender, Utils.getString("messages.already-exists"));
                        return true;
                    } else {
                        String newData = args[2];
                        if (!newData.equalsIgnoreCase("forever")) {
                            try {
                                Integer days = Integer.parseInt(newData);
                                LocalDateTime time = LocalDateTime.now();
                                time = time.plusDays((long) days);
                                newData = time.toString().substring(0, 10);
                            } catch (Exception e) {
                                newData = null;
                                Utils.sendMessage(sender, Utils.getString("messages.incorrect-date"));
                            }
                            if (newData == null) {
                                return true;
                            }
                        }
                        String prefix = chat.getPlayerPrefix(player);
                        prefix = prefix.substring(0, prefix.length() - 3);
                        String symbol = Utils.getString("settings.symbol");

                        for (String commands : Utils.getStringList("settings.commands-on-give")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.replace("%player%", player.getName()));
                        }
                        //chat.setPlayerPrefix("none", player, Utils.getString("settings.prefix-1").replace("%prefix%", prefix).replace("%symbol%", symbol));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " meta setprefix \"" + Utils.getString("settings.prefix-1").replace("%prefix%", prefix).replace("%symbol%", symbol));
                        Data.setData(player, newData);
                        if (args[2].equalsIgnoreCase("forever")) {
                            Utils.sendMessage(sender, Utils.getString("messages.give-forever").replace("%player%", player.getName()));
                        } else {
                            Utils.sendMessage(sender, Utils.getString("messages.give-duration").replace("%player%", player.getName()).replace("%duration%", args[2].toString()));
                        }
                        return true;
                    }
                }
            }
        } else if (args.length > 0 && args[0].equalsIgnoreCase("take")) {
            if (args.length != 2) {
                for (String help : Utils.getStringList("messages.no-args"))
                    Utils.sendMessage(sender, help);
                return true;
            } else if (!sender.hasPermission("fmsubscribe.take")) {
                Utils.sendMessage(sender, Utils.getString("messages.no-permission"));
                return true;
            } else {
                player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    Utils.sendMessage(sender, Utils.getString("messages.null-player"));
                    return true;
                } else {
                    oldData = Data.getData(player);
                    if (oldData.isEmpty()) {
                        Utils.sendMessage(sender, Utils.getString("messages.no-exists"));
                        return true;
                    } else {

                        for (String commands : Utils.getStringList("settings.commands-on-take")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.replace("%player%", player.getName()));
                        }

                        String prefix = chat.getPlayerPrefix(player);
                        int symbol_lenght = Utils.getString("settings.symbol").length();
                        prefix = prefix.substring(0, prefix.length() - symbol_lenght);
                        Data.removeData(player);
                        //chat.setPlayerPrefix("none", player, Utils.getString("settings.prefix-2").replace("%prefix%", prefix));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " meta setprefix \"" + Utils.getString("settings.prefix-2").replace("%prefix%", prefix));
                        Utils.sendMessage(sender, Utils.getString("messages.take").replace("%player%", player.getName()));
                        return true;
                    }
                }
            }
        } else if (args.length > 0 && args[0].equalsIgnoreCase("info")) {
            if (args.length != 2) {
                for (String help : Utils.getStringList("messages.no-args"))
                    Utils.sendMessage(sender, help);
                return true;
            } else if (!sender.hasPermission("fmsubscribe.info")) {
                Utils.sendMessage(sender, Utils.getString("messages.no-permission"));
                return true;
            } else {
                player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    Utils.sendMessage(sender, Utils.getString("messages.null-player"));
                    return true;
                } else {
                    oldData = Data.getData(player);
                    if (oldData.isEmpty()) {
                        Utils.sendMessage(sender, Utils.getString("messages.no-sub"));
                        return true;
                    } else {
                        Utils.sendMessage(sender, Utils.getString("messages.yes-sub"));
                        return true;
                    }
                }
            }
        } else if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (args.length != 1) {
                for (String help : Utils.getStringList("messages.no-args"))
                    Utils.sendMessage(sender, help);
                return true;
            }
            if (!sender.hasPermission("fmsubscribe.reload")) {
                Utils.sendMessage(sender, Utils.getString("messages.no-permission"));
                return true;
            }
            FmSubscribe.getInstance().reloadConfig();
            Data.reload();
            Utils.sendMessage(sender, Utils.getString("messages.reload"));
            return true;
        } else {
            for (String help : Utils.getStringList("messages.no-args"))
                Utils.sendMessage(sender, help);
        }

        return false;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = FmSubscribe.getInstance().getServer().getServicesManager().getRegistration(Chat.class);
        chat = (Chat)rsp.getProvider();
        return chat != null;
    }
}
