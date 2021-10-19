package eos.moe.dragoncore.command.sub;

import com.google.common.collect.ImmutableList;
import eos.moe.dragoncore.DragonCore;
import eos.moe.dragoncore.api.CooldownAPI;
import eos.moe.dragoncore.api.CoreAPI;
import eos.moe.dragoncore.api.easygui.EasyScreen;
import eos.moe.dragoncore.api.easygui.component.*;
import eos.moe.dragoncore.api.easygui.component.listener.ClickListener;
import eos.moe.dragoncore.api.worldtexture.WorldTexture;
import eos.moe.dragoncore.api.worldtexture.animation.RotateAnimation;
import eos.moe.dragoncore.api.worldtexture.animation.TranslateAnimation;
import eos.moe.dragoncore.command.CommandBase;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TestCommand extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        Player player = Bukkit.getOnlinePlayers().stream().findFirst().get();
        EasyScreen openedScreen = EasyScreen.getOpenedScreen(player);
        openedScreen.addComponent(new EasyButton(10, 10, 120, 120, "test.png"));

        openedScreen.updateGui(player);
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        player.sendMessage("这不是打开了");
        opGuiButton(player);
        if (true) return;
        //ArrayList<Animation> animations = new ArrayList<>();
        //RotateAnimation z = RotateAnimation.builder().angle(360).cycleCount(-1).delay(0).direction("z").duration(3000).fixed(false).resetTime(0).build();

        //animations.add(z);

        RotateAnimation z = new RotateAnimation();
        // 旋转角度
        z.angle = 360;
        // 循环次数，-1为永久循环
        z.cycleCount = -1;
        // 旋转方向
        z.direction = "z";
        // 3000毫秒内从0°转到angle°
        z.duration = 3000;


        TranslateAnimation t = new TranslateAnimation();
        t.cycleCount = 1;
        // 往下移动，因为上面旋转了方向所以这里得改成z，或者你先build.animationList.add(t)再add(z)
        t.direction = "z";
        t.duration = 3000;
        // 播放结束后是否固定在结束的位置
        t.fixed = true;
        // 移动距离
        t.distance = 3;


        WorldTexture build = new WorldTexture();
        build.entity = player.getUniqueId();
        build.width = 3;
        build.height = 3;
        build.path = "a.png";
        build.rotateX = 90;
        build.translateY = 3;
        build.animationList.add(z);
        build.animationList.add(t);
        CoreAPI.setPlayerWorldTextureItem(player, "芜湖", build);

        player.sendMessage("发送了");
        if (true) return;
        opGuiButton(player);

        if (true) return;
        PacketSender.sendDeleteItemStackCache(player, "额外", true);
        PacketSender.sendDeletePlaceholderCache(player, "player_", true);

        if (true) return;
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        CooldownAPI.setPlayerCooldown(player, itemInMainHand, 10000);
        if (true) return;
        EasyScreen screen = new EasyScreen("a.png", 400, 250);
        screen.addComponent(new EasyButton(5, 5, 10, 10, "按钮框.png", "inventory.png") {
            @Override
            public void onClick(Player player, ClickListener.Type type) {
                player.sendMessage("你点击了喵喵" + type.name());
            }
        });
        screen.addComponent("文本", new EasyLabel(5, 15, 1, ImmutableList.of("§c哇哦", "§c牛逼")));
        EasyTextField textField = new EasyTextField(5, 35, 50, "秒了个米的");
        screen.addComponent("编辑框", textField);
        screen.addComponent(new EasySlot(5, 70, new ItemStack(Material.CAKE)));
        screen.addComponent(new EasyEntityView(40, 20, player.getUniqueId()));
        screen.addComponent(new EasyImage(60, 0, 50, 50, "按钮框.png"));
        EasyScrollingList list = new EasyScrollingList(100, 30, 196, 212, "0,102,255,255");
        list.setBar(10, 28, 500, "b.png");
        list.addComponent(new EasyLabel(5, 185, ImmutableList.of("哇哦", "牛逼")));
        screen.addComponent(list);

        screen.openGui(player);
        Bukkit.getScheduler().runTaskLater(DragonCore.getInstance(), () -> {
            player.sendMessage("5秒过去了,当前文本框的内容为" + textField.getText());
        }, 100);
    }

    public static void opGuiButton(Player player) {
        EasyScreen screen = new EasyScreen("tip/main.png", 20, 20);
        screen.addComponent(new EasyButton(145, 100, 50, 50, "tip/p1.png", "tip/p2.png") {
            @Override
            public void onClick(final Player player, final ClickListener.Type type) {
                player.sendMessage("打开了新的gui");
                //新的gui
                EasyScreen screen = new EasyScreen("tip/main.png", 20, 20);
                screen.addComponent(new EasyButton(145, 100, 50, 50, "tip/p1.png", "tip/p2.png") {
                    @Override
                    public void onClick(final Player player, final ClickListener.Type type) {
                        player.sendMessage("点到我了");
                    }
                });
                screen.openGui(player);
            }
        });
        screen.openGui(player);
    }

    @Override
    public String getPermission() {
        return "core.command.test";
    }

    @Override
    public String getCommandDesc() {
        return "/core test";
    }
}
