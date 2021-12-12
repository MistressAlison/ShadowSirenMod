package ShadowSiren.damageModifiers;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.icons.AbstractCustomIcon;
import IconsAddon.icons.FireIcon;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.VulcanizePower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;

public class FireDamage extends AbstractVivianDamageModifier {
    public static final String ID = ShadowSirenMod.makeID("FireDamage");

    TooltipInfo fireTooltip = null;
    TooltipInfo fireDamageTooltip = null;
    TooltipInfo fireBlockTooltip = null;
    public boolean innate;

    public FireDamage() {
        this(TipType.DAMAGE, true);
    }

    public FireDamage(TipType tipType) {
        this(tipType, true);
    }

    public FireDamage(boolean innate) {
        this(TipType.DAMAGE, innate);
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

    //TODO this is actually worse
    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        //return damage * (1 + 0.25f*((float)target.currentHealth/target.maxHealth));
        return ((float)target.currentHealth/target.maxHealth) >= 0.75f ? damage * 1.25f : damage;
    }

    @Override
    public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature targetHit) {
        if (lastDamageTaken > 0) {
            this.addToTop(new AbstractGameAction() {
                boolean firstPass = true;
                @Override
                public void update() {
                    if (firstPass) {
                        firstPass = false;
                        this.duration = 0.05f;
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(targetHit.hb.cX, targetHit.hb.cY, AbstractGameAction.AttackEffect.FIRE));
                        float multiplier = 1;
                        if (info.owner.hasPower(VulcanizePower.POWER_ID)) {
                            multiplier += (info.owner.getPower(VulcanizePower.POWER_ID).amount*VulcanizePower.INCREASE_PERCENT/100f);
                            info.owner.getPower(VulcanizePower.POWER_ID).flash();
                        }
                        targetHit.decreaseMaxHealth((int)(lastDamageTaken*multiplier));
                    }
                    tickDuration();
                }
            });
        }
    }

    @Override
    public ArrayList<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> l = super.getCustomTooltips();
        if (fireTooltip == null) {
            fireTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        if (fireDamageTooltip == null) {
            fireDamageTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[1]);
        }
        if (fireBlockTooltip == null) {
            fireBlockTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[2]);
        }
        switch (tipType) {
            case DAMAGE_AND_BLOCK:
                l.add(fireTooltip);
                break;
            case DAMAGE:
                l.add(fireDamageTooltip);
                break;
            case BLOCK:
                l.add(fireBlockTooltip);
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
