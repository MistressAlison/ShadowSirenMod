package ShadowSiren.cards.abstractCards;

import ShadowSiren.cards.uniqueCards.UniqueCard;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractDualityCard extends AbstractDynamicCard {

    public AbstractDualityCard dualCard;

    public HashSet<String> masterKeywordSet = new HashSet<>();

    public AbstractDualityCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, AbstractDualityCard dualCard) {
        super(id, img, cost, type, color, rarity, target);
        masterKeywordSet.addAll(keywords);
        if (dualCard != null) {
            this.dualCard = dualCard;
            dualCard.dualCard = this;
            this.cardsToPreview = dualCard.makeStatEquivalentCopy();
            masterKeywordSet.addAll(dualCard.keywords);
        }
    }

    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        if (this.dualCard != null || this instanceof UniqueCard) {
            tags.add(BaseMod.getKeywordTitle("shadowsiren:duality"));
            tags.addAll(super.getCardDescriptors());
        }
        return tags;
    }

    public void buffDualDamage(int amount) {
        this.dualCard.damage += amount;
        this.dualCard.baseDamage += amount;
        this.dualCard.applyPowers();
        this.cardsToPreview = this.dualCard;
    }

    public void buffDualBlock(int amount) {
        this.dualCard.block += amount;
        this.dualCard.baseBlock += amount;
        this.dualCard.applyPowers();
        this.cardsToPreview = this.dualCard;
    }

    @Override
    public void upgrade() {
        if (dualCard != null && dualCard.canUpgrade()) {
            dualCard.upgrade();
            cardsToPreview.upgrade();
        }
    }

    @Override
    public void initializeDescription() {
        super.initializeDescription();
        if(masterKeywordSet != null) {
            for (String s : masterKeywordSet) {
                if (!this.keywords.contains(s)) {
                    this.keywords.add(s);
                }
            }
        }
    }

    @Override
    public void renderCardTip(SpriteBatch sb) {
        //ArrayList<String> backup = new ArrayList<>(keywords);
        if(masterKeywordSet != null) {
            for (String s : masterKeywordSet) {
                if (!this.keywords.contains(s)) {
                    this.keywords.add(s);
                }
            }
        }
        super.renderCardTip(sb);
        //keywords = backup;
    }
}
