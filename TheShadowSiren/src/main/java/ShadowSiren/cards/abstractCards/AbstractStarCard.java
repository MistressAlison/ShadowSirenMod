package ShadowSiren.cards.abstractCards;

import ShadowSiren.ShadowSirenMod;

public abstract class AbstractStarCard extends AbstractDynamicCard {

    public AbstractStarCard(String id, String img, CardType type, CardColor color, CardTarget target) {
        super(id, img, 0, type, color, CardRarity.SPECIAL, target);
        switch (type) {
            case ATTACK:
                this.setBackgroundTexture(ShadowSirenMod.ATTACK_STAR, ShadowSirenMod.ATTACK_STAR_PORTRAIT);
                break;
            case POWER:
                this.setBackgroundTexture(ShadowSirenMod.POWER_STAR, ShadowSirenMod.POWER_STAR_PORTRAIT);
                break;
            default:
                this.setBackgroundTexture(ShadowSirenMod.SKILL_STAR, ShadowSirenMod.SKILL_STAR_PORTRAIT);
                break;
        }
    }

    public abstract int getSpawnCost();
}
