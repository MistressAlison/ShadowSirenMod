package ShadowSiren.icons;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.util.TextureLoader;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;

public class IceIcon extends AbstractCustomIcon {
    public static final String ID = ShadowSirenMod.makeID("Ice");
    private static IceIcon singleton;

    public IceIcon() {
        super(ID, TextureLoader.getTexture("ShadowSirenResources/images/icons/Ice.png"));
    }

    public static IceIcon get()
    {
        if (singleton == null) {
            singleton = new IceIcon();
        }
        return singleton;
    }
}
