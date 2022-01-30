package ShadowSiren.blockTypes;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.ShockPower;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class ElectricBlock extends AbstractBlockModifier {

    public static final String ID = ShadowSirenMod.makeID("ElectricBlock");
    public final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final Color c = Color.YELLOW.cpy();

    public ElectricBlock() {}

    @Override
    public void onThisBlockDamaged(DamageInfo info, int lostAmount) {
        if (info.owner != null && info.owner != owner && lostAmount > 0) {
            this.addToBot(new ApplyPowerAction(info.owner, owner, new ShockPower(info.owner, lostAmount)));
        }
    }

    //    @Override
//    public void atStartOfTurnPreBlockLoss() {
//        ChargeModifier.triggerCharge();
//    }
//
//    @Override
//    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
//        if (info.type == DamageInfo.DamageType.NORMAL && target != owner) {
//            triggerEffect(target);
//        }
//    }
//
//    @Override
//    public void onAttacked(DamageInfo info, int damageAmount) {
//        if (info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner != this.owner) {
//            triggerEffect(info.owner);
//        }
//    }
//
//    private void triggerEffect(AbstractCreature target) {
//        this.addToBot(new DamageAction(target, DamageModifierHelper.makeBoundDamageInfo(electricContainer, owner, DAMAGE, DamageInfo.DamageType.THORNS), true));
//    }


    @Override
    public Priority priority() {
        return Priority.TOP;
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
        return new ElectricBlock();
    }

    @Override
    public String getName() {
        return cardStrings.NAME;
    }

    @Override
    public String getDescription() {
        //return cardStrings.EXTENDED_DESCRIPTION[0]+DAMAGE+cardStrings.EXTENDED_DESCRIPTION[1]+DAMAGE+cardStrings.EXTENDED_DESCRIPTION[2];
        return cardStrings.DESCRIPTION;
    }

    @Override
    public boolean isInherent() {
        return true;
    }
}
