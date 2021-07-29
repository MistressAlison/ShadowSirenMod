package ShadowSiren.oldStuff.cards.abstractCards;

import ShadowSiren.cards.abstractCards.AbstractDynamicCard;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractDualityCard extends AbstractDynamicCard {

    public AbstractDualityCard dualCard;

    public HashSet<String> masterKeywordSet = new HashSet<>();

    public AbstractDualityCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, AbstractDualityCard dualCard) {
        super(id, img, cost, type, color, rarity, target);
        masterKeywordSet.addAll(keywords);
        setupDualCard(dualCard);
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

    private void setupDualCard(AbstractDualityCard dualCard) {
        if (dualCard != null) {
            this.dualCard = (AbstractDualityCard) dualCard.makeStatEquivalentCopyNoDual();
            this.dualCard.dualCard = this;
            this.cardsToPreview = this.dualCard;
            masterKeywordSet.addAll(dualCard.keywords);
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractDualityCard copy = (AbstractDualityCard) super.makeStatEquivalentCopy();
        copy.masterKeywordSet.addAll(this.keywords);
        copy.setupDualCard(this.dualCard);
        return copy;
    }

    public AbstractCard makeStatEquivalentCopyNoDual() {
        AbstractDualityCard copy = (AbstractDualityCard) super.makeStatEquivalentCopy();
        copy.masterKeywordSet.addAll(this.keywords);
        return copy;
    }
}
