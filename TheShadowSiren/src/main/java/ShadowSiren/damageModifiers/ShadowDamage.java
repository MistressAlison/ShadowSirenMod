package ShadowSiren.damageModifiers;

import IconsAddon.icons.AbstractCustomIcon;
import IconsAddon.icons.DarkIcon;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.ShadowPower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;

public class ShadowDamage extends AbstractVivianDamageModifier {
    public static final String ID = ShadowSirenMod.makeID("ShadowDamage");

    TooltipInfo shadowTooltip = null;

    public ShadowDamage() {
        super(ID);
    }

    @Override
    public boolean ignoresBlock() {
        return true;
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        AbstractPower pow = target.getPower(ShadowPower.POWER_ID);
        if (pow instanceof ShadowPower && ((ShadowPower) pow).checkIfSurpassedHP()) {
            return damageAmount;
        } else {
            this.addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new ShadowPower(target, damageAmount),damageAmount, true));
            this.addToTop(new VFXAction(new CardPoofEffect(target.hb.cX, target.hb.cY)));
            return 0;
        }
    }

    @Override
    public TooltipInfo getCustomTooltip() {
        if (shadowTooltip == null) {
            shadowTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        return shadowTooltip;
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
    public int priority() {
        return Integer.MAX_VALUE;
    }
}
