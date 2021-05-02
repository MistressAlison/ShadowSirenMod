package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.MakeTempCardInExhaustAction;
import ShadowSiren.cards.abstractCards.AbstractAbyssCard;
import ShadowSiren.cards.dualityCards.abyss.CrushDual;
import ShadowSiren.cards.tempCards.Splash;
import ShadowSiren.characters.Vivian;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class Crush extends AbstractAbyssCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Crush.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int CARDS = 1;

    // /STAT DECLARATION/


    public Crush() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, new CrushDual());
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = CARDS;
        isMultiDamage = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractPower pow : AbstractDungeon.player.powers) {
            pow.onDamageAllEnemies(multiDamage);
        }

        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);

        int i = 0, sum = 0;
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                aM.tint.color.set(Color.BLUE.cpy());
                aM.tint.changeColor(Color.WHITE.cpy());
                aM.damage(new DamageInfo(p, multiDamage[i], damageType));
                if (aM.lastDamageTaken > 0) sum++;
            }
            i++;
        }

        this.addToBot(new MakeTempCardInExhaustAction(new Splash(), sum));

        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
        } else {
            this.addToTop(new WaitAction(0.1F));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            //rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            super.upgrade();
        }
    }
}
