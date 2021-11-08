package net.sakuragame.eternal.dragoncore.network;


import net.sakuragame.eternal.dragoncore.DragonCore;
import net.sakuragame.eternal.dragoncore.api.event.YamlPacketEvent;
import net.sakuragame.eternal.dragoncore.api.worldtexture.WorldTexture;
import net.sakuragame.eternal.dragoncore.api.worldtexture.animation.Animation;
import net.sakuragame.eternal.dragoncore.config.FolderType;
import net.sakuragame.eternal.dragoncore.config.sub.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


public class PacketSender extends PluginMessageSender {

    private static DragonCore plugin = DragonCore.getInstance();

    public static void removeModelEntityAnimation(LivingEntity entity, String animation, int transitionTime) {
        sendPluginMessage(getNearPlayers(entity), 0, buffer -> {
            buffer.writeUniqueId(entity.getUniqueId());
            buffer.writeString(animation);
            buffer.writeInt(transitionTime);
        });

    }

    public static void setModelEntityAnimation(LivingEntity entity, String animation, int transitionTime) {
        sendPluginMessage(getNearPlayers(entity), 1, buffer -> {
            buffer.writeUniqueId(entity.getUniqueId());
            buffer.writeString(animation);
            buffer.writeInt(transitionTime);
        });
    }

    public static void sendYaml(Player player, FolderType type, String fileName, YamlConfiguration yaml) {
        sendYaml(player, type.format(fileName), yaml);
    }

    public static void sendYaml(Player player, String fileName, YamlConfiguration yaml) {
        YamlPacketEvent yamlPacketEvent = new YamlPacketEvent(player, fileName, yaml);
        Bukkit.getPluginManager().callEvent(yamlPacketEvent);

        YamlConfiguration yamlConfiguration = yamlPacketEvent.getYaml();
        String file = yamlPacketEvent.getFileName();
        sendPluginMessage(player, 2, buffer -> {
            buffer.writeString(file);
            buffer.writeString(yamlConfiguration.saveToString());
        });
    }


    public static void sendPlayerWorld(Player player) {
        sendPluginMessage(player, 3, buffer -> {
            buffer.writeString(player.getWorld().getName());
        });
    }

    public static void setChatBoxText(Player player, String text) {
        sendPluginMessage(player, 4, buffer -> {
            buffer.writeString(text);
        });
    }

    public static void setEntityModel(Player player, UUID uuid, String name) {
        if (name != null) {
            sendPluginMessage(player, 5, buffer -> {
                buffer.writeUniqueId(uuid);
                buffer.writeString(name);
            });
        } else {
            sendPluginMessage(player, 6, buffer -> {
                buffer.writeUniqueId(uuid);
            });
        }
    }


    // 压缩包密码
    public static void sendZipPassword(Player player) {
        String str = ConfigFile.password;
        sendPluginMessage(player, 18, buffer -> {
            buffer.writeString(str);
        });
    }


    public static void sendClearCache(Player player) {
        sendPluginMessage(player, 11, buffer -> {
        });
    }

    public static void setPlayerWorldTexture(Player player, String key, Location location,
                                             float rotateX, float rotateY, float rotateZ, String path,
                                             float width, float height, float alpha, boolean followPlayer, boolean glow,
                                             UUID entity, boolean followEntity, double x, double y, double z) {

        sendPluginMessage(player, 12, buffer -> {
            buffer.writeString(key);
            buffer.writeString(location.getWorld().getName());
            buffer.writeDouble(location.getX());
            buffer.writeDouble(location.getY());
            buffer.writeDouble(location.getZ());
            buffer.writeFloat(rotateX);
            buffer.writeFloat(rotateY);
            buffer.writeFloat(rotateZ);

            buffer.writeString(path);
            buffer.writeFloat(width);
            buffer.writeFloat(height);
            buffer.writeFloat(alpha);
            buffer.writeBoolean(followPlayer);
            buffer.writeBoolean(glow);
            buffer.writeBoolean(entity != null);
            if (entity != null) {

                buffer.writeUniqueId(entity);
                buffer.writeBoolean(followEntity);
                buffer.writeDouble(x);
                buffer.writeDouble(y);
                buffer.writeDouble(z);
            }
            buffer.writeInt(0);
        });
    }

    public static void setPlayerWorldTexture(Player player, String key, Location location,
                                             float rotateX, float rotateY, float rotateZ, ItemStack itemStack,
                                             float scale, boolean followPlayer, boolean glow,
                                             UUID entity, boolean followEntity, double x, double y, double z) {
        sendPluginMessage(player, 15, buffer -> {
            buffer.writeString(key);
            buffer.writeString(location.getWorld().getName());
            buffer.writeDouble(location.getX());
            buffer.writeDouble(location.getY());
            buffer.writeDouble(location.getZ());
            buffer.writeFloat(rotateX);
            buffer.writeFloat(rotateY);
            buffer.writeFloat(rotateZ);

            buffer.writeItemStack(itemStack);
            buffer.writeFloat(scale);
            buffer.writeBoolean(followPlayer);
            buffer.writeBoolean(glow);
            buffer.writeBoolean(entity != null);
            if (entity != null) {
                buffer.writeUniqueId(entity);
                buffer.writeBoolean(followEntity);
                buffer.writeDouble(x);
                buffer.writeDouble(y);
                buffer.writeDouble(z);
            }
            buffer.writeInt(0);
        });
    }

    public static void setPlayerWorldTexture(Player player, String key, WorldTexture worldTexture) {
        if (worldTexture.itemStack == null) {
            sendPluginMessage(player, 12, buffer -> {
                buffer.writeString(key);
                buffer.writeString(worldTexture.world);
                buffer.writeDouble(worldTexture.translateX);
                buffer.writeDouble(worldTexture.translateY);
                buffer.writeDouble(worldTexture.translateZ);
                buffer.writeFloat(worldTexture.rotateX);
                buffer.writeFloat(worldTexture.rotateY);
                buffer.writeFloat(worldTexture.rotateZ);

                buffer.writeString(worldTexture.path);
                buffer.writeFloat(worldTexture.width);
                buffer.writeFloat(worldTexture.height);
                buffer.writeFloat(worldTexture.alpha);
                buffer.writeBoolean(worldTexture.followPlayerEyes);
                buffer.writeBoolean(worldTexture.glow);
                buffer.writeBoolean(worldTexture.entity != null);
                if (worldTexture.entity != null) {
                    buffer.writeUniqueId(worldTexture.entity);
                    buffer.writeBoolean(worldTexture.followEntityDirection);
                    buffer.writeDouble(worldTexture.translateEntityFront);
                    buffer.writeDouble(0);
                    buffer.writeDouble(worldTexture.translateEntityRight);
                }
                buffer.writeInt(worldTexture.animationList == null ? 0 : worldTexture.animationList.size());
                for (Animation animation : worldTexture.animationList) {
                    buffer.writeString(animation.toData());
                }
            });
        } else {
            sendPluginMessage(player, 15, buffer -> {
                buffer.writeString(key);
                buffer.writeString(worldTexture.world);
                buffer.writeDouble(worldTexture.translateX);
                buffer.writeDouble(worldTexture.translateY);
                buffer.writeDouble(worldTexture.translateZ);
                buffer.writeFloat(worldTexture.rotateX);
                buffer.writeFloat(worldTexture.rotateY);
                buffer.writeFloat(worldTexture.rotateZ);

                buffer.writeItemStack(worldTexture.itemStack);
                buffer.writeFloat(worldTexture.height);
                buffer.writeBoolean(worldTexture.followPlayerEyes);
                buffer.writeBoolean(worldTexture.glow);
                buffer.writeBoolean(worldTexture.entity != null);
                if (worldTexture.entity != null) {
                    buffer.writeUniqueId(worldTexture.entity);
                    buffer.writeBoolean(worldTexture.followEntityDirection);
                    buffer.writeDouble(worldTexture.translateEntityFront);
                    buffer.writeDouble(0);
                    buffer.writeDouble(worldTexture.translateEntityRight);
                }
                buffer.writeInt(worldTexture.animationList == null ? 0 : worldTexture.animationList.size());
                for (Animation animation : worldTexture.animationList) {
                    buffer.writeString(animation.toData());
                }
            });
        }
    }

    public static void removePlayerWorldTexture(Player player, String key) {
        sendPluginMessage(player, 13, buffer -> {
            buffer.writeString(key);
        });
    }

    public static void sendKeyRegister(Player player) {
        sendPluginMessage(player, 14, buffer -> {
            Set<String> set = plugin.getFileManager().getKeyConfig().getKeys(false);
            set.addAll(ConfigFile.registeredKeys);
            buffer.writeInt(set.size());
            for (String s : set) {
                buffer.writeString(s);
            }
        });
    }


    /**
     * 同步客户端的papi变量
     *
     * @param player
     * @param map
     */
    public static void sendSyncPlaceholder(Player player, Map<String, String> map) {
        if (map.size() > 0) {
            sendPluginMessage(player, 7, buffer -> {
                buffer.writeInt(map.size());
                map.forEach((k, v) -> {
                    buffer.writeString(k);
                    buffer.writeString(v);
                });
            });
        }
    }

    public static void sendFinished(Player player) {
        sendPluginMessage(player, 99, buffer -> {
        });
    }

    public static void sendOpenGui(Player player, String guiName) {
        sendRunFunction(player, guiName, "opengui", false);
    }

    public static void sendUpdateGui(Player player, YamlConfiguration yaml) {
        sendPluginMessage(player, 101, buffer -> buffer.writeString(yaml.saveToString()));
    }

    public static void sendOpenHud(Player player, String guiName) {
        sendRunFunction(player, guiName, "openhud", false);
    }

    public static void sendRunFunction(Player player, String guiName, String function, boolean async) {
        sendPluginMessage(player, 100, buffer -> {
            buffer.writeString(guiName);
            buffer.writeString(function);
            buffer.writeBoolean(async);
        });
    }

    public static void putClientSlotItem(Player player, String slotIdentity, ItemStack itemStack) {
        sendPluginMessage(player, 16, buffer -> {
            buffer.writeString(slotIdentity);
            buffer.writeItemStack(itemStack);
        });
    }

    public static void sendPlaySound(Player player, String sound, float volume, float pitch, boolean loop, float x, float y, float z) {
        sendPlaySound(player, UUID.randomUUID().toString(), sound, volume, pitch, loop, x, y, z);
    }

    public static void sendPlaySound(Player player, String key, String sound, float volume, float pitch, boolean loop, float x, float y, float z) {
        sendPluginMessage(player, 19, buffer -> {
            buffer.writeString(key);
            buffer.writeString(sound);
            buffer.writeFloat(volume);
            buffer.writeFloat(pitch);
            buffer.writeBoolean(loop);
            buffer.writeFloat(x);
            buffer.writeFloat(y);
            buffer.writeFloat(z);
        });
    }

    public static void sendStopSound(Player player, String key) {
        sendPluginMessage(player, 20, buffer -> {
            buffer.writeString(key);
        });
    }


    public static void sendCooldown(Player player, long startTime, long endTime, String material, String name, String lore, Map<String, String> nbts) {
        sendPluginMessage(player, 21, buffer -> {
            buffer.writeLong(startTime);
            buffer.writeLong(endTime);
            buffer.writeString(material);
            buffer.writeString(name);
            buffer.writeString(lore);
            buffer.writeInt(nbts.size());
            for (Map.Entry<String, String> entry : nbts.entrySet()) {
                buffer.writeString(entry.getKey());
                buffer.writeString(entry.getValue());
            }
        });
    }

    public static void sendDeleteItemStackCache(Player player, String key, boolean isStartWith) {
        sendPluginMessage(player, 22, buffer -> {
            buffer.writeString(key);
            buffer.writeBoolean(isStartWith);
        });
    }

    public static void sendDeletePlaceholderCache(Player player, String key, boolean isStartWith) {
        sendPluginMessage(player, 23, buffer -> {
            buffer.writeString(key);
            buffer.writeBoolean(isStartWith);
        });
    }

    public static void setThirdPersonView(Player player, int val) {
        sendPluginMessage(player, 24, buffer -> {
            buffer.writeInt(val);
        });
    }

    public static void setWindowTitle(Player player, String title) {
        sendPluginMessage(player, 25, buffer -> {
            buffer.writeString(title);
        });
    }


    public static List<Player> getNearPlayers(Entity entity) {
        return getNearPlayers(entity, 50);
    }

    public static List<Player> getNearPlayers(Entity entity, int range) {
        List<Player> list = entity.getNearbyEntities(range, range, range).stream().filter(e -> e instanceof Player).map(e -> (Player) e).collect(Collectors.toList());
        if (entity instanceof Player) {
            list.add((Player) entity);
        }
        return list;
    }
}
