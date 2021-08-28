package ShadowSiren.cards.abstractCards;

import ShadowSiren.ShadowSirenMod;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStarCard extends AbstractDynamicCard {

    private static ArrayList<TooltipInfo> starTooltip;

    public AbstractStarCard(String id, String img, CardType type, CardColor color, CardTarget target, boolean showStarCost) {
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
        if (showStarCost) {
            this.rawDescription = EXTENDED_DESCRIPTION[0];
            initializeDescription();
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (starTooltip == null)
        {
            starTooltip = new ArrayList<>();
            starTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("shadowsiren:star"), BaseMod.getKeywordDescription("shadowsiren:star")));
            starTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("shadowsiren:star_power"), BaseMod.getKeywordDescription("shadowsiren:star_power")));
        }
        List<TooltipInfo> compoundList = new ArrayList<>(starTooltip);
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }

    public abstract int getSpawnCost();

}
