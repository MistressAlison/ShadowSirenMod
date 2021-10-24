package ShadowSiren.oldStuff.cards.prismaticCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.prismatics.AbstractPrismaticHyperCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class FermentHyper extends AbstractPrismaticHyperCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(FermentHyper.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int EFFECT = 1;

    // /STAT DECLARATION/
    public FermentHyper() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded) {
            applyEffect(m);
        } else {
            for (AbstractMonster mon :  AbstractDungeon.getMonsters().monsters) {
                if (!mon.isDeadOrEscaped()) {
                    applyEffect(mon);
                }
            }
        }
    }

    private int calcDebuffs(AbstractMonster m) {
        return (int) m.powers.stream().filter(p -> p.type == AbstractPower.PowerType.DEBUFF).count();
    }

    private void applyEffect(AbstractMonster m) {
        int effect = calcDebuffs(m);
        this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new StrengthPower(m, -effect), -effect, true, AbstractGameAction.AttackEffect.NONE));
        if (!m.hasPower("Artifact")) {
            this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new GainStrengthPower(m, effect), effect, true, AbstractGameAction.AttackEffect.NONE));
            this.addToBot(new SFXAction("POWER_SHACKLE"));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            target = CardTarget.ALL_ENEMY;
            initializeDescription();
            super.upgrade();
        }
    }
}