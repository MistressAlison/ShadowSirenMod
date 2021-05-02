package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.stances.VeilStance;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.actions.watcher.NotStanceCheckAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.combat.EmptyStanceEffect;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class BooSheet extends CustomRelic implements CustomSavable<Integer>, ClickableRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("BooSheet");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BooSheet.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("BooSheet.png"));

    private boolean activated;
    private boolean enteredVeil;
    private int mode;
    private static final int maxModes = 2;
    private int turns;

    public BooSheet() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void setDescriptionAfterLoading() {
        this.description = this.DESCRIPTIONS[mode];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void atBattleStart() {
        activated = false;
        enteredVeil = false;
        turns = 0;
        beginLongPulse();
    }

    @Override
    public void onChangeStance(AbstractStance prevStance, AbstractStance newStance) {
        if (newStance.ID.equals(NeutralStance.STANCE_ID) && !activated) {
            beginLongPulse();
        } else {
            stopPulse();
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if (!activated) {
            activated = true;
            if (AbstractDungeon.player.stance.ID.equals(NeutralStance.STANCE_ID)) {
                flash();
                this.addToBot(new ChangeStanceAction(new VeilStance()));
                this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                enteredVeil = true;
            }
            stopPulse();
        }
        turns++;
    }

    @Override
    public void atTurnStart() {
        if (turns == 1 && mode == 1 && enteredVeil) {
            flash();
            this.addToBot(new NotStanceCheckAction("Neutral", new VFXAction(new EmptyStanceEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.1F)));
            this.addToBot(new ChangeStanceAction("Neutral"));
        }
    }

    @Override
    public Integer onSave() {
        return mode;
    }

    @Override
    public void onLoad(Integer integer) {
        mode = integer;
        setDescriptionAfterLoading();
    }

    @Override
    public void onRightClick() {
        mode++;
        mode %= maxModes;
        setDescriptionAfterLoading();
    }
}
