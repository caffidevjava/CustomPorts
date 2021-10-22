package caffidev.gachicraft;

import caffidev.gachicraft.gui.CustomPortsGui;
import caffidev.gachicraft.upnp.UPnP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//Currently known bugs:
//1. white thingy when changing gui scale
//2. button double clicks first time
//3. Bad gui scaling of textfields and buttons
@Mod(
        modid = Gachicraft.MOD_ID,
        name = Gachicraft.MOD_NAME,
        version = Gachicraft.VERSION,
        clientSideOnly = true //works only for a integrated server
)
public class Gachicraft {
    public Minecraft minecraftPointer = Minecraft.getMinecraft();
    public Integer port;

    public static final String MOD_ID = "gachicraft";
    public static final String MOD_NAME = "Custom Ports";
    public static final String VERSION = "1.0.0.0";
    public static Logger logger = LogManager.getLogger(MOD_ID);
    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static Gachicraft INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {

    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }

    /** When server stops */
    @Mod.EventHandler
    public static void onStopping(FMLServerStoppingEvent event){
        if (UPnP.isMappedTCP(INSTANCE.port)) UPnP.closePortTCP(INSTANCE.port);
        logger.warn("You forgot to close your port " + INSTANCE.port + ". I closed it for you.");
    }

    /**
     * This is a special class that listens to registry events, to allow creation of mod blocks and items at the proper time.
     */
    @Mod.EventBusSubscriber
    public static class EventsHandler {
        @SubscribeEvent
        public static void onGui(GuiOpenEvent event){
            if(event.getGui() instanceof GuiShareToLan) {
                event.setGui(new CustomPortsGui());
            }
        }

        @SubscribeEvent
        public void onGuiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
            if (event.getGui() instanceof GuiIngameMenu) {
                GuiIngameMenu gui = ((GuiIngameMenu) event.getGui());
                //через event.buttonList можно получить список кнопок после инициализации гуи
                //А дальше можно сделать что угодно, например так
                for (int i = 0; i < event.getButtonList().size(); i++) {
                    if (event.getButtonList().get(i).id == 7) {
                        event.getButtonList().get(i).enabled = true;
                    }
                }
            }
        }

}}
