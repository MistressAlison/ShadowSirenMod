package ShadowSiren.icons;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.util.TextureLoader;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;

public class DarkIcon extends AbstractCustomIcon {
    public static final String ID = ShadowSirenMod.makeID("Dark");
    private static DarkIcon singleton;

    public DarkIcon() {
        super(ID, TextureLoader.getTexture("ShadowSirenResources/images/icons/Dark.png"));
    }

    public static DarkIcon get()
    {
        if (singleton == null) {
            singleton = new DarkIcon();
        }
        return singleton;
    }
}
