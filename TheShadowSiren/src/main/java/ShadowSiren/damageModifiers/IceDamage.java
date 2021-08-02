package ShadowSiren.damageModifiers;

import IconsAddon.icons.AbstractCustomIcon;
import IconsAddon.icons.IceIcon;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.ChillPower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbActivateEffect;

public class IceDamage extends AbstractVivianDamageModifier {
    public static final String ID = ShadowSirenMod.makeID("IceDamage");

    TooltipInfo iceTooltip = null;

    public IceDamage() {
        super(ID);
    }

    @Override
    public void onDamageModifiedByBlock(DamageInfo info, int unblockedAmount, int blockedAmount, AbstractCreature target) {
        if (unblockedAmount+blockedAmount > 0) {
            this.addToBot(new ApplyPowerAction(target, info.owner, new ChillPower(target, unblockedAmount+blockedAmount)));
            AbstractDungeon.effectList.add(new FrostOrbActivateEffect(target.hb.cX, target.hb.cY));
        }
    }

    @Override
    public TooltipInfo getCustomTooltip() {
        if (iceTooltip == null) {
            iceTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        return iceTooltip;
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
}
