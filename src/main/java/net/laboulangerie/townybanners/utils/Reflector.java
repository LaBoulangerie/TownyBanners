package net.laboulangerie.townybanners.utils;

import org.bukkit.Bukkit;

import java.util.Optional;

public class Reflector {

   public static String ORG_BUKKIT_CRAFTBUKKIT = "org.bukkit.craftbukkit";
   public static String NET_MINECRAFT_SERVER = "net.minecraft.server";
   public static String VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(
           ORG_BUKKIT_CRAFTBUKKIT.length() + 1
   );

   public static Optional<Class<?>> nmsClass(String className) {
       StringBuilder sb = new StringBuilder()
               .append(NET_MINECRAFT_SERVER)
               .append(".")
               .append(VERSION)
               .append(".")
               .append(className);
       try {
           return Optional.of(Class.forName(sb.toString()));

       } catch (ClassNotFoundException ignored) {
           return Optional.empty();
       }
   }

   public static Optional<Class<?>> obcClass(String className) {
       StringBuilder sb = new StringBuilder()
               .append(ORG_BUKKIT_CRAFTBUKKIT)
               .append(".")
               .append(VERSION)
               .append(".")
               .append(className);
       try {
           return Optional.of(Class.forName(sb.toString()));
       } catch (ClassNotFoundException ignored) {
           return Optional.empty();
       }
   }


}
