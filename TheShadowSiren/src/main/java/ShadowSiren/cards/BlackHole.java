package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.cards.interfaces.MagicAnimation;
import ShadowSiren.cards.interfaces.ManuallySizeAdjustedCard;
import ShadowSiren.cards.interfaces.ModularDescription;
import ShadowSiren.characters.Vivian;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostForTurnAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.EmptyStanceEffect;
import com.megacrit.cardcrawl.vfx.combat.SanctityEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class BlackHole extends AbstractShadowCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(BlackHole.class.getSimpleName());
    public static final String IMG = makeCardPath("BlackHole.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int EFFECT = 1;
    private static final int UPGRADE_PLUS_EFFECT = 1;
    private static final int INCREASE = 2;
    private static final int UPGRADE_PLUS_INCREASE = 1;

    // /STAT DECLARATION/

    public BlackHole() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        int sum = 0;
//        this.addToBot(new VFXAction(new EmptyStanceEffect(p.hb.cX, p.hb.cY)));
//        for (AbstractPower pow : p.powers) {
//            if (pow.type == AbstractPower.PowerType.DEBUFF && !(pow instanceof InvisiblePower)) {
//                this.addToBot(new RemoveSpecificPowerAction(p, p, pow));
//                sum++;
//            }
//        }
//        if (sum > 0) {
//
//        }
        this.addToBot(new VFXAction(new SanctityEffect(p.hb.cX, p.hb.cY), 0.2F));
        for (AbstractCard c : p.hand.group) {
            if (c.costForTurn > 0) {
                this.addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        c.setCostForTurn(c.costForTurn - magicNumber);
                        c.flash(GOLD_BORDER_GLOW_COLOR);
                        this.isDone = true;
                    }
                });
            }
        }
    }

//    private boolean hasDebuff() {
//        return AbstractDungeon.player.powers.stream().anyMatch(p -> p.type == AbstractPower.PowerType.DEBUFF);
//    }
//
//    public void triggerOnGlowCheck() {
//        super.triggerOnGlowCheck();
//        if (hasDebuff()) {
//            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
//        } else {
//            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
//        }
//    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}
