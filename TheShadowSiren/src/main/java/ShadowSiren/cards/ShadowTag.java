package ShadowSiren.cards;

import IconsAddon.cardmods.AddIconToDescriptionMod;
import IconsAddon.icons.DarkIcon;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractInertCard;
import ShadowSiren.cards.abstractCards.AbstractModdedCard;
import ShadowSiren.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.cards.interfaces.ElementallyInert;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.ShadowDamage;
import ShadowSiren.patches.ElementalPatches;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
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

    // /STAT DECLARATION/


    public ShadowTag() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (p.drawPile.isEmpty()) {
                    this.isDone = true;
                    return;
                }
                CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                for (AbstractCard c : p.drawPile.group) {
                    if (c.type == CardType.ATTACK) {
                        tmp.addToRandomSpot(c);
                    }
                }

                if (tmp.size() == 0) {
                    this.isDone = true;
                    return;
                }

                tmp.shuffle();

                AbstractCard card = tmp.getBottomCard();
                tmp.removeCard(card);
                if (ElementalPatches.noElementalModifiers(card) && !(card instanceof ElementallyInert) && !(card instanceof AbstractInertCard)) {
                    DamageModifierManager.addModifier(card, new ShadowDamage());
                    CardModifierManager.addModifier(card, new AddIconToDescriptionMod(AddIconToDescriptionMod.DAMAGE, DarkIcon.get()));
                    card.setCostForTurn(0);
                    if (card instanceof AbstractModdedCard) {
                        ((AbstractModdedCard)card).setBackgroundTexture(ShadowSirenMod.ATTACK_SHADOW, ShadowSirenMod.ATTACK_SHADOW_PORTRAIT);
                    }
                }
                if (p.hand.size() == 10) {
                    p.drawPile.moveToDiscardPile(card);
                    p.createHandIsFullDialog();
                } else {
                    card.unhover();
                    card.lighten(true);
                    card.setAngle(0.0F);
                    card.drawScale = 0.12F;
                    card.targetDrawScale = 0.75F;
                    card.current_x = CardGroup.DRAW_PILE_X;
                    card.current_y = CardGroup.DRAW_PILE_Y;
                    p.drawPile.removeCard(card);
                    AbstractDungeon.player.hand.addToTop(card);
                    AbstractDungeon.player.hand.refreshHandLayout();
                    AbstractDungeon.player.hand.applyPowers();
                }

                this.isDone = true;
            }
        });
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
