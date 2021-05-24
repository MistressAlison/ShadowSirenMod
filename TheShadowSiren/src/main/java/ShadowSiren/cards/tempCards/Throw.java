package ShadowSiren.cards.tempCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.abstractCards.AbstractModdedCard;
import ShadowSiren.cards.interfaces.MultiCardPreviewHack;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.SoftPower;
import basemod.AutoAdd;
import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Throw extends AbstractDynamicCard implements TempCard, ModalChoice.Callback, MultiCardPreviewHack {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Throw.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget OPTION_TARGET = CardTarget.NONE;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = -2;
    private static final int UP_DAMAGE = 2;
    private static final int DOWN_DAMAGE = 2;
    private static final int BACK_DAMAGE = 2;
    private static final int FORWARD_DAMAGE = 5;
    private static final int VULN = 1;
    private static final int SOFT = 1;
    private static final int WEAK = 1;

    private ModalChoice modal;
    public AbstractMonster enemyToHit;

    // /STAT DECLARATION/

    //TODO make it preview Throw as well
    public Throw() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = VULN;
        damage = baseDamage = UP_DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enemyToHit = m;
        AbstractCard option1 = new UpThrow(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[4]);
        AbstractCard option2 = new DownThrow(EXTENDED_DESCRIPTION[1], EXTENDED_DESCRIPTION[5]);
        AbstractCard option3 = new BackThrow(EXTENDED_DESCRIPTION[2], EXTENDED_DESCRIPTION[6]);
        AbstractCard option4 = new ForwardThrow(EXTENDED_DESCRIPTION[3], EXTENDED_DESCRIPTION[7]);

        modal = new ModalChoiceBuilder()
                .setCallback(this)
                .addOption(option1)
                .addOption(option2)
                .addOption(option3)
                .addOption(option4)
                .create();

        modal.open();

        if (upgraded) {
            this.addToBot(new DrawCardAction(1));
        }
    }

    @Override
    public void optionSelected(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster, int i) {}

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @AutoAdd.Ignore
    private class UpThrow extends AbstractModdedCard {
        public UpThrow(String name, String desc) {
            super(UpThrow.class.getSimpleName(), name, IMG, -2, desc, CardType.ATTACK, COLOR, RARITY, OPTION_TARGET);
            damage = baseDamage = UP_DAMAGE;
            magicNumber = baseMagicNumber = VULN;
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            if (enemyToHit != null) {
                applyPowers();
                calculateCardDamage(enemyToHit);
                this.addToBot(new DamageAction(enemyToHit, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                this.addToBot(new ApplyPowerAction(enemyToHit, p, new VulnerablePower(enemyToHit, magicNumber, false)));
            }
        }

        @Override
        public AbstractCard makeCopy() {
            return new UpThrow(name, rawDescription);
        }
    }

    @AutoAdd.Ignore
    private class DownThrow extends AbstractModdedCard {
        public DownThrow(String name, String desc) {
            super(DownThrow.class.getSimpleName(), name, IMG, -2, desc, CardType.ATTACK, COLOR, RARITY, OPTION_TARGET);
            damage = baseDamage = DOWN_DAMAGE;
            magicNumber = baseMagicNumber = SOFT;
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            if (enemyToHit != null) {
                applyPowers();
                calculateCardDamage(enemyToHit);
                this.addToBot(new DamageAction(enemyToHit, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                this.addToBot(new ApplyPowerAction(enemyToHit, p, new SoftPower(enemyToHit, magicNumber)));
            }
        }

        @Override
        public AbstractCard makeCopy() {
            return new DownThrow(name, rawDescription);
        }
    }

    @AutoAdd.Ignore
    private class BackThrow extends AbstractModdedCard {
        public BackThrow(String name, String desc) {
            super(BackThrow.class.getSimpleName(), name, IMG, -2, desc, CardType.ATTACK, COLOR, RARITY, OPTION_TARGET);
            damage = baseDamage = BACK_DAMAGE;
            magicNumber = baseMagicNumber = WEAK;
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            if (enemyToHit != null) {
                applyPowers();
                calculateCardDamage(enemyToHit);
                this.addToBot(new DamageAction(enemyToHit, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                this.addToBot(new ApplyPowerAction(enemyToHit, p, new WeakPower(enemyToHit, magicNumber, false)));
            }
        }

        @Override
        public AbstractCard makeCopy() {
            return new BackThrow(name, rawDescription);
        }
    }

    @AutoAdd.Ignore
    private class ForwardThrow extends AbstractModdedCard {
        public ForwardThrow(String name, String desc) {
            super(ForwardThrow.class.getSimpleName(), name, IMG, -2, desc, CardType.ATTACK, COLOR, RARITY, OPTION_TARGET);
            damage = baseDamage = FORWARD_DAMAGE;
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            if (enemyToHit != null) {
                applyPowers();
                calculateCardDamage(enemyToHit);
                this.addToBot(new DamageAction(enemyToHit, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }

        }

        @Override
        public AbstractCard makeCopy() {
            return new ForwardThrow(name, rawDescription);
        }
    }
}
