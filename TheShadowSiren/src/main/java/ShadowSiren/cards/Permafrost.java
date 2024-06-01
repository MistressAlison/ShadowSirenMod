package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractIceCard;
import ShadowSiren.characters.Vivian;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Permafrost extends AbstractIceCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Permafrost.class.getSimpleName());
    public static final String IMG = makeCardPath("Permafrost.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int WEAK = 2;
    private static final int UPGRADE_PLUS_WEAK = 1;

    // /STAT DECLARATION/

    public Permafrost() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = WEAK;
        baseBlock = block = 0;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.WHITE.cpy(), true));
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(aM, p, new WeakPower(aM, this.magicNumber, false), this.magicNumber, true));
            }
        }
        addToBot(new GainBlockAction(p, block));
    }

    @Override
    protected void applyPowersToBlock() {
        baseBlock = 0;
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (!mon.isDeadOrEscaped()) {
                AbstractPower weak = mon.getPower(WeakPower.POWER_ID);
                if (weak != null) {
                    baseBlock += weak.amount;
                }
                if (!mon.hasPower(ArtifactPower.POWER_ID)) {
                    baseBlock += magicNumber;
                }
            }
        }
        super.applyPowersToBlock();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_WEAK);
            initializeDescription();
        }
    }
}
