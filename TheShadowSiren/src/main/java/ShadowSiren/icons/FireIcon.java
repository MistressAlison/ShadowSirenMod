package ShadowSiren.icons;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.util.TextureLoader;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;

public class FireIcon extends AbstractCustomIcon {
    public static final String ID = ShadowSirenMod.makeID("Fire");
    private static FireIcon singleton;

    public FireIcon() {
        super(ID, TextureLoader.getTexture("ShadowSirenResources/images/icons/Fire.png"));
    }

    public static FireIcon get()
    {
        if (singleton == null) {
            singleton = new FireIcon();
        }
        return singleton;
    }
}
