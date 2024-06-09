package ShadowSiren.damageModifiers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.icons.FireIcon;
import ShadowSiren.powers.BurningPower;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.ArrayList;

public class FireDamage extends AbstractVivianDamageModifier {
    public static final String ID = ShadowSirenMod.makeID("FireDamage");

    TooltipInfo fireTooltip = null;
    TooltipInfo fireDamageTooltip = null;
    TooltipInfo burnTooltip = null;
    public boolean innate;

    public FireDamage() {
        this(TipType.TYPE_AND_DAMAGE, true);
    }

    public FireDamage(TipType tipType) {
        this(tipType, true);
    }

    public FireDamage(boolean innate) {
        this(TipType.TYPE_AND_DAMAGE, innate);
    }

    public FireDamage(TipType tipType, boolean innate) {
        this(tipType, innate, true);
    }

    public FireDamage(TipType tipType, boolean innate, boolean autoAdd) {
        super(ID, tipType);
        this.innate = innate;
        this.automaticBindingForCards = autoAdd;
        this.priority = -3;
    }

    /*@Override
    public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature targetHit) {
        if (lastDamageTaken > 0) {
            addToTop(new ApplyPowerAction(info.owner, info.owner, new VigorPower(info.owner, 3)));
        }
    }*/

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        this.addToTop(new ApplyPowerAction(target, info.owner, new BurningPower(target, 1), 1, true));
    }

    @Override
    public ArrayList<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> l = super.getCustomTooltips();
        if (fireTooltip == null) {
            fireTooltip = new TooltipInfo(cardStrings.NAME, cardStrings.DESCRIPTION);
        }
        if (fireDamageTooltip == null) {
            fireDamageTooltip = new TooltipInfo(cardStrings.EXTENDED_DESCRIPTION[0], cardStrings.EXTENDED_DESCRIPTION[1]);
        }
        if (burnTooltip == null) {
            burnTooltip = new TooltipInfo(cardStrings.EXTENDED_DESCRIPTION[2], cardStrings.EXTENDED_DESCRIPTION[3]);
        }
        if (tipType == TipType.TYPE_AND_DAMAGE) {
            l.add(fireTooltip);
            l.add(fireDamageTooltip);
            l.add(burnTooltip);
        } else if (tipType == TipType.DAMAGE) {
            l.add(fireDamageTooltip);
            l.add(burnTooltip);
        }
        return l;
    }

    @Override
    public String getCardDescriptor() {
        return cardStrings.NAME;
    }

    @Override
    public AbstractCustomIcon getAccompanyingIcon() {
        return FireIcon.get();
    }

    @Override
    public String getKeyword() {
        return "shadowsiren:fire_damage";
    }

    @Override
    public boolean isInherent() {
        return innate;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new FireDamage(tipType, innate, automaticBindingForCards);
    }
}
