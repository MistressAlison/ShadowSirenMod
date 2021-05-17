package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cardModifiers.BideModifier;
import ShadowSiren.cards.abstractCards.AbstractAbyssCard;
import ShadowSiren.cards.dualityCards.abyss.VacuumFistDual;
import ShadowSiren.cards.interfaces.FistAttack;
import ShadowSiren.characters.Vivian;
import ShadowSiren.stances.AbyssStance;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class VacuumFist extends AbstractAbyssCard implements FistAttack {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(VacuumFist.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_PLUS_DMG = 4;
    private static final int VULN = 2;
    private static final int UPGRADE_PLUS_VULN = 1;

    // /STAT DECLARATION/


    public VacuumFist() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new VacuumFistDual());
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = VULN;
        CardModifierManager.addModifier(this, new BideModifier(2, UPGRADE_PLUS_DMG, 0, UPGRADE_PLUS_VULN));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_VULN);
            initializeDescription();
            super.upgrade();
        }
    }
}