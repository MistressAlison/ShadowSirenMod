package ShadowSiren.cards.itemCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractItemCard;
import ShadowSiren.characters.Vivian;
import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class AutoArmor extends AbstractItemCard implements OnPlayerDamagedSubscriber {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(AutoArmor.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.NONE;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int USES = 4;
    private static final int UPGRADE_PLUS_USES = 2;
    private static final int REDUCTION = 3;

    // /STAT DECLARATION/

    public AutoArmor() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        baseUsesCount = usesCount = USES;
        magicNumber = baseMagicNumber = REDUCTION;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeUsesCount(UPGRADE_PLUS_USES);
            initializeDescription();
        }
    }

    @Override
    public void checkIfActive() {
        super.checkIfActive();
        if (isActive) {
            BaseMod.subscribe(this);
        } else {
            BaseMod.unsubscribe(this);
        }
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        int delta = i - AbstractDungeon.player.currentBlock;
        if (isActive && delta > 0) {
            i -= magicNumber;
            if (i < 0) {
                i = 0;
            }
            this.superFlash();
            this.applyEffect();
        }
        return i;
    }
}
