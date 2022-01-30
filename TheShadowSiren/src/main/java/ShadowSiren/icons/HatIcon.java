package ShadowSiren.icons;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.util.TextureLoader;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;

public class HatIcon extends AbstractCustomIcon {
    public static final String ID = ShadowSirenMod.makeID("Hat");
    private static HatIcon singleton;

    public HatIcon() {
        super(ID, TextureLoader.getTexture("ShadowSirenResources/images/icons/Hat.png"));
    }

    public static HatIcon get()
    {
        if (singleton == null) {
            singleton = new HatIcon();
        }
        return singleton;
    }
}
