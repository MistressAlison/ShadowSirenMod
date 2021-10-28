package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.*;

public class CreepingShadowsPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = ShadowSirenMod.makeID("CreepingShadowsPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int activations = 0;

    private final Color whyIsGreenColorPrivate = new Color(0.0F, 1.0F, 0.0F, 1.0F);

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public CreepingShadowsPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("darkembrace");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (activations < amount) {
            if (card.type != AbstractCard.CardType.POWER) {
                activations++;
                this.flash();
                action.reboundCard = true;
            }
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        if (this.amount - this.activations > 0) {
            this.whyIsGreenColorPrivate.a = c.a;
            c = this.whyIsGreenColorPrivate;
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount-this.activations), x, y, this.fontScale, c);
        }
    }

    @Override
    public void atStartOfTurn() {
        activations = 0;
//        flash();
//        this.addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
//        this.addToBot(new ApplyPowerAction(owner, owner, new LoseStrengthPower(owner, amount), amount));
//        this.addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, amount), amount));
//        this.addToBot(new ApplyPowerAction(owner, owner, new LoseDexterityPower(owner, amount), amount));
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }

    }

    @Override
    public AbstractPower makeCopy() {
        return new CreepingShadowsPower(owner, amount);
    }

}
