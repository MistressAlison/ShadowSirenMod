package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractHyperCard;
import ShadowSiren.oldStuff.cards.dualityCards.hyper.PhasingStrikeDual;
import ShadowSiren.patches.StanceCounterPatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class PhasingStrike extends AbstractHyperCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(PhasingStrike.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 2;

    private static final int ENERGY = 1;

    // /STAT DECLARATION/


    public PhasingStrike() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new PhasingStrikeDual());
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = ENERGY;
        this.tags.add(CardTags.STRIKE);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if (StanceCounterPatches.getTurnStanceChanges(p) > 0) {
            this.addToBot(new GainEnergyAction(magicNumber));
        }
    }

    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (StanceCounterPatches.getTurnStanceChanges(AbstractDungeon.player) > 0) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
        super.upgrade();
    }
}