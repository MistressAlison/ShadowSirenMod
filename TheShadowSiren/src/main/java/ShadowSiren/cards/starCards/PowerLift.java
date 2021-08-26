package ShadowSiren.cards.starCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractStarCard;
import ShadowSiren.characters.Vivian;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class PowerLift extends AbstractStarCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(PowerLift.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 3;
    private static final int EFFECT = 4;
    private static final int UPGRADE_PLUS_EFFECT = 1;
    // /STAT DECLARATION/


    public PowerLift() {
        this(false);
    }

    public PowerLift(boolean showStarCost) {
        super(ID, IMG, TYPE, COLOR, TARGET, showStarCost);
        baseMagicNumber = magicNumber = EFFECT;
        purgeOnUse = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(new InflameEffect(p)));
        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber, true));
        this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber), magicNumber, true));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT);
            initializeDescription();
        }
    }

    @Override
    public int getSpawnCost() {
        return COST;
    }
}
