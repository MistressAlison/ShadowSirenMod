package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.util.GameSpeedController;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.helpers.PowerTip;

import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class Chronometer extends CustomRelic implements GameSpeedController.GameSpeedModifyingObject, ClickableRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("Chronometer");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("StopWatch.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("StopWatch.png"));

    private int state = 2;
    private static final HashMap<Integer, Float> stateMap = new HashMap<>();

    static {
        stateMap.put(0, 0.25f);
        stateMap.put(1, 0.5f);
        stateMap.put(2, 1f);
        stateMap.put(3, 2f);
        stateMap.put(4, 4f);
    }

    public Chronometer() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.MAGICAL);
        setDescriptionAfterLoading();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + stateMap.get(state) + DESCRIPTIONS[1];
    }

    public void setDescriptionAfterLoading() {
        this.description = DESCRIPTIONS[0] + stateMap.get(state) + DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public float getGameSpeedDivisor() {
        return 1f/stateMap.get(state);
    }

    @Override
    public void onRightClick() {
        ++state;
        state %= stateMap.size();
        setDescriptionAfterLoading();
    }
}
