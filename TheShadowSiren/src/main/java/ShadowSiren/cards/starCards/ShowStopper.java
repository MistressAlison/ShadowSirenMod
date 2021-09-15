package ShadowSiren.cards.starCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractStarCard;
import ShadowSiren.cards.tempCards.TempCard;
import ShadowSiren.characters.Vivian;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ShowStopper extends AbstractStarCard implements TempCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ShowStopper.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 3;
    private static final int EFFECT = 30;
    private static final int UPGRADE_PLUS_EFFECT = 10;
    // /STAT DECLARATION/


    public ShowStopper() {
        this(false);
    }

    public ShowStopper(boolean showStarCost) {
        super(ID, IMG, TYPE, COLOR, TARGET, showStarCost);
        baseMagicNumber = magicNumber = EFFECT;
        purgeOnUse = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped() && aM.currentHealth <= magicNumber) {
                this.addToBot(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));
                this.addToBot(new VFXAction(new CollectorCurseEffect(aM.hb.cX, aM.hb.cY), 1.0F));
                this.addToBot(new InstantKillAction(aM));
                //this.addToBot(new SuicideAction(aM));
                //this.addToBot(new LoseHPAction(aM, aM, 99999));
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT);
            initializeDescription();
        }
    }

    @Override
    public int getSpawnCost() {
        return COST;
    }
}
