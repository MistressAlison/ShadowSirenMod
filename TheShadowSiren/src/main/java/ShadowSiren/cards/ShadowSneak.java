package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.MoveCardsOfPredicateToHandAction;
import ShadowSiren.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.characters.Vivian;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.function.Predicate;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ShadowSneak extends AbstractShadowCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ShadowSneak.class.getSimpleName());
    public static final String IMG = makeCardPath("DarkEmbrace.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 2;

    private static final Predicate<AbstractCard> costZeroCard = c -> c.cost == 0 || c.costForTurn == 0 || c.freeToPlayOnce;

    // /STAT DECLARATION/

    public ShadowSneak() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        this.addToBot(new MoveCardsOfPredicateToHandAction(costZeroCard, 1));
    }

    private boolean noZeroCosts() {
        return AbstractDungeon.player.drawPile.group.stream().noneMatch(costZeroCard);
    }

    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (noZeroCosts()) {
            this.glowColor = Color.RED.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
