package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.oldStuff.cards.dualityCards.veil.CollapseDual;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Collapse extends AbstractShadowCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Collapse.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;

    private static final int HP_LOSS = 5;
    private static final int UPGRADE_PLUS_HP_LOSS = 3;
    private static final int LOSS_PER_POWER = 2;
    private static final int UPGRADE_PLUS_LOSS_PER_POWER = 1;

    // /STAT DECLARATION/


    public Collapse() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new CollapseDual());
        secondMagicNumber = baseSecondMagicNumber = LOSS_PER_POWER;
        magicNumber = baseMagicNumber = HP_LOSS;
        thirdMagicNumber = baseThirdMagicNumber = 0;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Get the number of powers the monsters have.
        int powers = 0;
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                for (AbstractPower pow : aM.powers) {
                    //We don't count invisible powers
                    if (!(pow instanceof InvisiblePower)) {
                        powers++;
                    }
                }
            }
        }
        //Get the number of powers the player has as well
        for (AbstractPower pow : AbstractDungeon.player.powers) {
            if(!(pow instanceof InvisiblePower)) {
                powers++;
            }
        }

        //Drop our HP loss attack
        this.addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(magicNumber+secondMagicNumber*powers, true), DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.FIRE, true));
    }

    @Override
    public void applyPowers() {
        thirdMagicNumber = 0;
        int powers = 0;
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                for (AbstractPower pow : aM.powers) {
                    //We don't count invisible powers
                    if (!(pow instanceof InvisiblePower)) {
                        powers++;
                    }
                }
            }
        }
        for (AbstractPower pow : AbstractDungeon.player.powers) {
            if(!(pow instanceof InvisiblePower)) {
                powers++;
            }
        }
        thirdMagicNumber += (magicNumber+secondMagicNumber*powers);
        isThirdMagicNumberModified = thirdMagicNumber != baseThirdMagicNumber;
        super.applyPowers();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HP_LOSS);
            upgradeSecondMagicNumber(UPGRADE_PLUS_LOSS_PER_POWER);
            initializeDescription();
            super.upgrade();
        }
    }
}