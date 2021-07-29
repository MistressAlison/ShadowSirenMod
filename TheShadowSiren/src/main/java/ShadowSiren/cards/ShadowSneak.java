package ShadowSiren.cards;

import IconsAddon.util.DamageModifierHelper;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.characters.Vivian;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ShadowSneak extends AbstractShadowCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ShadowSneak.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int INCREASE = 2;
    private static final int UPGRADE_PLUS_INCREASE = 1;

    // /STAT DECLARATION/

    public ShadowSneak() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = INCREASE;
        AutoplayField.autoplay.set(this, true);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(DamageModifierHelper.makeModifiedDamageRandomEnemyAction(this, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                baseDamage += magicNumber;
                applyPowers();

                for(AbstractCard c : AbstractDungeon.player.discardPile.group) {
                    if (c instanceof ShadowSneak) {
                        c.baseDamage += magicNumber;
                        c.applyPowers();
                    }
                }

                for (AbstractCard c: AbstractDungeon.player.drawPile.group) {
                    if (c instanceof ShadowSneak) {
                        c.baseDamage += magicNumber;
                        c.applyPowers();
                    }
                }

                for (AbstractCard c: AbstractDungeon.player.hand.group) {
                    if (c instanceof ShadowSneak) {
                        c.baseDamage += magicNumber;
                        c.applyPowers();
                    }
                }

                this.isDone = true;
            }
        });
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_INCREASE);
            initializeDescription();
        }
    }
}
