package net.sakuragame.eternal.dragoncore.api;

import net.sakuragame.eternal.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ModelAPI {
    /**
     * 设置实体模型
     *
     * @param player 发包给哪个玩家(只有这个玩家能看到变化)
     * @param uuid   设置哪个实体
     * @param name   模型名(该名字为EntityModel.yml的entity节点)
     */
    public static void setEntityModel(Player player, UUID uuid, String name) {
        PacketSender.setEntityModel(player, uuid, name);
    }

    public static void removeEntityModel(Player player, UUID uuid) {
        PacketSender.setEntityModel(player, uuid, null);
    }

    /**
     * 同上，不过会发包给每个玩家
     *
     * @param uuid
     * @param name
     */
    public static void setEntityModel(UUID uuid, String name) {
        Bukkit.getOnlinePlayers().forEach(player -> PacketSender.setEntityModel(player, uuid, name));
    }

    public static void removeEntityModel(UUID uuid) {
        Bukkit.getOnlinePlayers().forEach(player -> PacketSender.setEntityModel(player, uuid, null));
    }

    /**
     * 设置实体动画
     *
     * @param entity         实体
     * @param animation      动画名
     * @param transitionTime 过渡时间(不要太长，200以下，甚至是0)
     */
    public static void setEntityAnimation(LivingEntity entity, String animation, int transitionTime) {
        PacketSender.setModelEntityAnimation(entity, animation, transitionTime);
    }

    public static void removeEntityAnimation(LivingEntity entity, String animation, int transitionTime) {
        PacketSender.removeModelEntityAnimation(entity, animation, transitionTime);
    }
}

