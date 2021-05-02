package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.interfaces.VigorMagicBuff;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.BurnPower;
import ShadowSiren.powers.FreezePower;
import ShadowSiren.powers.WillOWispPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Whiteout extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Whiteout.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 3;
    private static final int UPGRADE_COST = 2;
    private static final int FREEZE = 1;

    // /STAT DECLARATION/


    public Whiteout() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = FREEZE;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.WHITE.cpy(), true));
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(aM, p, new FreezePower(aM, magicNumber), magicNumber, true));
                aM.tint.color = Color.BLUE.cpy();
                aM.tint.changeColor(Color.WHITE.cpy());
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}