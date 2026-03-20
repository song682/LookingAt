package jp.kaiz.lookingat;

import com.mojang.realmsclient.gui.ChatFormatting;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import java.util.List;

@Mod(
        name = Tags.MOD_NAME,
        modid = Tags.MOD_ID,
        version = Tags.VERSION,
        useMetadata = true,
        acceptableRemoteVersions = Tags.MC_VERSION,
        acceptableSaveVersions = Tags.MC_VERSION,
        acceptedMinecraftVersions = Tags.MC_VERSION
)
public class LookingAt {

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        if (event.getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Text event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings.showDebugInfo) {
            List<String> right = event.right;
            List<String> left = event.left;
            String osName = System.getProperty("os.name");
            String osVersion = System.getProperty("os.version");
            String osArch = System.getProperty("os.arch");
            String CPUInfo = System.getenv("PROCESSOR_IDENTIFIER");
            String GPUInfo = GL11.glGetString(GL11.GL_RENDERER);
            String OpenGLVer = GL11.glGetString(GL11.GL_VERSION);
            int cores = Runtime.getRuntime().availableProcessors();
            right.add("Java: " + System.getProperty("java.version"));
            right.add("CPU: " + cores + CPUInfo);
            right.add(GPUInfo);
            right.add("OpenGL " + OpenGLVer);
            right.add("OS: " + osName + " " + osVersion + " (" + osArch + ")");

            MovingObjectPosition mouseOver = mc.objectMouseOver;
            if (mouseOver == null) {
                return;
            }

            switch (mouseOver.typeOfHit) {
                case BLOCK:
                    right.add("");
                    right.add(ChatFormatting.UNDERLINE + String.format("Targeted Block: %s %s %s", mouseOver.blockX, mouseOver.blockY, mouseOver.blockZ));
                    Block block = mc.theWorld.getBlock(mouseOver.blockX, mouseOver.blockY, mouseOver.blockZ);
                    right.add(Block.blockRegistry.getNameForObject(block));
                    break;
                case ENTITY:
                    Entity entity = mouseOver.entityHit;
                    if (entity != null) {
                        right.add(ChatFormatting.UNDERLINE + "Targeted Entity");
                        right.add(EntityList.getEntityString(entity));
                    }
            }
        }
    }
}