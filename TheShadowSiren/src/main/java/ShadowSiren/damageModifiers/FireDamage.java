package ShadowSiren.damageModifiers;

import IconsAddon.icons.AbstractCustomIcon;
import IconsAddon.icons.FireIcon;
import ShadowSiren.ShadowSirenMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class FireDamage extends AbstractVivianDamageModifier {
    public static final String ID = ShadowSirenMod.makeID("FireDamage");

    TooltipInfo fireTooltip = null;

    public FireDamage() {
        super(ID);
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        return damage * (1 + 0.25f*((float)target.currentHealth/target.maxHealth));
    }

    @Override
    public void onDamageModifiedByBlock(DamageInfo info, int unblockedAmount, int blockedAmount, AbstractCreature target) {
        super.onDamageModifiedByBlock(info, unblockedAmount, blockedAmount, target);
        target.maxHealth -= unblockedAmount;
        if (target.maxHealth <= 0) {
            target.maxHealth = 1;
        }
        AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public TooltipInfo getCustomTooltip() {
        if (fireTooltip == null) {
            fireTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        return fireTooltip;
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
}
