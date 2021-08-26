package ShadowSiren.blockTypes;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.ShadowPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ShadowBlock extends AbstractBlockModifier {

    public static final String ID = ShadowSirenMod.makeID("ShadowBlock");
    public final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final Color c = Color.DARK_GRAY.cpy();

    public ShadowBlock() {}

    @Override
    public int amountLostAtStartOfTurn() {
        return Math.max(0, getCurrentAmount() - countShadowSplit());
    }

    @Override
    public Color blockImageColor() {
        return c;
    }

    @Override
    public Color blockTextColor() {
        return c;
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new ShadowBlock();
    }

    @Override
    public String getName() {
        return cardStrings.NAME;
    }

    @Override
    public String getDescription() {
        return cardStrings.DESCRIPTION;
    }

    @Override
    public boolean isInherent() {
        return true;
    }

    private int countShadowSplit() {
        int ret = 0;
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                if (aM.hasPower(ShadowPower.POWER_ID)) {
                    ret += aM.getPower(ShadowPower.POWER_ID).amount;
                }
            }
        }
        return Math.max(0, ret);
    }
}
