package ru.feymer.fmsubscribe.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "fmsubscribe";
    }

    @Override
    public @NotNull String getAuthor() {
        return "feymerwtf";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.3";
    }

    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (identifier.equalsIgnoreCase("has")) {
            String oldData = Data.getData(player);
            if (!oldData.isEmpty()) {
                return Utils.getString("placeholders.has.yes-sub");
            } else {
                return Utils.getString("placeholders.has.no-sub");
            }
        }
        return identifier;
    }
}
