package ShadowSiren.cards.abstractCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.damageModifiers.FireDamage;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;

public abstract class AbstractFireCard extends AbstractElementalCard {

    public AbstractFireCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        this(id, img, cost, type, color, rarity, target, AbstractVivianDamageModifier.TipType.TYPE_AND_DAMAGE);
    }

    public AbstractFireCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, AbstractVivianDamageModifier.TipType tipType) {
        super(id, img, cost, type, color, rarity, target);
        switch (type) {
            case ATTACK:
                this.setBackgroundTexture(ShadowSirenMod.ATTACK_HUGE, ShadowSirenMod.ATTACK_HUGE_PORTRAIT);
                break;
            case POWER:
                this.setBackgroundTexture(ShadowSirenMod.POWER_HUGE, ShadowSirenMod.POWER_HUGE_PORTRAIT);
                break;
            default:
                this.setBackgroundTexture(ShadowSirenMod.SKILL_HUGE, ShadowSirenMod.SKILL_HUGE_PORTRAIT);
                break;
        }
        DamageModifierManager.addModifier(this, new FireDamage(tipType));
    }
}
