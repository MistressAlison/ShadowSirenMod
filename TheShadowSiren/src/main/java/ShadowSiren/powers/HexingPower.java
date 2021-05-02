package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import basemod.ReflectionHacks;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class HexingPower extends AbstractPower implements CloneablePowerInterface, HealthBarRenderPower {

    public static final String POWER_ID = ShadowSirenMod.makeID("HexingPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final AbstractCreature source;
    private static final float DECAY_RATE = 0.5f;
    private final Color hpBarColor = CardHelper.getColor(83.0f, 52.0f, 85.0f);

    private boolean attackedThisTurn;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public HexingPower(AbstractCreature owner, AbstractCreature source, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.source = source;

        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("hex");
        //logger.info("Blasting Fuse?");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flashWithoutSound();
            this.addToTop(new DamageAction(owner, new DamageInfo(source, amount, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.NONE));
            attackedThisTurn = true;
        }
    }

    @Override
    public void atEndOfRound() {
        if (!attackedThisTurn) {
            this.flashWithoutSound();
            if (this.amount <= 1) {
                this.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            } else {
                this.amount *= DECAY_RATE;
                updateDescription();
            }
        }
        attackedThisTurn = false;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new HexingPower(owner, source, amount);
    }

    @Override
    public int getHealthBarAmount() {
        int amt = amount;
        if (owner instanceof AbstractMonster) {
            if(((AbstractMonster) owner).getIntentBaseDmg() >= 0) {
                if (ReflectionHacks.getPrivate(owner, AbstractMonster.class, "isMultiDmg")) {
                    amt *= (int)ReflectionHacks.getPrivate(owner, AbstractMonster.class, "intentMultiAmt");
                }
            } else {
                amt = 0;
            }
        }
        return amt;
    }

    @Override
    public Color getColor() {
        return hpBarColor;
    }
}
