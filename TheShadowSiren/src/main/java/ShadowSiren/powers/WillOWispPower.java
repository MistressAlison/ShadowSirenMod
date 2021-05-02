package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WillOWispPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = ShadowSirenMod.makeID("WillOWispPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final AbstractCreature source;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public WillOWispPower(AbstractCreature owner, AbstractCreature source) {
        this(owner, source, 1);
    }

    public WillOWispPower(AbstractCreature owner, AbstractCreature source, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.source = source;

        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("cExplosion");
        //logger.info("Blasting Fuse?");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void onDeath() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead() && this.owner.currentHealth <= 0) {
            if (owner.hasPower(BurnPower.POWER_ID)) {
                int stacks = amount * owner.getPower(BurnPower.POWER_ID).amount;
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDeadOrEscaped()) {
                        this.addToTop(new ApplyPowerAction(m, owner, new BurnPower(m, source, stacks)));
                    }
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new WillOWispPower(owner, source, amount);
    }

}
