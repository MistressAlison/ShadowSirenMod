package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.EasyXCostAction;
import ShadowSiren.cards.abstractCards.AbstractIceCard;
import ShadowSiren.cards.tempCards.IceShard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.ChillPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import java.util.Arrays;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class FlashFreeze extends AbstractIceCard {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(FlashFreeze.class.getSimpleName());
    public static final String IMG = makeCardPath("FlashFreeze.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = -1;

    // /STAT DECLARATION/

    public FlashFreeze() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        this.cardsToPreview = new IceShard();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new EasyXCostAction(this, (base, params) -> {
            int effect = base + Arrays.stream(params).sum();
            if (effect > 0) {
                AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.WHITE.cpy(), true));
                addToTop(new MakeTempCardInHandAction(cardsToPreview.makeStatEquivalentCopy(), effect));
                for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                    if (!aM.isDeadOrEscaped()) {
                        addToTop(new ApplyPowerAction(aM, p, new ChillPower(aM, effect), effect, true));
                        aM.tint.color = Color.BLUE.cpy();
                        aM.tint.changeColor(Color.WHITE.cpy());
                    }
                }
            }
            return true;
        }));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            cardsToPreview.upgrade();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
