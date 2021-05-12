package ShadowSiren.actions;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractModdedCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.stances.*;
import basemod.AutoAdd;
import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class EnterAnyStanceAction extends AbstractGameAction implements ModalChoice.Callback {
    public static final float DURATION = Settings.ACTION_DUR_XFAST;
    public static final String IMG = makeCardPath("PlaceholderSkill.png");
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private ModalChoice modal;

    public EnterAnyStanceAction() {
        this.duration = DURATION;
        AbstractPlayer p = AbstractDungeon.player;
        AbstractCard option1 = p.stance.ID.equals(VeilStance.STANCE_ID) ? new Encore(TEXT[1], TEXT[2]) : new VeilShift(TEXT[3], TEXT[4]);
        AbstractCard option2 = p.stance.ID.equals(AbyssStance.STANCE_ID) ? new Encore(TEXT[1], TEXT[2]) : new AbyssShift(TEXT[5], TEXT[6]);
        AbstractCard option3 = p.stance.ID.equals(SmokeStance.STANCE_ID) ? new Encore(TEXT[1], TEXT[2]) : new SmokeShift(TEXT[7], TEXT[8]);
        AbstractCard option4 = p.stance.ID.equals(HugeStance.STANCE_ID) ? new Encore(TEXT[1], TEXT[2]) : new HugeShift(TEXT[9], TEXT[10]);
        AbstractCard option5 = p.stance.ID.equals(HyperStance.STANCE_ID) ? new Encore(TEXT[1], TEXT[2]) : new HyperShift(TEXT[11], TEXT[12]);
        modal = new ModalChoiceBuilder()
                .setCallback(this)
                .setTitle(TEXT[0])
                .addOption(option1)
                .addOption(option2)
                .addOption(option3)
                .addOption(option4)
                .addOption(option5)
                .create();
    }

    @Override
    public void update() {
        if (this.duration == DURATION) {
            modal.open();
        }

        this.tickDuration();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ShadowSirenMod.makeID("ChooseStance"));
        TEXT = uiStrings.TEXT;
    }

    @Override
    public void optionSelected(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster, int i) {}

    //There has got to be a better way to do this. Oh well
    @AutoAdd.Ignore
    private static class Encore extends AbstractModdedCard {
        public Encore(String name, String description) {
            super(Encore.class.getSimpleName(), name, IMG, -2, description, CardType.SKILL, COLOR, CardRarity.RARE, CardTarget.SELF);
            this.setBackgroundTexture(ShadowSirenMod.SKILL_VOODOO, ShadowSirenMod.SKILL_VOODOO_PORTRAIT);
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
        }

        @Override
        public AbstractCard makeCopy() {
            return new Encore(this.name, this.rawDescription);
        }
    }

    @AutoAdd.Ignore
    private static class VeilShift extends AbstractModdedCard {
        public VeilShift(String name, String description) {
            super(VeilShift.class.getSimpleName(), name, IMG, -2, description, CardType.SKILL, COLOR, CardRarity.RARE, CardTarget.SELF);
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
            return new VeilShift(this.name, this.rawDescription);
        }
    }

    @AutoAdd.Ignore
    private static class AbyssShift extends AbstractModdedCard {
        public AbyssShift(String name, String description) {
            super(AbyssShift.class.getSimpleName(), name, IMG, -2, description, CardType.SKILL, COLOR, CardRarity.RARE, CardTarget.SELF);
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
            return new AbyssShift(this.name, this.rawDescription);
        }
    }

    @AutoAdd.Ignore
    private static class SmokeShift extends AbstractModdedCard {
        public SmokeShift(String name, String description) {
            super(SmokeShift.class.getSimpleName(), name, IMG, -2, description, CardType.SKILL, COLOR, CardRarity.RARE, CardTarget.SELF);
            this.setBackgroundTexture(ShadowSirenMod.SKILL_SMOKE, ShadowSirenMod.SKILL_SMOKE_PORTRAIT);
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            this.addToBot(new ChangeStanceAction(new SmokeStance()));
        }

        @Override
        public AbstractCard makeCopy() {
            return new SmokeShift(this.name, this.rawDescription);
        }
    }

    @AutoAdd.Ignore
    private static class HugeShift extends AbstractModdedCard {
        public HugeShift(String name, String description) {
            super(HugeShift.class.getSimpleName(), name, IMG, -2, description, CardType.SKILL, COLOR, CardRarity.RARE, CardTarget.SELF);
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
            return new HugeShift(this.name, this.rawDescription);
        }
    }

    @AutoAdd.Ignore
    private static class HyperShift extends AbstractModdedCard {
        public HyperShift(String name, String description) {
            super(HyperShift.class.getSimpleName(), name, IMG, -2, description, CardType.SKILL, COLOR, CardRarity.RARE, CardTarget.SELF);
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
            return new HyperShift(this.name, this.rawDescription);
        }
    }
}
