package ShadowSiren.cards.abstractCards;

public abstract class AbstractElementalCard extends AbstractDynamicCard {
    public AbstractElementalCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }
}
