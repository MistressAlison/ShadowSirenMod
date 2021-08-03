package ShadowSiren.cards.abstractCards;

import IconsAddon.util.DamageModifierManager;
import ShadowSiren.damageModifiers.ElementallyInert;

public abstract class AbstractInertCard extends AbstractDynamicCard {
    public AbstractInertCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
        DamageModifierManager.addModifier(this, new ElementallyInert());
    }
}
