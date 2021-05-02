package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.abstractCards.AbstractHugeCard;
import ShadowSiren.cards.dualityCards.huge.MegaFistDual;
import ShadowSiren.cards.interfaces.FistAttack;
import ShadowSiren.characters.Vivian;
import ShadowSiren.stances.HugeStance;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class MegaFist extends AbstractHugeCard implements FistAttack {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(MegaFist.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 18;
    private static final int UPGRADE_PLUS_DMG = 4;

    // /STAT DECLARATION/


    public MegaFist() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new MegaFistDual());
        damage = baseDamage = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
        this.addToBot(new ChangeStanceAction(new HugeStance()));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
            super.upgrade();
        }
    }
}