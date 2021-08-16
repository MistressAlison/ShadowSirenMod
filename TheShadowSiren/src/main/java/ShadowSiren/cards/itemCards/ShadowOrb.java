package ShadowSiren.cards.itemCards;

import IconsAddon.util.DamageModifierHelper;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractItemCard;
import ShadowSiren.cards.interfaces.ModularDescription;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.FireDamage;
import ShadowSiren.damageModifiers.ShadowDamage;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ShadowOrb extends AbstractItemCard implements ModularDescription {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ShadowOrb.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.NONE;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int USES = 2;
    private static final int UPGRADE_PLUS_USES = 1;
    private static final int DEBUFFS = 1;

    // /STAT DECLARATION/

    public ShadowOrb() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        baseUsesCount = usesCount = USES;
        magicNumber = baseMagicNumber = DEBUFFS;
        DamageModifierManager.addModifier(this, new ShadowDamage());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        if (isActive && hasDebuff()) {
            int cleansed = 0;
            for (AbstractPower pow : AbstractDungeon.player.powers) {
                if (pow.type == AbstractPower.PowerType.DEBUFF && !(pow instanceof InvisiblePower)) {
                    cleansed++;
                    this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, pow));
                    if (cleansed == magicNumber) {
                        break;
                    }
                }
            }
            this.superFlash();
            this.applyEffect();
        }
    }

    //other hook takes place before powers are dealt with, so we can remove temp effects and make them permanent that that
    /*@Override
    public void onRetained() {
        if (isActive && hasDebuff()) {
            int cleansed = 0;
            for (AbstractPower pow : AbstractDungeon.player.powers) {
                if (pow.type == AbstractPower.PowerType.DEBUFF && !(pow instanceof InvisiblePower)) {
                    cleansed++;
                    this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, pow));
                    if (cleansed == magicNumber) {
                        break;
                    }
                }
            }
            this.superFlash();
            this.applyEffect();
        }
    }*/

    private boolean hasDebuff() {
        for (AbstractPower pow : AbstractDungeon.player.powers) {
            if (pow.type == AbstractPower.PowerType.DEBUFF && !(pow instanceof InvisiblePower)) {
                return true;
            }
        }
        return false;
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
