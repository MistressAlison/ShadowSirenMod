package ShadowSiren.damageModifiers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.icons.IceIcon;
import ShadowSiren.powers.ChillPower;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.ArrayList;

public class IceDamage extends AbstractVivianDamageModifier {
    public static final String ID = ShadowSirenMod.makeID("IceDamage");

    TooltipInfo iceTooltip = null;
    TooltipInfo iceDamageTooltip = null;
    TooltipInfo chillTooltip = null;
    public boolean innate;

    public IceDamage() {
        this(TipType.TYPE_AND_DAMAGE, true);
    }

    public IceDamage(TipType tipType) {
        this(tipType, true);
    }

    public IceDamage(boolean innate) {
        this(TipType.TYPE_AND_DAMAGE, innate);
    }

    public IceDamage(TipType tipType, boolean innate) {
        this(tipType, innate, true);
    }

    public IceDamage(TipType tipType, boolean innate, boolean autoAdd) {
        super(ID, tipType);
        this.innate = innate;
        this.automaticBindingForCards = autoAdd;
        this.priority = -2;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        this.addToTop(new ApplyPowerAction(target, info.owner, new ChillPower(target, 1), 1, true));
    }

    /*@Override
    public void onDamageModifiedByBlock(DamageInfo info, int unblockedAmount, int blockedAmount, AbstractCreature target) {
        if (unblockedAmount+blockedAmount > 0) {
            this.addToBot(new ApplyPowerAction(target, info.owner, new ChillPower(target, unblockedAmount+blockedAmount)));
            AbstractDungeon.effectList.add(new FrostOrbActivateEffect(target.hb.cX, target.hb.cY));
        }
    }*/

    @Override
    public ArrayList<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> l = super.getCustomTooltips();
        if (iceTooltip == null) {
            iceTooltip = new TooltipInfo(cardStrings.NAME, cardStrings.DESCRIPTION);
        }
        if (iceDamageTooltip == null) {
            iceDamageTooltip = new TooltipInfo(cardStrings.EXTENDED_DESCRIPTION[0], cardStrings.EXTENDED_DESCRIPTION[1]);
        }
        if (chillTooltip == null) {
            chillTooltip = new TooltipInfo(cardStrings.EXTENDED_DESCRIPTION[2], cardStrings.EXTENDED_DESCRIPTION[3]);
        }
        if (tipType == TipType.TYPE_AND_DAMAGE) {
            l.add(iceTooltip);
            l.add(iceDamageTooltip);
            l.add(chillTooltip);
        } else if (tipType == TipType.DAMAGE) {
            l.add(iceDamageTooltip);
            l.add(chillTooltip);
        }
        return l;
    }

    @Override
    public String getCardDescriptor() {
        return cardStrings.NAME;
    }

    @Override
    public AbstractCustomIcon getAccompanyingIcon() {
        return IceIcon.get();
    }

    @Override
    public String getKeyword() {
        return "shadowsiren:ice_damage";
    }

    @Override
    public boolean isInherent() {
        return innate;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new IceDamage(tipType, innate, automaticBindingForCards);
    }
}
