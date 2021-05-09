package ShadowSiren.cards.abstractCards.prismatics;

import ShadowSiren.cards.abstractCards.AbstractPrismaticCard;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPrismaticHugeCard extends AbstractPrismaticCard {

    private static ArrayList<TooltipInfo> prismTooltip;

    public AbstractPrismaticHugeCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }

    public AbstractPrismaticHugeCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, AbstractPrismaticBaseCard baseCard, AbstractPrismaticVeilCard veilCard, AbstractPrismaticAbyssCard abyssCard, AbstractPrismaticSmokeCard smokeCard, AbstractPrismaticHugeCard hugeCard, AbstractPrismaticHyperCard hyperCard) {
        super(id, img, cost, type, color, rarity, target, baseCard, veilCard, abyssCard, smokeCard, hugeCard, hyperCard);
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (prismTooltip == null)
        {
            prismTooltip = new ArrayList<>();
            prismTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("shadowsiren:prismatic_huge"), BaseMod.getKeywordDescription("shadowsiren:prismatic_huge")));
        }
        List<TooltipInfo> compoundList = new ArrayList<>(prismTooltip);
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }
}
