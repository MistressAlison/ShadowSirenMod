package ShadowSiren.cards.dualityCards.huge;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractHugeCard;
import ShadowSiren.cards.interfaces.FistAttack;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class DoubleFistDual extends AbstractHugeCard implements FistAttack, UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(DoubleFistDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 1;
    private static final int HITS = 6;
    private static final int UPGRADE_PLUS_HITS = 2;

    // /STAT DECLARATION/


    public DoubleFistDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = HITS;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int anim = 0;
        AbstractGameAction.AttackEffect effect;
        for (int i = 0 ; i < magicNumber ; i++) {
            //Get the next animation to play
            switch (anim) {
                default:
                    effect = AbstractGameAction.AttackEffect.BLUNT_HEAVY;
                    break;
                case 1:
                    effect = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
                    break;
                /*case 2:
                    effect = AbstractGameAction.AttackEffect.SLASH_HORIZONTAL;
                    break;
                case 3:
                    effect = AbstractGameAction.AttackEffect.SLASH_VERTICAL;
                    break;*/
            }
            //Increment and mod our index
            anim = (anim + 1) % 2;

            //Do the hit
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), effect, true));
        }
    }

    /*@Override
    protected void upgradeName() {
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = EXTENDED_DESCRIPTION[0];
        this.initializeTitle();
    }*/

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HITS);
            initializeDescription();
            super.upgrade();
        }
    }
}