package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.SwapDualityCardsAction;
import ShadowSiren.cards.abstractCards.AbstractDualityCard;
import ShadowSiren.cards.abstractCards.AbstractSmokeCard;
import ShadowSiren.cards.dualityCards.smoke.CloakDual;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.stances.SmokeStance;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Cloak extends AbstractSmokeCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Cloak.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;


    // /STAT DECLARATION/


    public Cloak() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new CloakDual());
        block = baseBlock = BLOCK;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, block));
    }

    @Override
    protected void applyPowersToBlock() {
        super.applyPowersToBlock();
        if (hasDebuff()) {
            block *= 2;
            isBlockModified = block != baseBlock;
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
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
            super.upgrade();
        }
    }
}