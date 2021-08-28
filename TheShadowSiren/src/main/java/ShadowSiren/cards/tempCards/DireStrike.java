package ShadowSiren.cards.tempCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class DireStrike extends AbstractDynamicCard implements TempCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(DireStrike.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 3;

    // /STAT DECLARATION/

    public DireStrike() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        this.tags.add(CardTags.STRIKE);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        if(hasDebuff()) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        }
    }

    private boolean hasDebuff() {
        for (AbstractPower pow : AbstractDungeon.player.powers) {
            if (pow.type == AbstractPower.PowerType.DEBUFF) {
                return true;
            }
        }
        return false;
    }

    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (hasDebuff()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
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
