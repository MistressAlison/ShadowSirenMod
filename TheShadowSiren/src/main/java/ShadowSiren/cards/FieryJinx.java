package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.interfaces.VigorMagicBuff;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.BurnPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class FieryJinx extends AbstractDynamicCard implements VigorMagicBuff {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(FieryJinx.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 0;

    private static final int BURN = 3;
    private static final int UPGRADE_PLUS_BURN = 1;

    // /STAT DECLARATION/


    public FieryJinx() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BURN;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if(!aM.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(aM, p, new BurnPower(aM, p, magicNumber)));
            }
        }
    }

    /*
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if (AbstractDungeon.player.hasPower(VigorPower.POWER_ID)) {
            magicNumber += AbstractDungeon.player.getPower(VigorPower.POWER_ID).amount;
            isMagicNumberModified = magicNumber != baseMagicNumber;
            initializeDescription();
        }
    }*/

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BURN);
            initializeDescription();
        }
    }
}