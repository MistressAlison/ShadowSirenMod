package ShadowSiren.cards.itemCards;

import IconsAddon.util.DamageModifierHelper;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractItemCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.IceDamage;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class IceOrb extends AbstractItemCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(IceOrb.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.NONE;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int BLOCK = 3;
    private static final int UPGRADE_PLUS_BLOCK = 1;
    private static final int USES = 4;
    private static final int UPGRADE_PLUS_USES = 2;

    // /STAT DECLARATION/

    public IceOrb() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        baseBlock = block = BLOCK;
        baseUsesCount = usesCount = USES;
        DamageModifierManager.addModifier(this, new IceDamage());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (isActive && c != this) {
            if (c.type == CardType.ATTACK) {
                if (m != null) {
                    this.addToBot(new DamageAction(m, DamageModifierHelper.makeBoundDamageInfo(this, AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS), true));
                } else {
                    AbstractCreature target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                    if (target != null) {
                        this.addToBot(new DamageAction(target, DamageModifierHelper.makeBoundDamageInfo(this, AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS), true));
                    }
                }
                this.superFlash();
                this.applyEffect();
            } else if (c.type == CardType.SKILL) {
                this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block, true));
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
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
