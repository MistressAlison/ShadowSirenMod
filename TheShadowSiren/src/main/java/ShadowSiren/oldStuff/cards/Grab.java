package ShadowSiren.oldStuff.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.interfaces.MagicAnimation;
import ShadowSiren.cards.interfaces.MultiCardPreviewHack;
import ShadowSiren.oldStuff.cards.tempCards.Pummel;
import ShadowSiren.oldStuff.cards.tempCards.Throw;
import ShadowSiren.characters.Vivian;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Grab extends AbstractDynamicCard implements MagicAnimation, MultiCardPreviewHack {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Grab.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;

    private final ArrayList<AbstractCard> previews = new ArrayList<>();
    public float duration = 0;
    public int state = 0;

    //private AbstractCard useCard = new Pummel();

    // /STAT DECLARATION/
    public Grab() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        previews.add(new Pummel());
        previews.add(new Throw());
        cardsToPreview = previews.get(0);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new RemoveAllBlockAction(m, p));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (!m.isDeadOrEscaped() && m.currentHealth > 0) {
                    AbstractCard card = new Pummel();
                    if (upgraded) card.upgrade();
                    card.applyPowers();
                    card.calculateCardDamage(m);
                    card.purgeOnUse = true;
                    card.isInAutoplay = true;
                    //card.use(p, m);
                    //this.addToTop(new QueueCardAction(card, m));
                    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(card, m));
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public void update() {
        super.update();
        if (previews.size() > 0) {
            duration += Gdx.graphics.getDeltaTime();
            if (duration > 3.0f) {
                state = (state + 1) % previews.size();
                cardsToPreview = previews.get(state);
                duration = 0.0f;
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            cardsToPreview.upgrade();
        }
    }
}