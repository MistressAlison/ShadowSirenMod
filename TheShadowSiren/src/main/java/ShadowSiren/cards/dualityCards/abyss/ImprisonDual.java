package ShadowSiren.cards.dualityCards.abyss;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractAbyssCard;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.DrenchPower;
import ShadowSiren.powers.SoftPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ImprisonDual extends AbstractAbyssCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ImprisonDual.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int EFFECT = 1;
    private static final int UPGRADE_PLUS_EFFECT = 1;

    // /STAT DECLARATION/


    public ImprisonDual() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                CardCrawlGame.sound.play("POWER_SHACKLE", 0.05F);
                this.isDone = true;
            }
        });
        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false)));
        this.addToBot(new ApplyPowerAction(m, p, new SoftPower(m, magicNumber)));
        this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false)));
        this.addToBot(new ApplyPowerAction(m, p, new DrenchPower(m, magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT);
            initializeDescription();
            super.upgrade();
        }
    }
}
