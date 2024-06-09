package ShadowSiren.cards.abstractCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.damageModifiers.IceDamage;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;

public abstract class AbstractIceCard extends AbstractElementalCard {

    public AbstractIceCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        this(id, img, cost, type, color, rarity, target, AbstractVivianDamageModifier.TipType.TYPE_AND_DAMAGE);
    }

    public AbstractIceCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, AbstractVivianDamageModifier.TipType tipType) {
        super(id, img, cost, type, color, rarity, target);
        switch (type) {
            case ATTACK:
                this.setBackgroundTexture(ShadowSirenMod.ATTACK_VOID, ShadowSirenMod.ATTACK_VOID_PORTRAIT);
                break;
            case POWER:
                this.setBackgroundTexture(ShadowSirenMod.POWER_VOID, ShadowSirenMod.POWER_VOID_PORTRAIT);
                break;
            default:
                this.setBackgroundTexture(ShadowSirenMod.SKILL_VOID, ShadowSirenMod.SKILL_VOID_PORTRAIT);
                break;
        }
        DamageModifierManager.addModifier(this, new IceDamage(tipType));
    }
}
