package ShadowSiren.cards.tempCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.interfaces.ModularDescription;
import ShadowSiren.cards.interfaces.MultiCardPreviewHack;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Pummel extends AbstractDynamicCard implements TempCard, MultiCardPreviewHack {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Pummel.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;
    private static final int DAMAGE = 2;
    private static final int UPGRADE_PLUS_DAMAGE = 1;
    private static final int BASE_HITS = 1;
    private static final int MAX_HITS = 3;

    // /STAT DECLARATION/


    public Pummel() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BASE_HITS;
        secondMagicNumber = baseSecondMagicNumber = MAX_HITS;
        cardsToPreview = new Throw();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0 ; i < thirdMagicNumber ; i++) {
            if (p instanceof Vivian) {
                this.addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        ((Vivian) p).playAnimation("punch");
                        this.isDone = true;
                    }
                });
            }
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            if (p instanceof Vivian) {
                this.addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        ((Vivian) p).playAnimation("idle");
                        this.isDone = true;
                    }
                });
            }
        }
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (!m.isDeadOrEscaped() && m.currentHealth > 0) {
                    cardsToPreview.use(p, m);
                }
                this.isDone = true;
            }
        });
        //this.addToBot(new MakeTempCardInHandAction(cardsToPreview.makeStatEquivalentCopy(), 1));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        thirdMagicNumber = baseThirdMagicNumber;
        isMagicNumberModified = false;
        //changeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        thirdMagicNumber = (int)Math.min(MAX_HITS, Math.max(1, -Math.ceil(MAX_HITS*(float)mo.currentHealth/mo.maxHealth)+MAX_HITS+1));
        isThirdMagicNumberModified = thirdMagicNumber != baseThirdMagicNumber;
        //changeDescription();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
        }
    }
}
