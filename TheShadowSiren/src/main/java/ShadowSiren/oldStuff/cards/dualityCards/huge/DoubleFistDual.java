package ShadowSiren.oldStuff.cards.dualityCards.huge;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractHugeCard;
import ShadowSiren.cards.interfaces.FistAttack;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

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
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int HITS = 4;
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
        DamageInfo info = new DamageInfo(p, damage, damageTypeForTurn);
        this.addToBot(new DamageAction(m, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                //Subtract 1 since we automatically do onHit stuff once
                for (int i = 0 ; i < magicNumber-1 ; i++) {
                    for (AbstractPower pow : p.powers) {
                        pow.onAttack(info, damage, m);
                    }
                    for (AbstractPower pow : m.powers) {
                        pow.onAttacked(info, damage);
                    }
                    for (AbstractRelic r : p.relics) {
                        r.onAttack(info, damage, m);
                    }
                }
                this.isDone = true;
            }
        });
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_HITS);
            initializeDescription();
            super.upgrade();
        }
    }
}