package net.laboulangerie.townybanners.utils;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.LongDataField;

import java.util.concurrent.TimeUnit;

public class CooldownUtils {
    public static long getCurrentTime() {
        return (System.currentTimeMillis());
    }

    public static String getHumanReadableTime(long millis, String days, String hours, String minutes, String seconds) {
        long timeDays = TimeUnit.MILLISECONDS.toDays(millis) / 1000;
        long timeHours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis));
        long timeMinutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));

        return String.format("%d %s, %d %s, %d %s, %d %s",
                timeDays, days,
                timeHours, hours,
                timeMinutes, minutes,
                timeSeconds, seconds
                );
    }

    public static void setTownTimestamp(Town town) {
        LongDataField currentTimestampField = new LongDataField("townybanners_timestamp", getCurrentTime());
        town.addMetaData(currentTimestampField);
    }

    public static long getTownTimestamp(Town town) {
        LongDataField bannerTimestampField = (LongDataField) town.getMetadata("townybanners_timestamp");
        return bannerTimestampField.getValue();
    }

    public static void setNationTimestamp(Nation nation) {
        LongDataField currentTimestampField = new LongDataField("townybanners_timestamp", getCurrentTime());
        nation.addMetaData(currentTimestampField);
    }

    public static long getNationTimestamp(Nation nation) {
        LongDataField bannerTimestampField = (LongDataField) nation.getMetadata("townybanners_timestamp");
        return bannerTimestampField.getValue();
    }
}
