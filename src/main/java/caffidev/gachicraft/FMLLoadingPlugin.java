package caffidev.gachicraft;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

//Thanks to https://github.com/18107/Default-LAN-Port/blob/master/src/main/java/core/defaultlanport/coretransform/CoreLoader.java
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions(value = "caffidev.gachicraft.")
@IFMLLoadingPlugin.Name(Gachicraft.MOD_NAME)
@IFMLLoadingPlugin.SortingIndex(value = 999)
public class FMLLoadingPlugin implements IFMLLoadingPlugin {
    public static boolean isObfuscated;

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{CoreTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        isObfuscated = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
