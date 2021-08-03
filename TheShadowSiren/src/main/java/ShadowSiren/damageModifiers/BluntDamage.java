package ShadowSiren.damageModifiers;

import IconsAddon.icons.AbstractCustomIcon;
import IconsAddon.icons.ImpactIcon;
import ShadowSiren.ShadowSirenMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.ArrayList;

public class BluntDamage extends AbstractVivianDamageModifier {
    public static final String ID = ShadowSirenMod.makeID("BluntDamage");

    TooltipInfo bluntTooltip = null;

    public BluntDamage() {
        super(ID, TipType.NONE);
        isAnElement = false;
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        if (target.currentBlock > 0) {
            return damage*1.5f;
        }
        return damage;
    }

    @Override
    public ArrayList<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> l = super.getCustomTooltips();
        if (bluntTooltip == null) {
            bluntTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        l.add(bluntTooltip);
        return l;
    }

    @Override
    public String getCardDescriptor() {
        return cardStrings.NAME;
    }

    @Override
    public AbstractCustomIcon getAccompanyingIcon() {
        return ImpactIcon.get();
    }

    @Override
    public String getKeyword() {
        return "shadowsiren:blunt_damage";
    }
}
