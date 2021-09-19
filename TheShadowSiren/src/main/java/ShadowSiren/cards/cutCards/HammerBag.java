package ShadowSiren.cards.cutCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractItemCard;
import ShadowSiren.characters.Vivian;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;
@AutoAdd.Ignore
public class HammerBag extends AbstractItemCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(HammerBag.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.NONE;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int USES = 4;
    private static final int UPGRADE_PLUS_USES = 2;

    // /STAT DECLARATION/

    public HammerBag() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        baseUsesCount = usesCount = USES;
        this.cardsToPreview = new Hammer();
        grabPreviewKeywords();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (isActive && c != this) {
            if (c.type == CardType.SKILL) {
                this.addToBot(new MakeTempCardInHandAction(cardsToPreview.makeStatEquivalentCopy(), 1));
                this.superFlash();
                this.applyEffect();
            }
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeUsesCount(UPGRADE_PLUS_USES);
            initializeDescription();
        }
    }
}
