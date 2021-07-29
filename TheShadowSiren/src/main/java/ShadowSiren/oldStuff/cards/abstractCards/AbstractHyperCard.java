package ShadowSiren.oldStuff.cards.abstractCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.SwapDualityCardsAction;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.stances.HyperStance;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHyperCard extends AbstractDualityCard {

    public boolean glowOnStance = true; //Set false for cards that only shift and have no bonuses in the given stance
    private static ArrayList<TooltipInfo> dualityTooltip;

    public AbstractHyperCard(String id, String img, int cost, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        this(id, img, cost, type, color, rarity, target, null);
    }

    public AbstractHyperCard(String id, String img, int cost, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target, AbstractDualityCard dualCard) {
        super(id, img, cost, type, color, rarity, target, dualCard);
        switch (type) {
            case ATTACK:
                this.setBackgroundTexture(ShadowSirenMod.ATTACK_HYPER, ShadowSirenMod.ATTACK_HYPER_PORTRAIT);
                break;
            case POWER:
                this.setBackgroundTexture(ShadowSirenMod.POWER_HYPER, ShadowSirenMod.POWER_HYPER_PORTRAIT);
                break;
            default:
                this.setBackgroundTexture(ShadowSirenMod.SKILL_HYPER, ShadowSirenMod.SKILL_HYPER_PORTRAIT);
                break;
        }
        //setOrbTexture(ShadowSirenMod.ENERGY_ORB_SHADOW, ShadowSirenMod.ENERGY_ORB_SHADOW_PORTRAIT);
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (glowOnStance && AbstractDungeon.player.stance.ID.equals(HyperStance.STANCE_ID)) {
            this.glowColor = Color.YELLOW.cpy();
        }
        if (this.dualCard != null) {
            if (AbstractDungeon.player.stance.ID.equals(HyperStance.STANCE_ID) ^ this instanceof UniqueCard) {
                this.addToTop(new SwapDualityCardsAction(this, (AbstractDualityCard) this.dualCard.makeStatEquivalentCopy()));
            }
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        List<TooltipInfo> compoundList = new ArrayList<>();
        if (dualityTooltip == null)
        {
            dualityTooltip = new ArrayList<>();
            dualityTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("shadowsiren:hyper_duality"), BaseMod.getKeywordDescription("shadowsiren:hyper_duality")));
        }
        if (this.dualCard != null || this instanceof UniqueCard) {
            compoundList.addAll(dualityTooltip);
        }
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }

}
