package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.abstractCards.AbstractModdedCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.stances.HugeStance;
import ShadowSiren.stances.HyperStance;
import ShadowSiren.stances.VeilStance;
import ShadowSiren.stances.AbyssStance;
import basemod.AutoAdd;
import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class DiamondInTheRough extends AbstractDynamicCard implements ModalChoice.Callback {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(DiamondInTheRough.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = -2;
    private static final int UPGRADE_COST = 0;

    private ModalChoice modal;

    // /STAT DECLARATION/


    public DiamondInTheRough() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        isEthereal = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            AbstractCard option1 = new VeilShift(this.name);
            AbstractCard option2 = new VoidShift(this.name);
            AbstractCard option3 = new HugeShift(this.name);
            AbstractCard option4 = new HyperShift(this.name);

            modal = new ModalChoiceBuilder()
                    .setCallback(this)
                    .addOption(option1)
                    .addOption(option2)
                    .addOption(option3)
                    .addOption(option4)
                    .create();

            modal.open();
        }
    }

    /*@Override
    public float getTitleFontSize() {
        return 16f;
    }*/

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return upgraded && super.canUse(p, m);
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            rawDescription = UPGRADE_DESCRIPTION;
            isEthereal = false;
            initializeDescription();
        }
    }

    @Override
    public void optionSelected(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster, int i) {

    }

    @AutoAdd.Ignore
    private class VeilShift extends AbstractModdedCard {
        public VeilShift(String name) {
            super(VeilShift.class.getSimpleName(), name, IMG, -2, DiamondInTheRough.this.EXTENDED_DESCRIPTION[0], CardType.SKILL, COLOR, CardRarity.RARE, CardTarget.SELF);
            this.setBackgroundTexture(ShadowSirenMod.SKILL_SHADOW, ShadowSirenMod.SKILL_SHADOW_PORTRAIT);
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            this.addToBot(new ChangeStanceAction(new VeilStance()));
        }

        @Override
        public AbstractCard makeCopy() {
            return new VeilShift(name);
        }
    }

    @AutoAdd.Ignore
    private class VoidShift extends AbstractModdedCard {
        public VoidShift(String name) {
            super(VoidShift.class.getSimpleName(), name, IMG, -2, DiamondInTheRough.this.EXTENDED_DESCRIPTION[1], CardType.SKILL, COLOR, CardRarity.RARE, CardTarget.SELF);
            this.setBackgroundTexture(ShadowSirenMod.SKILL_VOID, ShadowSirenMod.SKILL_VOID_PORTRAIT);
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            this.addToBot(new ChangeStanceAction(new AbyssStance()));
        }

        @Override
        public AbstractCard makeCopy() {
            return new VoidShift(name);
        }
    }

    @AutoAdd.Ignore
    private class HugeShift extends AbstractModdedCard {
        public HugeShift(String name) {
            super(HugeShift.class.getSimpleName(), name, IMG, -2, DiamondInTheRough.this.EXTENDED_DESCRIPTION[2], CardType.SKILL, COLOR, CardRarity.RARE, CardTarget.SELF);
            this.setBackgroundTexture(ShadowSirenMod.SKILL_HUGE, ShadowSirenMod.SKILL_HUGE_PORTRAIT);
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            this.addToBot(new ChangeStanceAction(new HugeStance()));
        }

        @Override
        public AbstractCard makeCopy() {
            return new HugeShift(name);
        }
    }

    @AutoAdd.Ignore
    private class HyperShift extends AbstractModdedCard {
        public HyperShift(String name) {
            super(HyperShift.class.getSimpleName(), name, IMG, -2, DiamondInTheRough.this.EXTENDED_DESCRIPTION[3], CardType.SKILL, COLOR, CardRarity.RARE, CardTarget.SELF);
            this.setBackgroundTexture(ShadowSirenMod.SKILL_HYPER, ShadowSirenMod.SKILL_HYPER_PORTRAIT);
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            this.addToBot(new ChangeStanceAction(new HyperStance()));
        }

        @Override
        public AbstractCard makeCopy() {
            return new HyperShift(name);
        }
    }
}