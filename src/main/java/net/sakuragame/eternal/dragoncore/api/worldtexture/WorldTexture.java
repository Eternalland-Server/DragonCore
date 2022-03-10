package net.sakuragame.eternal.dragoncore.api.worldtexture;

import lombok.AllArgsConstructor;
import net.sakuragame.eternal.dragoncore.api.worldtexture.animation.Animation;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class WorldTexture {
    // 世界名，只有玩家在该世界才能看到[ 为*则所有世界均能看到 ]
    public String world = "*";
    // 显示的坐标[ 注意entity不为空时的变化 ]
    public double translateX = 0;
    public double translateY = 0;
    public double translateZ = 0;
    // 旋转角[ 先旋转的是Y哦 ] [ 注意followPlayerEye为true时的变化 ]
    public float rotateY = 0;
    public float rotateX = 0;
    public float rotateZ = 0;

    // 贴图路径[ 当前缀为[text]时,则为渲染文字 ]
    public String path = "unknown.png";
    // 渲染物品[ 当itemStack不为null时, 将绘制物品而不绘制path路径的贴图 ]
    public ItemStack itemStack;

    // 图片显示的宽高[ 当渲染文字时,仅height生效,作为文字的高度使用 ][ 当渲染物品时,仅height生效,作为物品的缩放大小使用 ]
    public float width = 1;
    public float height = 1;
    // 图片透明度[ 渲染文字时无效 ]
    public float alpha = 1;
    // 增加光照处理,晚上时图片也存在光照,可以看得清[ 开光影时图片可能会看不清 ]
    public boolean glow = false;
    // 旋转角是否自动跟随客户端玩家视角[ 上方旋转角将作为额外旋转 ] [ 与followEntityDirection冲突 ]
    public boolean followPlayerEyes = false;

    // 绑定实体[ 此时上方的translate将作为额外位移 ] [ 下方三个参数均为绑定实体后才可使用的参数 ]
    public UUID entity = null;
    // 旋转角是否自动跟随实体身体视角[ 上方旋转角将作为额外旋转 ] [ 与followPlayerEyes冲突 ]
    public boolean followEntityDirection = false;
    // 坐标前后位移[ >0为前, <0为后],例如为1,则始终处于玩家面前1m的位置
    public double translateEntityFront = 0;
    // 坐标左右位移[ >0为右, <0为左],例如为1,则始终处于玩家右边1m的位置
    public double translateEntityRight = 0;

    // 旋转,缩放,平移动画[RotateAnimation,ScaleAnimation,TranslateAnimation]
    public List<Animation> animationList = new ArrayList<>();
}
