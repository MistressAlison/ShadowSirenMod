package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.MoveCardsOfTypeToHandAction;
import ShadowSiren.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.characters.Vivian;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ShadowTag extends AbstractShadowCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ShadowTag.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int CARDS_TO_MOVE = 1;

    // /STAT DECLARATION/

    public ShadowTag() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new MoveCardsOfTypeToHandAction(CardType.ATTACK, CARDS_TO_MOVE, true));
    }

    private boolean noAttacks() {
        return AbstractDungeon.player.drawPile.group.stream().noneMatch(c -> c.type == CardType.ATTACK);
    }

    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (noAttacks()) {
            this.glowColor = Color.RED.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

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
