package ShadowSiren.icons;

import IconsAddon.icons.AbstractCustomIcon;
import IconsAddon.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;

public class HatIcon extends AbstractCustomIcon {

    private static HatIcon singleton;

    public static HatIcon get()
    {
        if (singleton == null) {
            singleton = new HatIcon();
        }
        return singleton;
    }

    @Override
    public String name() {
        return "Hat";
    }

    @Override
    public Texture getTexture() {
        return TextureLoader.getTexture("ShadowSirenResources/images/icons/Hat.png");
    }

}
