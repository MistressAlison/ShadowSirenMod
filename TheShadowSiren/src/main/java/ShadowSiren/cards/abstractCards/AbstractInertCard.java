package ShadowSiren.cards.abstractCards;

import ShadowSiren.damageModifiers.ElementallyInert;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;

public abstract class AbstractInertCard extends AbstractDynamicCard {
    public AbstractInertCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
        DamageModifierManager.addModifier(this, new ElementallyInert());
    }
}
