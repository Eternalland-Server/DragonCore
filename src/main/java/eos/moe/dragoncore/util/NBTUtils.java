package eos.moe.dragoncore.util;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

public class NBTUtils {
    public static Class<?> xCraftItemStack;
    public static Class<?> xNBTTagCompound;
    public static Class<?> xNBTTagList;
    public static Constructor<?> xNewNBTTagString;
    public static Method xAsNMSCopay;
    public static Method xGetTag;
    public static Method xHasTag;
    public static Method xSet;
    public static Method xSetString;
    public static Method xSetInteger;
    public static Method xAdd;
    public static Method xSetTag;
    public static Method xAsBukkitCopy;
    public static Method xHasKey;
    public static Method xGet;
    public static Method xGetNBT;
    public static Method xGetInteger;
    public static Method xGetString;
    public static Method xGetListString;
    public static Method xSize;
    public static Method xRemove;

    public static ItemStack setNBT(ItemStack item, String key, String value) {
        try {
            Object nmsItem = NBTUtils.xAsNMSCopay.invoke(NBTUtils.xCraftItemStack, item);
            Object itemTag = ((boolean) NBTUtils.xHasTag.invoke(nmsItem)) ? NBTUtils.xGetTag.invoke(nmsItem) : NBTUtils.xNBTTagCompound.newInstance();
            Object tagString = NBTUtils.xNewNBTTagString.newInstance(value);
            NBTUtils.xSet.invoke(itemTag, key, tagString);
            NBTUtils.xSetTag.invoke(nmsItem, itemTag);
            item.setItemMeta(((ItemStack) NBTUtils.xAsBukkitCopy.invoke(NBTUtils.xCraftItemStack, nmsItem)).getItemMeta());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {

        }
        return item;
    }

    public static ItemStack setNBTList(ItemStack item, String key, List<String> list) {
        try {
            Object nmsItem = NBTUtils.xAsNMSCopay.invoke(NBTUtils.xCraftItemStack, item);
            Object itemTag = ((boolean) NBTUtils.xHasTag.invoke(nmsItem)) ? NBTUtils.xGetTag.invoke(nmsItem) : NBTUtils.xNBTTagCompound.newInstance();
            Object tagList = NBTUtils.xNBTTagList.newInstance();
            for (String str : list) {
                NBTUtils.xAdd.invoke(tagList, NBTUtils.xNewNBTTagString.newInstance(str));
            }
            NBTUtils.xSet.invoke(itemTag, key, tagList);
            NBTUtils.xSetTag.invoke(nmsItem, itemTag);
            item.setItemMeta(((ItemStack) NBTUtils.xAsBukkitCopy.invoke(NBTUtils.xCraftItemStack, nmsItem)).getItemMeta());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {

        }
        return item;
    }

    public static String getNBT(ItemStack item, String key) {
        try {
            Object nmsItem = NBTUtils.xAsNMSCopay.invoke(NBTUtils.xCraftItemStack, item);
            Object itemTag = ((boolean) NBTUtils.xHasTag.invoke(nmsItem)) ? NBTUtils.xGetTag.invoke(nmsItem) : NBTUtils.xNBTTagCompound.newInstance();
            if ((boolean) NBTUtils.xHasKey.invoke(itemTag, key)) {
                return (String) NBTUtils.xGetString.invoke(itemTag, key);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {

        }
        return null;
    }

    public static void removeNBT(ItemStack item, String key) {
        try {
            Object nmsItem = NBTUtils.xAsNMSCopay.invoke(NBTUtils.xCraftItemStack, item);
            Object itemTag = ((boolean) NBTUtils.xHasTag.invoke(nmsItem)) ? NBTUtils.xGetTag.invoke(nmsItem) : NBTUtils.xNBTTagCompound.newInstance();
            if ((boolean) NBTUtils.xHasKey.invoke(itemTag, key)) {
                NBTUtils.xRemove.invoke(itemTag, key);
                NBTUtils.xSetTag.invoke(nmsItem, itemTag);
                item.setItemMeta(((ItemStack) NBTUtils.xAsBukkitCopy.invoke(NBTUtils.xCraftItemStack, nmsItem)).getItemMeta());
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {

        }
    }

    public static List<String> getNBTList(ItemStack item, String key) {
        List<String> list = new ArrayList<>();
        try {
            Object nmsItem = NBTUtils.xAsNMSCopay.invoke(NBTUtils.xCraftItemStack, item);
            Object itemTag = ((boolean) NBTUtils.xHasTag.invoke(nmsItem)) ? NBTUtils.xGetTag.invoke(nmsItem) : NBTUtils.xNBTTagCompound.newInstance();
            Object tagList = ((boolean) NBTUtils.xHasKey.invoke(itemTag, key)) ? NBTUtils.xGet.invoke(itemTag, key) : NBTUtils.xNBTTagList.newInstance();
            for (int i = 0; i < (int) NBTUtils.xSize.invoke(tagList); ++i) {
                list.add((String) NBTUtils.xGetListString.invoke(tagList, i));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {

        }
        return list;
    }


    public static void loadNBTUtils() {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        String nmsName = "net.minecraft.server." + packet.substring(packet.lastIndexOf(46) + 1);
        System.out.println("NMS版本:" + nmsName);
        try {
            NBTUtils.xCraftItemStack = Class.forName(packet + ".inventory.CraftItemStack");
            Class<?> xNMSItemStack = Class.forName(nmsName + ".ItemStack");
            NBTUtils.xNBTTagCompound = Class.forName(nmsName + ".NBTTagCompound");
            Class<?> xNBTTagString = Class.forName(nmsName + ".NBTTagString");
            NBTUtils.xNBTTagList = Class.forName(nmsName + ".NBTTagList");
            Class<?> xNBTBase = Class.forName(nmsName + ".NBTBase");
            NBTUtils.xNewNBTTagString = xNBTTagString.getConstructor(String.class);
            NBTUtils.xAsNMSCopay = NBTUtils.xCraftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
            NBTUtils.xGetTag = xNMSItemStack.getDeclaredMethod("getTag");
            NBTUtils.xHasTag = xNMSItemStack.getDeclaredMethod("hasTag");
            NBTUtils.xSet = NBTUtils.xNBTTagCompound.getDeclaredMethod("set", String.class, xNBTBase);
            NBTUtils.xSetString = NBTUtils.xNBTTagCompound.getDeclaredMethod("setString", String.class, String.class);
            NBTUtils.xSetInteger = NBTUtils.xNBTTagCompound.getDeclaredMethod("setInt", String.class, int.class);
            NBTUtils.xAdd = NBTUtils.xNBTTagList.getDeclaredMethod("add", xNBTBase);
            NBTUtils.xSetTag = xNMSItemStack.getDeclaredMethod("setTag", NBTUtils.xNBTTagCompound);
            NBTUtils.xAsBukkitCopy = NBTUtils.xCraftItemStack.getDeclaredMethod("asBukkitCopy", xNMSItemStack);
            NBTUtils.xHasKey = NBTUtils.xNBTTagCompound.getDeclaredMethod("hasKey", String.class);
            NBTUtils.xGet = NBTUtils.xNBTTagCompound.getDeclaredMethod("get", String.class);

            NBTUtils.xGetNBT = NBTUtils.xNBTTagCompound.getDeclaredMethod("getCompound", String.class);
            NBTUtils.xGetString = NBTUtils.xNBTTagCompound.getDeclaredMethod("getString", String.class);
            NBTUtils.xGetInteger = NBTUtils.xNBTTagCompound.getDeclaredMethod("getInt", String.class);
            NBTUtils.xGetListString = NBTUtils.xNBTTagList.getDeclaredMethod("getString", Integer.TYPE);
            NBTUtils.xSize = NBTUtils.xNBTTagList.getDeclaredMethod("size");
            NBTUtils.xRemove = NBTUtils.xNBTTagCompound.getDeclaredMethod("remove", String.class);
            Bukkit.getConsoleSender().sendMessage("§6加载NBT功能");
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
            ex.printStackTrace();
        }
    }
}
