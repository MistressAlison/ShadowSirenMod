package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.prismatics.AbstractPrismaticBaseCard;
import ShadowSiren.cards.prismaticCards.*;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Ferment extends AbstractPrismaticBaseCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Ferment.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    private static final int EFFECT = 1;
    private static final int UPGRADE_PLUS_EFFECT = 1;

    // /STAT DECLARATION/
    public Ferment() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, null, new FermentVeil(), new FermentAbyss(), new FermentSmoke(), new FermentHuge(), new FermentHyper());
        magicNumber = baseMagicNumber = EFFECT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded) {
            modifyPowers(m);
        } else {
            for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
                if (!mon.isDeadOrEscaped()) {
                    modifyPowers(mon);
                }
            }
        }
    }

    private void modifyPowers(AbstractMonster m) {
        boolean didSomething = false;
        for (AbstractPower pow : m.powers) {
            if (pow.type == AbstractPower.PowerType.DEBUFF) {
                pow.amount += Math.signum(pow.amount)*magicNumber;
                pow.flashWithoutSound();
                pow.updateDescription();
                didSomething = true;
            }
        }
        if (didSomething) {
            this.addToTop(new SFXAction("POWER_SHACKLE"));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            target = CardTarget.ALL_ENEMY;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            super.upgrade();
        }
    }
}