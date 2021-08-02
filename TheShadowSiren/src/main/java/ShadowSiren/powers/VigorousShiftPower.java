package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.interfaces.OnChangeElementPower;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.stances.AbstractStance;

public class VigorousShiftPower extends AbstractPower implements CloneablePowerInterface, OnChangeElementPower {

    public static final String POWER_ID = ShadowSirenMod.makeID("VigorousShiftPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public VigorousShiftPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("attackBurn");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    /*
    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        super.onChangeStance(oldStance, newStance);
        if (!oldStance.ID.equals(newStance.ID)) {
            flash();
            this.addToTop(new ApplyPowerAction(owner, owner, new VigorPower(owner, amount)));
        }
    }*/

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new VigorousShiftPower(owner, amount);
    }

    @Override
    public void onChangeElement() {
        this.addToBot(new ApplyPowerAction(owner, owner, new VigorPower(owner, amount)));
    }
}
