package ShadowSiren.oldStuff;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractItemCard;
import ShadowSiren.cards.interfaces.ModularDescription;
import ShadowSiren.cards.tempCards.TempCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.ElectricDamage;
import ShadowSiren.powers.ChargePower;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ElectricOrb extends AbstractItemCard implements ModularDescription, TempCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ElectricOrb.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.NONE;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int CHARGE = 1;
    private static final int USES = 2;
    private static final int UPGRADE_PLUS_USES = 1;

    // /STAT DECLARATION/

    public ElectricOrb() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = CHARGE;
        baseUsesCount = usesCount = USES;
        DamageModifierManager.addModifier(this, new ElectricDamage());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void onRetained() {
        if (isActive) {
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargePower(AbstractDungeon.player, magicNumber)));
            this.superFlash();
            this.applyEffect();
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

    @Override
    public void changeDescription() {
        if (DESCRIPTION != null) {
            if (magicNumber > 1) {
                rawDescription = UPGRADE_DESCRIPTION;
            } else {
                rawDescription = DESCRIPTION;
            }
        }
    }
}
