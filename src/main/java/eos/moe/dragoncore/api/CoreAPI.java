package eos.moe.dragoncore.api;

import eos.moe.dragoncore.api.worldtexture.WorldTexture;
import eos.moe.dragoncore.config.Config;
import eos.moe.dragoncore.network.PacketSender;
import eos.moe.dragoncore.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

public class CoreAPI {

    /**
     * 跟下面一样嗷，就是少了uuid开始的那5个
     */
    public static void setPlayerWorldTexture(Player player, String key, Location location,
                                             float rotateX, float rotateY, float rotateZ, String path,
                                             float width, float height, float alpha, boolean follow, boolean glow
    ) {
        PacketSender.setPlayerWorldTexture(player, key, location, rotateX, rotateY, rotateZ, path, width, height, alpha, follow, glow, null, false, 0, 0, 0);
    }

    /**
     * @param player       发送数据包给哪个玩家
     * @param key          节点名，使用同样的节点名发送会覆盖上一个
     * @param location     世界,偏移坐标xyz
     * @param rotateX      旋转角
     * @param rotateY      旋转角
     * @param rotateZ      旋转角
     * @param path         图片地址(当存在前缀[text]则为显示文字，文字的高度与height设置有关，width则无用)
     * @param width        显示的宽度(1=1个方块的宽度)
     * @param height       显示的高度(1=1个方块的高度)
     * @param alpha        透明度(0-1区间)
     * @param followPlayer 是否跟随玩家的视角自动旋转(上面的旋转角将作为额外旋转)
     * @param glow         是否发光(开光影+发光会导致图片变奇怪)
     * @param entity       是否绑定实体位置，绑定后，上面的location将作为额外偏移坐标
     * @param followEntity 图片的方向是否跟随实体的身体方向，与followPlayer冲突，不可同时为true
     * @param x            额外偏移,该偏移绑定了实体的方向，x为左右
     * @param y            额外偏移
     * @param z            额外偏移,该偏移绑定了实体的方向，z为前后
     *                     比如(0,1.5,1)则是固定显示在实体脚部往上1.5格，实体面前1格的位置
     */
    public static void setPlayerWorldTexture(Player player, String key, Location location,
                                             float rotateX, float rotateY, float rotateZ, String path,
                                             float width, float height, float alpha, boolean followPlayer, boolean glow,
                                             UUID entity, boolean followEntity, float x, float y, float z
    ) {
        PacketSender.setPlayerWorldTexture(player, key, location, rotateX, rotateY, rotateZ, path, width, height, alpha, followPlayer, glow, entity, followEntity, x, y, z);
    }

    /**
     * 跟上面差不多嗷，就是path换成了itemstack
     * 要注意的是，物品的形状方向可能千奇百怪，因此需要画正的话得调整角度(比如钻石剑都指着右上方，有些物品模型里指着正上方)
     *
     * @param itemStack 渲染的物品
     * @param scale     缩放大小
     */
    public static void setPlayerWorldTextureItem(Player player, String key, Location location,
                                                 float rotateX, float rotateY, float rotateZ, ItemStack itemStack,
                                                 float scale, boolean followPlayer) {
        Objects.requireNonNull(itemStack);
        PacketSender.setPlayerWorldTexture(player, key, location, rotateX, rotateY, rotateZ, itemStack, scale, followPlayer, false, null, false, 0, 0, 0);
    }

    /**
     * 跟上面setPlayerWorldTexture的差不多嗷
     */
    public static void setPlayerWorldTextureItem(Player player, String key, Location location,
                                                 float rotateX, float rotateY, float rotateZ, ItemStack itemStack,
                                                 float scale, boolean followPlayer,
                                                 UUID entity, boolean followEntity, double x, double y, double z) {
        Objects.requireNonNull(itemStack);
        PacketSender.setPlayerWorldTexture(player, key, location, rotateX, rotateY, rotateZ, itemStack, scale, followPlayer, false, entity, followEntity, x, y, z);
    }

    public static void setPlayerWorldTextureItem(Player player, String key, WorldTexture worldTexture) {
        Objects.requireNonNull(worldTexture);
        PacketSender.setPlayerWorldTexture(player, key, worldTexture);
    }

    /**
     * 移除某个玩家的贴图
     *
     * @param player
     * @param key
     */
    public static void removePlayerWorldTexture(Player player, String key) {
        PacketSender.removePlayerWorldTexture(player, key);
    }

    /**
     * 注册按键，按键名参考教程，只有注册了的按键才会在KeyPressEvent中触发(或者KeyConfig.yml里写的也行)
     *
     * @param key
     */
    public static void registerKey(String key) {
        Config.registeredKeys.add(key);
        Bukkit.getOnlinePlayers().forEach(PacketSender::sendKeyRegister);
    }

    public static void unregisterKey(String key) {
        Config.registeredKeys.remove(key);
        Bukkit.getOnlinePlayers().forEach(PacketSender::sendKeyRegister);
    }

    /**
     * 当玩家聊天栏打开着的时候，可以设置聊天栏的字(可以与ChatTextBoxEvent一同使用)
     *
     * @param player
     * @param text
     */
    public static void setPlayerChatBox(Player player, String text) {
        PacketSender.setChatBoxText(player, text);
    }

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

    public static String parseGradientText(String text) {
        return ColorUtil.parseColor(text);
    }

}
