package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cardModifiers.BideModifier;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.abstractCards.AbstractHyperCard;
import ShadowSiren.cards.dualityCards.hyper.RecuperateDual;
import ShadowSiren.cards.interfaces.ModularDescription;
import ShadowSiren.characters.Vivian;
import ShadowSiren.stances.VeilStance;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.actions.watcher.NotStanceCheckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.EmptyStanceEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Recuperate extends AbstractHyperCard implements ModularDescription {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Recuperate.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int CARDS = 1;

    // /STAT DECLARATION/


    public Recuperate() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new RecuperateDual());
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = CARDS;
        CardModifierManager.addModifier(this, new BideModifier(1, 0, 0, 1));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, block));
        int cleansed = 0;
        for (AbstractPower pow : p.powers) {
            if (pow.type == AbstractPower.PowerType.DEBUFF) {
                cleansed++;
                this.addToBot(new RemoveSpecificPowerAction(p, p, pow));
                if (cleansed == magicNumber) {
                    break;
                }
            }
        }
        if (cleansed < magicNumber) {
            this.addToBot(new DrawCardAction(magicNumber-cleansed));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
            super.upgrade();
        }
    }

    @Override
    public void changeDescription() {
        rawDescription = magicNumber == 1 ? DESCRIPTION : UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}