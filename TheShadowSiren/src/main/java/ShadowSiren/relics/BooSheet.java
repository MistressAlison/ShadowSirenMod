package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.stances.VeilStance;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class BooSheet extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("BooSheet");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BooSheet.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("BooSheet.png"));

    public BooSheet() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        flash();
        this.addToBot(new ChangeStanceAction(new VeilStance()));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }
}
