package ShadowSiren.oldStuff.cards.abstractCards.prismatics;

import ShadowSiren.oldStuff.cards.abstractCards.AbstractPrismaticCard;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPrismaticHyperCard extends AbstractPrismaticCard {

    private static ArrayList<TooltipInfo> prismTooltip;

    public AbstractPrismaticHyperCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }

    public AbstractPrismaticHyperCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, AbstractPrismaticBaseCard baseCard, AbstractPrismaticVeilCard veilCard, AbstractPrismaticAbyssCard abyssCard, AbstractPrismaticSmokeCard smokeCard, AbstractPrismaticHugeCard hugeCard, AbstractPrismaticHyperCard hyperCard) {
        super(id, img, cost, type, color, rarity, target, baseCard, veilCard, abyssCard, smokeCard, hugeCard, hyperCard);
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (prismTooltip == null)
        {
            prismTooltip = new ArrayList<>();
            prismTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("shadowsiren:prismatic_hyper"), BaseMod.getKeywordDescription("shadowsiren:prismatic_hyper")));
        }
        List<TooltipInfo> compoundList = new ArrayList<>(prismTooltip);
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }
}
