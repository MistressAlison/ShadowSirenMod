package ShadowSiren.damageModifiers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.icons.DarkIcon;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.ArrayList;

public class ShadowDamage extends AbstractVivianDamageModifier {
    public static final String ID = ShadowSirenMod.makeID("ShadowDamage");

    TooltipInfo shadowTooltip = null;
    TooltipInfo shadowDamageTooltip = null;
    public boolean innate;

    public ShadowDamage() {
        this(TipType.DAMAGE, true);
    }

    public ShadowDamage(TipType tipType) {
        this(tipType, true);
    }

    public ShadowDamage(boolean innate) {
        this(TipType.DAMAGE, innate);
    }

    public ShadowDamage(TipType tipType, boolean innate) {
        this(tipType, innate, true);
    }

    public ShadowDamage(TipType tipType, boolean innate, boolean autoAdd) {
        super(ID, tipType);
        this.innate = innate;
        this.automaticBindingForCards = autoAdd;
        this.priority = 0;
    }

    @Override
    public boolean ignoresBlock(AbstractCreature target) {
        return true;
    }

    @Override
    public ArrayList<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> l = super.getCustomTooltips();
        if (shadowTooltip == null) {
            shadowTooltip = new TooltipInfo(cardStrings.NAME, cardStrings.DESCRIPTION);
        }
        if (shadowDamageTooltip == null) {
            shadowDamageTooltip = new TooltipInfo(cardStrings.EXTENDED_DESCRIPTION[0], cardStrings.EXTENDED_DESCRIPTION[1]);
        }
        l.add(shadowTooltip);
        l.add(shadowDamageTooltip);
        return l;
    }

    @Override
    public String getCardDescriptor() {
        return cardStrings.NAME;
    }

    @Override
    public AbstractCustomIcon getAccompanyingIcon() {
        return DarkIcon.get();
    }

    @Override
    public String getKeyword() {
        return "shadowsiren:shadow_damage";
    }

    @Override
    public boolean isInherent() {
        return innate;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new ShadowDamage(tipType, innate, automaticBindingForCards);
    }
}
