package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ReturningStrike extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ReturningStrike.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int HITS = 2;

    // /STAT DECLARATION/

    public ReturningStrike() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = HITS;
        shuffleBackIntoDrawPile = true;
        isMultiDamage = true;
        this.tags.add(CardTags.STRIKE);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0 ; i < magicNumber ; i++) {
            if (i % 2 == 0) {
                for (int k = 0 ; k < AbstractDungeon.getMonsters().monsters.size() ; k++) {
                    if (!AbstractDungeon.getMonsters().monsters.get(k).isDeadOrEscaped()) {
                        this.addToBot(new DamageAction(AbstractDungeon.getMonsters().monsters.get(k), new DamageInfo(p, multiDamage[k], damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
                    }
                }
            } else {
                for (int k = AbstractDungeon.getMonsters().monsters.size()-1 ; k >= 0 ; k--) {
                    if (!AbstractDungeon.getMonsters().monsters.get(k).isDeadOrEscaped()) {
                        this.addToBot(new DamageAction(AbstractDungeon.getMonsters().monsters.get(k), new DamageInfo(p, multiDamage[k], damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
                    }
                }
            }
        }
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractPower pow : p.powers) {
                    pow.onDamageAllEnemies(multiDamage);
                }
                this.isDone = true;
            }
        });
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
