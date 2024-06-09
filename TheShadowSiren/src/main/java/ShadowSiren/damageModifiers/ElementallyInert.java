package ShadowSiren.damageModifiers;

import ShadowSiren.ShadowSirenMod;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;

import java.util.ArrayList;

public class ElementallyInert extends AbstractVivianDamageModifier {
    public static final String ID = ShadowSirenMod.makeID("ElementallyInert");

    TooltipInfo inertTooltip = null;

    public ElementallyInert() {
        super(ID, TipType.TYPE_AND_DAMAGE);
        isAnElement = false;
    }

    @Override
    public ArrayList<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> l = super.getCustomTooltips();
        if (inertTooltip == null) {
            inertTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        l.add(inertTooltip);
        return l;
    }

    @Override
    public String getCardDescriptor() {
        return cardStrings.NAME;
    }

    @Override
    public String getKeyword() {
        return "shadowsiren:inert";
    }

    @Override
    public boolean isInherent() {
        return true;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new ElementallyInert();
    }
}
