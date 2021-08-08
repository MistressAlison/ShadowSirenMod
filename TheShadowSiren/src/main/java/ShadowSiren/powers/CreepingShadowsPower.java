package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

public class CreepingShadowsPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = ShadowSirenMod.makeID("CreepingShadowsPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

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

    @Override
    public void atStartOfTurn() {
        flash();
        this.addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
        this.addToBot(new ApplyPowerAction(owner, owner, new LoseStrengthPower(owner, amount), amount));
        this.addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, amount), amount));
        this.addToBot(new ApplyPowerAction(owner, owner, new LoseDexterityPower(owner, amount), amount));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new CreepingShadowsPower(owner, amount);
    }

}
