package net.laboulangerie.townybanners.utils;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.LongDataField;

import java.util.concurrent.TimeUnit;

public class CooldownUtils {
    public static long getCurrentTime() {
        return (System.currentTimeMillis());
    }

    private static String niceTime(long time, String plural, String singular) {
        return time != 0 ? time > 1 ? String.valueOf(time) + plural + ", " : String.valueOf(time) + singular + ", " : "";
    }

    public static String getHumanReadableTime(long millis, String days, String day, String hours, String hour, String minutes, String minute, String seconds, String second) {


        long timeDays = (TimeUnit.MILLISECONDS.toDays(millis) / 1000);
        long timeHours = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis));
        long timeMinutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));

        String niceDays = niceTime(timeDays, days, day);
        String niceHours = niceTime(timeHours, hours, hour);
        String niceMinutes = niceTime(timeMinutes, minutes, minute);
        String niceSeconds = niceTime(timeSeconds, seconds, second);

        return niceDays + niceHours + niceMinutes + niceSeconds;
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
