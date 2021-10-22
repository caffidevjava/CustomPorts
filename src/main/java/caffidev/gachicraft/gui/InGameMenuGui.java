package caffidev.gachicraft.gui;

import net.minecraft.client.gui.GuiIngameMenu;

public class InGameMenuGui extends GuiIngameMenu {
    @Override
    public void initGui() {
        super.initGui();
        //Making our button always active
        //I don't know how will it live with other mods
        for (int i = 0; i < this.buttonList.size(); i++) {
            if (this.buttonList.get(i).id == 7) {
                this.buttonList.get(i).enabled = true;
            }
        }
    }
}
