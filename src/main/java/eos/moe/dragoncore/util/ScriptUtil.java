package eos.moe.dragoncore.util;

import eos.moe.armourers.api.DragonAPI;
import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.config.sub.ConfigFile;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.Arrays;

public class ScriptUtil {
    private static ScriptEngine engine;

    static {
        try {
            engine = new NashornScriptEngineFactory().getScriptEngine();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§c[DragonCore-错误] §f当前Java版本" + System.getProperty("java.version") + "不存在Nashorn脚本库,请更换服务端使用的java版本");
        }
    }

    public static boolean execute(String script, Player player, ItemStack itemStack, String slotIdentifier, String limitString) throws Exception {
        if (engine == null) {
            player.sendMessage("§c[错误] §f当前Java版本不存在Nashorn脚本库");
            return false;
        }
        if (ConfigFile.dragonArmourers) {
            if (Bukkit.getPluginManager().isPluginEnabled("DragonArmourers")) {
                engine.put("DragonAPI", DragonAPI.class);
            } else {
                player.sendMessage("§c[错误] §f未检测到服务端载入龙之时装插件,您无法将物品放入槽位");
                return false;
            }
        } else if (script.contains("DragonAPI")) {
            player.sendMessage("§c[错误] §f插件config.yml 未开启龙之时装兼容,您无法将物品放入槽位");
            return false;
        }

        engine.put("PlaceholderAPI", PlaceholderAPI.class);
        engine.put("Bukkit", Bukkit.class);
        engine.put("Arrays", Arrays.class);
        engine.put("SlotAPI", SlotAPI.class);
        engine.put("Material", Material.class);
        YamlConfiguration config = DragonCore.getInstance().getFileManager().getConfig();
        ConfigurationSection scriptTools = config.getConfigurationSection("ScriptTools");
        if (scriptTools != null) {
            for (String key : scriptTools.getKeys(false)) {
                engine.put(key, Class.forName(scriptTools.getString(key)));
            }
        }


        engine.eval(script);
        if (engine instanceof Invocable) {
            Invocable in = (Invocable) engine;
            Object obj = in.invokeFunction("execute", player, itemStack, slotIdentifier, limitString);
            if (obj instanceof Boolean)
                return (boolean) obj;
            else
                throw new RuntimeException("js需要返回一个boolean的结果");
        }
        return false;
    }
}
