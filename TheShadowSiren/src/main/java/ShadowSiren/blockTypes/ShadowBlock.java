package ShadowSiren.blockTypes;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.ShadowSplitPower;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class ShadowBlock extends AbstractBlockModifier {

    public static final String ID = ShadowSirenMod.makeID("ShadowBlock");
    public final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final Color c = Color.DARK_GRAY.cpy();

    private static final int AMOUNT = 3;

    public ShadowBlock() {}

    @Override
    public void onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner != this.owner) {
            this.addToBot(new ApplyPowerAction(info.owner, owner, new ShadowSplitPower(info.owner, AMOUNT)));
        }
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

//    private int countShadowSplit() {
//        int ret = 0;
//        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
//            if (!aM.isDeadOrEscaped()) {
//                if (aM.hasPower(ShadowPower.POWER_ID)) {
//                    ret += aM.getPower(ShadowPower.POWER_ID).amount;
//                }
//            }
//        }
//        return Math.max(0, ret);
//    }
}
