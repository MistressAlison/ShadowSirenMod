package ShadowSiren.cards.abstractCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.SwapPrismaticCardsAction;
import ShadowSiren.cards.abstractCards.prismatics.*;
import ShadowSiren.cards.interfaces.MultiCardPreviewHack;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.util.CardChainHelper;
import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPrismaticCard extends AbstractDynamicCard implements MultiCardPreviewHack {

    public AbstractPrismaticBaseCard baseCard;
    public AbstractPrismaticVeilCard veilCard;
    public AbstractPrismaticAbyssCard abyssCard;
    public AbstractPrismaticSmokeCard smokeCard;
    public AbstractPrismaticHugeCard hugeCard;
    public AbstractPrismaticHyperCard hyperCard;

    private final ArrayList<AbstractCard> previews = new ArrayList<>();
    public float duration = 0;
    public int state = 0;

    public AbstractPrismaticCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        this(id, img, cost, type, color, rarity, target, null, null, null, null, null, null);
    }

    public AbstractPrismaticCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, AbstractPrismaticBaseCard baseCard, AbstractPrismaticVeilCard veilCard, AbstractPrismaticAbyssCard abyssCard, AbstractPrismaticSmokeCard smokeCard, AbstractPrismaticHugeCard hugeCard, AbstractPrismaticHyperCard hyperCard) {
        super(id, img, cost, type, color, rarity, target);
        initializeFormCards(baseCard, veilCard, abyssCard, smokeCard, hugeCard, hyperCard);
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

    @Override
    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        if (this instanceof AbstractPrismaticBaseCard) {
            tags.add(BaseMod.getKeywordTitle("shadowsiren:prismatic_base"));
            tags.addAll(super.getCardDescriptors());
        }
        if (this instanceof AbstractPrismaticVeilCard) {
            tags.add(BaseMod.getKeywordTitle("shadowsiren:prismatic_veil"));
            tags.addAll(super.getCardDescriptors());
        }
        if (this instanceof AbstractPrismaticAbyssCard) {
            tags.add(BaseMod.getKeywordTitle("shadowsiren:prismatic_abyss"));
            tags.addAll(super.getCardDescriptors());
        }
        if (this instanceof AbstractPrismaticSmokeCard) {
            tags.add(BaseMod.getKeywordTitle("shadowsiren:prismatic_smoke"));
            tags.addAll(super.getCardDescriptors());
        }
        if (this instanceof AbstractPrismaticHugeCard) {
            tags.add(BaseMod.getKeywordTitle("shadowsiren:prismatic_huge"));
            tags.addAll(super.getCardDescriptors());
        }
        if (this instanceof AbstractPrismaticHyperCard) {
            tags.add(BaseMod.getKeywordTitle("shadowsiren:prismatic_hyper"));
            tags.addAll(super.getCardDescriptors());
        }
        return tags;
    }

    /*@Override
    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        if (this instanceof AbstractPrismaticBaseCard) {
            tags.add(BaseMod.getKeywordTitle("shadowsiren:prismatic_base"));

        } else if (this instanceof AbstractPrismaticVeilCard) {
            tags.add(BaseMod.getKeywordTitle("shadowsiren:prismatic_veil"));

        } else if (this instanceof AbstractPrismaticAbyssCard) {
            tags.add(BaseMod.getKeywordTitle("shadowsiren:prismatic_abyss"));

        } else if (this instanceof AbstractPrismaticSmokeCard) {
            tags.add(BaseMod.getKeywordTitle("shadowsiren:prismatic_smoke"));

        } else if (this instanceof AbstractPrismaticHugeCard) {
            tags.add(BaseMod.getKeywordTitle("shadowsiren:prismatic_huge"));

        } else if (this instanceof AbstractPrismaticHyperCard) {
            tags.add(BaseMod.getKeywordTitle("shadowsiren:prismatic_hyper"));

        } else {
            tags.add(BaseMod.getKeywordTitle("shadowsiren:prismatic"));

        }
        tags.addAll(super.getCardDescriptors());
        return tags;
    }*/

    @Override
    public void upgrade() {
        if (cardsToPreview != null && cardsToPreview.canUpgrade()) {
            cardsToPreview.upgrade();
        }
        if (baseCard != null && baseCard.canUpgrade()) {
            baseCard.upgrade();
        }
        if (veilCard != null && veilCard.canUpgrade()) {
            veilCard.upgrade();
        }
        if (abyssCard != null && abyssCard.canUpgrade()) {
            abyssCard.upgrade();
        }
        if (smokeCard != null && smokeCard.canUpgrade()) {
            smokeCard.upgrade();
        }
        if (hugeCard != null && hugeCard.canUpgrade()) {
            hugeCard.upgrade();
        }
        if (hyperCard != null && hyperCard.canUpgrade()) {
            hyperCard.upgrade();
        }
        for (AbstractCard c : previews) {
            if (c.canUpgrade()) {
                c.upgrade();
            }
        }
    }

    public boolean hasAlternateForm() {
        return baseCard != null || veilCard != null || abyssCard != null || smokeCard != null || hugeCard != null | hyperCard != null;
    }

    @Override
    public void update() {
        super.update();
        if (previews.size() > 0) {
            duration += Gdx.graphics.getDeltaTime();
            if (duration > 3.0f) {
                state = (state + 1) % previews.size();
                cardsToPreview = previews.get(state);
                duration = 0.0f;
            }
        }
    }

    /*@Override
    public AbstractCard getCurrentPreview(AbstractCard oldPreview) {
        return CardChainHelper.findCardInList((AbstractPrismaticCard) oldPreview);
    }*/

    public void initializeFormCards(AbstractPrismaticBaseCard baseCard, AbstractPrismaticVeilCard veilCard, AbstractPrismaticAbyssCard abyssCard, AbstractPrismaticSmokeCard smokeCard, AbstractPrismaticHugeCard hugeCard, AbstractPrismaticHyperCard hyperCard) {
        if (baseCard != null) {
            this.baseCard = baseCard;
            previews.add(baseCard);
        }
        if (veilCard != null) {
            this.veilCard = veilCard;
            previews.add(veilCard);
        }
        if (abyssCard != null) {
            this.abyssCard = abyssCard;
            previews.add(abyssCard);
        }
        if (smokeCard != null) {
            this.smokeCard = smokeCard;
            previews.add(smokeCard);
        }
        if (hugeCard != null) {
            this.hugeCard = hugeCard;
            previews.add(hugeCard);
        }
        if (hyperCard != null) {
            this.hyperCard = hyperCard;
            previews.add(hyperCard);
        }
        if (previews.size() > 0) {
            this.cardsToPreview = previews.get(0);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        switch (AbstractDungeon.player.stance.ID) {
            case "ShadowSiren:VeilStance" :
                this.glowColor = ShadowSirenMod.VOODOO.cpy();
                if (this.veilCard != null) this.addToTop(new SwapPrismaticCardsAction(this, (AbstractPrismaticCard) this.veilCard.makeStatEquivalentCopy()));
                break;
            case "ShadowSiren:AbyssStance" :
                this.glowColor = Color.BLUE.cpy();
                if (this.abyssCard != null) this.addToTop(new SwapPrismaticCardsAction(this, (AbstractPrismaticCard) this.abyssCard.makeStatEquivalentCopy()));
                break;
            case "ShadowSiren:SmokeStance" :
                this.glowColor = Color.GRAY.cpy();
                if (this.smokeCard != null) this.addToTop(new SwapPrismaticCardsAction(this, (AbstractPrismaticCard) this.smokeCard.makeStatEquivalentCopy()));
                break;
            case "ShadowSiren:HugeStance" :
                this.glowColor = Color.RED.cpy();
                if (this.hugeCard != null) this.addToTop(new SwapPrismaticCardsAction(this, (AbstractPrismaticCard) this.hugeCard.makeStatEquivalentCopy()));
                break;
            case "ShadowSiren:HyperStance" :
                this.glowColor = Color.YELLOW.cpy();
                if (this.hyperCard != null) this.addToTop(new SwapPrismaticCardsAction(this, (AbstractPrismaticCard) this.hyperCard.makeStatEquivalentCopy()));
                break;
            default:
                if (this.baseCard != null) this.addToTop(new SwapPrismaticCardsAction(this, (AbstractPrismaticCard) this.baseCard.makeStatEquivalentCopy()));
                break;
        }
    }
}
