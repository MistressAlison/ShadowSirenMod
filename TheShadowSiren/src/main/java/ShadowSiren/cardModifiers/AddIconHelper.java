package ShadowSiren.cardModifiers;

import IconsAddon.cardmods.AddIconToDescriptionMod;
import IconsAddon.icons.DarkIcon;
import IconsAddon.icons.ElectricIcon;
import IconsAddon.icons.FireIcon;
import IconsAddon.icons.IceIcon;

public class AddIconHelper {

    public static class AddFireIconMod extends AddIconToDescriptionMod {
        public AddFireIconMod(String searchString) {
            super(searchString, FireIcon.get());
            this.priority = 1;
        }
    }

    public static class AddIceIconMod extends AddIconToDescriptionMod {
        public AddIceIconMod(String searchString) {
            super(searchString, IceIcon.get());
            this.priority = 2;
        }
    }

    public static class AddElectricIconMod extends AddIconToDescriptionMod {
        public AddElectricIconMod(String searchString) {
            super(searchString, ElectricIcon.get());
            this.priority = 3;
        }
    }

    public static class AddShadowIconMod extends AddIconToDescriptionMod {
        public AddShadowIconMod(String searchString) {
            super(searchString, DarkIcon.get());
            this.priority = 4;
        }
    }

}
