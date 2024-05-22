package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractFireCard;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Singe extends AbstractFireCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Singe.class.getSimpleName());
    public static final String IMG = makeCardPath("Singe.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;
    private static final int HP_LOSS = 5;
    private static final int UPGRADE_PLUS_HP_LOSS = 2;
    private static final int MULTIPLIER = 2;
    private static final int UPGRADE_PLUS_MULTIPLIER = 1;

    // /STAT DECLARATION/


    public Singe() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = HP_LOSS;
        magicNumber = baseMagicNumber = MULTIPLIER;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new LoseHPAction(m, p, damage, AbstractGameAction.AttackEffect.FIRE));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                m.decreaseMaxHealth(damage*magicNumber);
                this.isDone = true;
            }
        });
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_HP_LOSS);
            //upgradeMagicNumber(UPGRADE_PLUS_MULTIPLIER);
        }
    }
}