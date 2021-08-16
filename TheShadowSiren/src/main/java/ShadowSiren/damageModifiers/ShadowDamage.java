package ShadowSiren.damageModifiers;

import IconsAddon.icons.AbstractCustomIcon;
import IconsAddon.icons.DarkIcon;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.ShadowPower;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;

import javax.tools.Tool;
import java.util.ArrayList;

public class ShadowDamage extends AbstractVivianDamageModifier {
    public static final String ID = ShadowSirenMod.makeID("ShadowDamage");

    TooltipInfo shadowTooltip = null;
    TooltipInfo shadowDamageTooltip = null;
    TooltipInfo shadowBlockTooltip = null;
    TooltipInfo shadowSplitTooltip = null;
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
        super(ID, tipType);
        this.innate = innate;
        this.priority = Integer.MAX_VALUE;
    }

    @Override
    public boolean ignoresBlock(AbstractCreature target) {
        return true;
    }

    @Override
    public boolean ignoresTempHP(AbstractCreature target) {
        AbstractPower pow = target.getPower(ShadowPower.POWER_ID);
        if (pow instanceof ShadowPower) {
            return !((ShadowPower) pow).checkIfSurpassedHP();
        } else {
            return true;
        }
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        AbstractPower pow = target.getPower(ShadowPower.POWER_ID);
        if (pow instanceof ShadowPower && ((ShadowPower) pow).checkIfSurpassedHP()) {
            return damageAmount;
        } else if (damageAmount > 0) {
            this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new ShadowPower(target, damageAmount),damageAmount, true));
            AbstractDungeon.effectList.add(new CardPoofEffect(target.hb.cX, target.hb.cY));
        }
        return 0;
    }

    @Override
    public ArrayList<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> l = super.getCustomTooltips();
        if (shadowTooltip == null) {
            shadowTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        if (shadowDamageTooltip == null) {
            shadowDamageTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[1]);
        }
        if (shadowBlockTooltip == null) {
            shadowBlockTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[2]);
        }
        if (shadowSplitTooltip == null) {
            shadowSplitTooltip = new TooltipInfo(BaseMod.getKeywordTitle("shadowsiren:shadow_split"), BaseMod.getKeywordDescription("shadowsiren:shadow_split"));
        }
        switch (tipType) {
            case DAMAGE_AND_BLOCK:
                l.add(shadowTooltip);
                break;
            case DAMAGE:
                l.add(shadowDamageTooltip);
                break;
            case BLOCK:
                l.add(shadowBlockTooltip);
                break;
        }
        l.add(shadowSplitTooltip);
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
}
