package ShadowSiren.cards.abstractCards;

import ShadowSiren.ShadowSirenMod;

public abstract class AbstractMultiElementCard extends AbstractElementalCard {

    public AbstractMultiElementCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
        switch (type) {
            case ATTACK:
                this.setBackgroundTexture(ShadowSirenMod.ATTACK_PRISMATIC, ShadowSirenMod.ATTACK_PRISMATIC_PORTRAIT);
                break;
            case POWER:
                this.setBackgroundTexture(ShadowSirenMod.POWER_PRISMATIC, ShadowSirenMod.POWER_PRISMATIC_PORTRAIT);
                break;
            default:
                this.setBackgroundTexture(ShadowSirenMod.SKILL_PRISMATIC, ShadowSirenMod.SKILL_PRISMATIC_PORTRAIT);
                break;
        }
    }
}
