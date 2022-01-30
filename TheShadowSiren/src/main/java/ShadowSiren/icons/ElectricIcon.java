package ShadowSiren.icons;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.util.TextureLoader;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;

public class ElectricIcon extends AbstractCustomIcon {
    public static final String ID = ShadowSirenMod.makeID("Electric");
    private static ElectricIcon singleton;

    public ElectricIcon() {
        super(ID, TextureLoader.getTexture("ShadowSirenResources/images/icons/Electric.png"));
    }

    public static ElectricIcon get()
    {
        if (singleton == null) {
            singleton = new ElectricIcon();
        }
        return singleton;
    }
}
