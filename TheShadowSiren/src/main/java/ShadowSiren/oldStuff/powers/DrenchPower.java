package ShadowSiren.oldStuff.powers;

import ShadowSiren.ShadowSirenMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DrenchPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = ShadowSirenMod.makeID("DrenchPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final int STEAM_DAMAGE = 2;
    public static final int STEAM_BURN = 2;
    public static final int CHILL_BONUS = 2;
    public static final int ELECTRIC_DAMAGE = 1;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public DrenchPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("like_water");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void playApplyPowerSfx() {
        this.addToTop(new SFXAction("BLOOD_SWISH", 0.1F));
    }

    @Override
    public void updateDescription() {
        /*description = DESCRIPTIONS[0] + (STEAM_DAMAGE*amount) + DESCRIPTIONS[1] + (STEAM_BURN*amount) + DESCRIPTIONS[2]
                + DESCRIPTIONS[3] + (CHILL_BONUS*amount) + DESCRIPTIONS[4]
                + DESCRIPTIONS[5] + (ELECTRIC_DAMAGE*amount) + DESCRIPTIONS[6];*/
        description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + (CHILL_BONUS*amount) + DESCRIPTIONS[2] + DESCRIPTIONS[3] + (ELECTRIC_DAMAGE*amount) + DESCRIPTIONS[4];
    }

    @Override
    public AbstractPower makeCopy() {
        return new DrenchPower(owner, amount);
    }
}
