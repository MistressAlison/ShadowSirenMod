package ShadowSiren.damageModifiers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.icons.IceIcon;
import ShadowSiren.powers.ChillPower;
import basemod.BaseMod;
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
    TooltipInfo iceBlockTooltip = null;
    TooltipInfo chillTooltip = null;
    public boolean innate;

    public IceDamage() {
        this(TipType.DAMAGE, true);
    }

    public IceDamage(TipType tipType) {
        this(tipType, true);
    }

    public IceDamage(boolean innate) {
        this(TipType.DAMAGE, innate);
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
        this.addToBot(new ApplyPowerAction(target, info.owner, new ChillPower(target, 1), 1, true));
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
            iceTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        if (iceDamageTooltip == null) {
            iceDamageTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[1]);
        }
        if (iceBlockTooltip == null) {
            iceBlockTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[2]);
        }
        if (chillTooltip == null) {
            chillTooltip = new TooltipInfo(BaseMod.getKeywordTitle("shadowsiren:chill"), BaseMod.getKeywordDescription("shadowsiren:chill"));
        }
        switch (tipType) {
            case DAMAGE_AND_BLOCK:
                l.add(iceTooltip);
                l.add(chillTooltip);
                break;
            case DAMAGE:
                l.add(iceDamageTooltip);
                l.add(chillTooltip);
                break;
            case BLOCK:
                l.add(iceBlockTooltip);
                l.add(chillTooltip);
                break;
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
