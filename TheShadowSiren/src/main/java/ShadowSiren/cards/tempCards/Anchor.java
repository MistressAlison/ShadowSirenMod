package ShadowSiren.cards.tempCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.ImmediateExhaustCardAction;
import ShadowSiren.cards.abstractCards.AbstractAbyssCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.stances.AbyssStance;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Anchor extends AbstractAbyssCard implements TempCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Anchor.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF_AND_ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;
    private static final int DAMAGE = 2;
    private static final int BLOCK = 2;
    private static final int RETAIN_BOOST = 1;
    private static final int UPGRADE_PLUS_RETAIN_BOOST = 1;

    // /STAT DECLARATION/

    public Anchor() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = RETAIN_BOOST;
        exhaust = true;
        selfRetain = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        this.addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void onRetained() {
        this.upgradeDamage(this.magicNumber);
        this.upgradeBlock(this.magicNumber);
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_RETAIN_BOOST);
            initializeDescription();
            super.upgrade();
        }
    }
}
