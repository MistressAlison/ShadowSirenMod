package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractShadowCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.powers.ShadowPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class BlackRose extends AbstractShadowCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(BlackRose.class.getSimpleName());
    public static final String IMG = makeCardPath("BlackRose.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;

    // /STAT DECLARATION/

    //TODO rework
    public BlackRose() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                if (aM.hasPower(ShadowPower.POWER_ID)) {
                    this.addToBot(new VFXAction(new CardPoofEffect(aM.hb.cX, aM.hb.cY)));
                    this.addToBot(new LoseHPAction(aM, p, aM.getPower(ShadowPower.POWER_ID).amount));
                }
            }
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.exhaust = false;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
