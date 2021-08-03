package ShadowSiren.cards.abstractCards;

import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.damageModifiers.ElectricDamage;

public abstract class AbstractElectricCard extends AbstractElementalCard {

    public AbstractElectricCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        this(id, img, cost, type, color, rarity, target, AbstractVivianDamageModifier.TipType.DAMAGE);
    }

    public AbstractElectricCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, AbstractVivianDamageModifier.TipType tipType) {
        super(id, img, cost, type, color, rarity, target);
        switch (type) {
            case ATTACK:
                this.setBackgroundTexture(ShadowSirenMod.ATTACK_HYPER, ShadowSirenMod.ATTACK_HYPER_PORTRAIT);
                break;
            case POWER:
                this.setBackgroundTexture(ShadowSirenMod.POWER_HYPER, ShadowSirenMod.POWER_HYPER_PORTRAIT);
                break;
            default:
                this.setBackgroundTexture(ShadowSirenMod.SKILL_HYPER, ShadowSirenMod.SKILL_HYPER_PORTRAIT);
                break;
        }
        DamageModifierManager.addModifier(this, new ElectricDamage(tipType));
    }
}
