package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.stances.HyperStance;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class SuperSheet extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("SuperSheet");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("SuperSheet.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("SuperSheet.png"));

    public SuperSheet() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override //Should replace default relic. Thanks kiooeht#3584 10/25/2020, #modding-technical
    public void obtain() {
        //Grab the player
        AbstractPlayer p = AbstractDungeon.player;
        //If we have the starter relic...
        if (p.hasRelic(BooSheet.ID)) {
            //Find it...
            for (int i = 0; i < p.relics.size(); ++i) {
                if (p.relics.get(i).relicId.equals(BooSheet.ID)) {
                    //Replace it
                    instantObtain(p, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    //Only spawn if we have Boo Sheet
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(BooSheet.ID);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        flash();
        this.addToBot(new ChangeStanceAction(new HyperStance()));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }
}
