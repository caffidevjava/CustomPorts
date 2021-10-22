package caffidev.gachicraft;

import net.minecraft.client.Minecraft;

import java.util.Map;

public class Utils {
    public boolean isObfuscated;
    /** Gets player's gamemode.
     * Can be survival, creative, spectator xor adventure.
     * @return Gamemode
     */
    public static String getCurrentGameMode(){
        Minecraft mc = Gachicraft.INSTANCE.minecraftPointer;
        if (mc.player.isCreative()) return "Creative";
        else if (mc.player.isSpectator()) return "Spectator";
        //todo: I haven't found a way to know a difference between Survival
        //todo: and Adventure mode.
        else return "Survival";
    }


    /**
     * Gets integer gamemode by its string equivalent.
     * @param gamemode
     * @return -1 if not successful.
     */
    public static int getIntGameMode(String gamemode){
        switch (gamemode){
            case "Creative":
                return 0;
            case "Survival":
                return 1;
            case "Adventure":
                return 2;
            case "Spectator":
                return 3;
            default:
                return -1;
        }
    }
}
