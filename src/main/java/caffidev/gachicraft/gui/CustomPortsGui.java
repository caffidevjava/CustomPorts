package caffidev.gachicraft.gui;

import caffidev.gachicraft.Gachicraft;
import caffidev.gachicraft.Utils;
import caffidev.gachicraft.upnp.UPnP;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldSettings;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class CustomPortsGui extends GuiScreen {
    private GuiTextField portTextbox;
    private boolean portOpened;
    private boolean checked;
    private boolean allowCheats;
    private boolean doUPnP;
    //Bug - It double clicks when clicking for a first time
    private boolean uPnPBug = false;
    private boolean cheatsBug = false;
    private boolean portBug = false;
    private boolean gameModeBug = false;

    private String defaultGamemode;
    private String message;
    private Future<Byte> informationState; //it only refreshes once, but you can manually refresh it

    private void deleteButton(GuiButton button) {
        for (int i = 0; i < this.buttonList.size(); i++) {
            if (this.buttonList.get(i).id == button.id) {
                this.buttonList.remove(i);
            }
        }
    }

    /**
     * Id = 2
     */
    private void addPortButton(){
        Gachicraft.logger.debug("Changed portOpened to: "+portOpened);
        if(portOpened) {
            this.buttonList.add(new GuiButton(2, 5 + width / 2, height/2 + 60, width /2 - 10, 20, "§cClose"));
        } else{
            this.buttonList.add(new GuiButton(2, 5 + width / 2, height/2 + 60, width /2 - 10, 20, "§aOpen"));
        }
    }

    /**
     * Id = 6
     */
    private void addGamemodeButton() {
        Gachicraft.logger.debug("Default gamemode: "+defaultGamemode);
        this.buttonList.add(new GuiButton(6, 5 + width / 2, height / 2 - 29, width / 2 - 10, 20, "Gamemode: "+defaultGamemode));
    }

    /**
     * Id = 7
     */
    private void addUPnPButton(){
        Gachicraft.logger.debug("Changed doUPnP to "+doUPnP);
        if(doUPnP) {
            this.buttonList.add(new GuiButton(7, 5 , height/2 + 20, width /2 -10 , 20, "UPnP: ON"));
        } else{
            this.buttonList.add(new GuiButton(7, 5 , height/2 + 20, width /2 -10 , 20, "UPnP: OFF"));
        }
    }

    /**
     * Id = 8
     */
    private void addCheatsButton(){
        Gachicraft.logger.debug("Changed allowCheats to "+allowCheats);
        if(allowCheats) {
            this.buttonList.add(new GuiButton(8, 5 + width / 2 , height/2 + 20, width /2 -10 , 20, "Cheats: ON"));
        } else{
            this.buttonList.add(new GuiButton(8, 5 + width / 2 , height/2 + 20, width /2 -10 , 20, "Cheats: OFF"));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
        for (int keyCodeAllowed = Keyboard.KEY_1; keyCodeAllowed <= Keyboard.KEY_0; keyCodeAllowed++){
            if(keyCode == keyCodeAllowed){
                portTextbox.textboxKeyTyped(typedChar, keyCode);
                super.keyTyped(typedChar, keyCode);
            }
        }
        if(keyCode == Keyboard.KEY_BACK){
            portTextbox.textboxKeyTyped(typedChar, keyCode);
            super.keyTyped(typedChar, keyCode);
        }
    }

    /** Initialization of GUI */
    @Override
    public void initGui(){
        // Add basic buttons
        addPortButton();
        this.buttonList.add(new GuiButton(1, 5, height/2 + 60, width /2 - 10, 20, "Refresh (WIP)"));
        this.buttonList.add(new GuiButton(3, 5, height/2 + 80, width /2 - 10, 20, "Copy port"));
        this.buttonList.add(new GuiButton(4, 5 + width / 2 , height/2 + 80, width /2 -10 , 20, "Cancel"));

        // Main part
        portTextbox = new GuiTextField(5,this.fontRenderer, 5, height /2 -29 , width /2 -10,20);
        portTextbox.setFocused(true);
        portTextbox.setCanLoseFocus(true);
        Keyboard.enableRepeatEvents(true);

        //Default values
        portTextbox.setText("7777");
    }

    /** Game will be paused if server was not opened */
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    public void updateScreen(){
        //With this speed, he blinks like a Flash
        portTextbox.updateCursorCounter();

        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){

        this.drawDefaultBackground();
        super.drawScreen(mouseX,mouseY,partialTicks);
        this.drawCenteredString(this.fontRenderer, I18n.format("lanServer.title"), this.width / 2, 50, 16777215);

        if(informationState.isDone()) {
            if(!checked) {
                message = "Information from your network was retrieved.";

                // Add additional buttons
                addGamemodeButton();
                addUPnPButton();
                addCheatsButton();
                checked = true;
            }
            this.drawString(this.fontRenderer, "Internal IP: " + UPnP.getLocalIP(), 10, 10, 16777215);
            this.drawString(this.fontRenderer, "External IP: " + UPnP.getExternalIP(), 10, 20, 16777215);
            this.drawString(this.fontRenderer, "IP address of router: " + UPnP.getDefaultGatewayIP(), width / 2, 10, 16777215);
            this.drawString(this.fontRenderer, "Is UPnP available: " + UPnP.isUPnPAvailable(), width / 2, 20, 16777215);

            // Main info
            this.drawString(this.fontRenderer, "Port:", 10, 80, 16777215);
            this.drawString(this.fontRenderer, "Default gamemode:", width / 2, 80, 16777215);
            this.drawString(this.fontRenderer, "Automatically forward ports:", 10, 130, 16777215);
            this.drawString(this.fontRenderer, "Allow cheats: ", width / 2, 130, 16777215);
            // ------
            portTextbox.drawTextBox();
            this.drawHorizontalLine(5,width - 9, 13, 0xffffff);
        } else
        {
            this.drawCenteredString(this.fontRenderer, "Started gathering info ...", width/2, 15, 16777215);
        }
        this.drawString(this.fontRenderer, "* "+ message, 10, height - 12, 0xFF0000);
        this.drawString(this.fontRenderer, "© caffidev", width - 60, height - 12, 16777215);
        this.updateScreen();
    }


    /**
     * Answers for buttons logic
     * @param button
     * @throws IOException
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException{
        switch (button.id) {
            case 1:
                startGathering();
                message = "Started gathering info again...";
                break;
            case 2:
                deleteButton(button);

                if (portOpened) portOpened = false;
                else {
                    portOpened = true;
                    @Nullable String result = this.mc.getIntegratedServer().shareToLAN(WorldSettings.getGameTypeById(Utils.getIntGameMode(defaultGamemode)), allowCheats);
                    if (result != null) message = "Successfully started.";
                    else message = "Failed to start.";
                }

                addPortButton();
                break;
            case 3:
                if (Gachicraft.INSTANCE.port == -1) {
                    message = "Couldn't copy the port.";
                    break;
                }
                // Port is available
                setClipboardString(Gachicraft.INSTANCE.port.toString());
                message = "Successfully copied port!";
                break;
            case 4:
                this.mc.displayGuiScreen(new GuiIngameMenu());
                break;
            case 5:
                if (portTextbox.getText().isEmpty()) Gachicraft.INSTANCE.port = -1;
                else {
                    try {
                        Gachicraft.INSTANCE.port = Integer.parseInt(portTextbox.getText());
                    } catch (NumberFormatException e) {
                        message = "Port can't contain character elements in it";
                        Gachicraft.INSTANCE.port = -1;
                    }
                }
                break;
            case 6:
                deleteButton(button);

                String prevGamemode = defaultGamemode;
                switch (prevGamemode) {
                    case "Creative":
                        defaultGamemode = "Survival";
                        break;
                    case "Survival":
                        defaultGamemode = "Adventure";
                        break;
                    case "Adventure":
                        defaultGamemode = "Creative";
                        break;
                }
                addGamemodeButton();
                break;
            case 7:
                deleteButton(button);

                if (doUPnP) doUPnP = false;
                else {
                    doUPnP = true;
                }
                addUPnPButton();
                break;
            case 8:
                deleteButton(button);

                if (allowCheats) allowCheats = false;
                else {
                    allowCheats = true;
                }
                addCheatsButton();
                break;
        }

        super.actionPerformed(button);
    }

    /** Default constructor */
    public CustomPortsGui() {
        startGathering();

        defaultGamemode = Utils.getCurrentGameMode();
        //Default values for more convenient work
        if (defaultGamemode == "Spectator") {
            defaultGamemode = "Survival";
            this.mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString("Due to mine" +
                    "craft capabilities we can't set a spectator mode as standard. "));
        }
        portOpened = false;
        message = "Message";
        Gachicraft.INSTANCE.port = 7777;
        allowCheats = true;
    }

    private void startGathering(){
        //We forget about previous information
        checked = false;
        //Starting gathering needed info...
        ExecutorService threadpool = Executors.newCachedThreadPool();
        UPnP.FindGW(); // there is a problem
        informationState = threadpool.submit(UPnP::waitInit);
    }
}
