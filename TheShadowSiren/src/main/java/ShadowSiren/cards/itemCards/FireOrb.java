package ShadowSiren.cards.itemCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractItemCard;
import ShadowSiren.cards.tempCards.TempCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.FireDamage;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class FireOrb extends AbstractItemCard implements TempCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(FireOrb.class.getSimpleName());
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
    private static final int VIGOR = 2;
    private static final int UPGRADE_PLUS_VIGOR = 1;
    private static final int USES = 4;
    private static final int UPGRADE_PLUS_USES = 2;

    // /STAT DECLARATION/

    public FireOrb() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        baseMagicNumber = magicNumber = VIGOR;
        baseUsesCount = usesCount = USES;
        DamageModifierManager.addModifier(this, new FireDamage());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (isActive && c != this) {
            if (c.type == CardType.ATTACK) {
                if (m != null) {
                    this.addToBot(new DamageAction(m, BindingHelper.makeInfo(this, AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS), true));
                } else {
                    AbstractCreature target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                    if (target != null) {
                        this.addToBot(new DamageAction(target, BindingHelper.makeInfo(this, AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS), true));
                    }
                }
                this.superFlash();
                this.applyEffect();
            } else if (c.type == CardType.SKILL) {
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VigorPower(AbstractDungeon.player, magicNumber), magicNumber, true));
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
            upgradeMagicNumber(UPGRADE_PLUS_VIGOR);
            initializeDescription();
        }
    }
}
