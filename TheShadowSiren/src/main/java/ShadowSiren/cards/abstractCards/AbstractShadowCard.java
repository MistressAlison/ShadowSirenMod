package ShadowSiren.cards.abstractCards;

import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.damageModifiers.ShadowDamage;

public abstract class AbstractShadowCard extends AbstractElementalCard {

    public AbstractShadowCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        this(id, img, cost, type, color, rarity, target, AbstractVivianDamageModifier.TipType.DAMAGE);
    }

    public AbstractShadowCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, AbstractVivianDamageModifier.TipType tipType) {
        super(id, img, cost, type, color, rarity, target);
        switch (type) {
            case ATTACK:
                this.setBackgroundTexture(ShadowSirenMod.ATTACK_SHADOW, ShadowSirenMod.ATTACK_SHADOW_PORTRAIT);
                break;
            case POWER:
                this.setBackgroundTexture(ShadowSirenMod.POWER_SHADOW, ShadowSirenMod.POWER_SHADOW_PORTRAIT);
                break;
            default:
                this.setBackgroundTexture(ShadowSirenMod.SKILL_SHADOW, ShadowSirenMod.SKILL_SHADOW_PORTRAIT);
                break;
        }
        DamageModifierManager.addModifier(this, new ShadowDamage(tipType));
    }
}
