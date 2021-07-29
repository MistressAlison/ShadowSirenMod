package ShadowSiren.oldStuff.cards.abstractCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.SwapDualityCardsAction;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.stances.VeilStance;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractShadowCard extends AbstractDualityCard {

    public boolean glowOnStance = true; //Set false for cards that only shift and have no bonuses in the given stance
    private static ArrayList<TooltipInfo> dualityTooltip;

    public AbstractShadowCard(String id, String img, int cost, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        this(id, img, cost, type, color, rarity, target, null);
    }

    public AbstractShadowCard(String id, String img, int cost, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target, AbstractDualityCard dualCard) {
        super(id, img, cost, type, color, rarity, target, dualCard);
        switch (type) {
            case ATTACK:
                this.setBackgroundTexture(ShadowSirenMod.ATTACK_SHADOW, ShadowSirenMod.ATTACK_SHADOW_PORTRAIT);
                break;
            case POWER:
                this.setBackgroundTexture(ShadowSirenMod.POWER_SHADOW, ShadowSirenMod.POWER_SHADOW_PORTRAIT);
                break;
            default:
                this.setBackgroundTexture(ShadowSirenMod.SKILL_SHADOW, ShadowSirenMod.SKILL_SHADOW_PORTRAIT);
                break;
        }
        //setOrbTexture(ShadowSirenMod.ENERGY_ORB_SHADOW, ShadowSirenMod.ENERGY_ORB_SHADOW_PORTRAIT);
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (glowOnStance && AbstractDungeon.player.stance.ID.equals(VeilStance.STANCE_ID)) {
            this.glowColor = ShadowSirenMod.VOODOO.cpy();
        }
        if (this.dualCard != null) {
            if (AbstractDungeon.player.stance.ID.equals(VeilStance.STANCE_ID) ^ this instanceof UniqueCard) {
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
            dualityTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("shadowsiren:shadow_duality"), BaseMod.getKeywordDescription("shadowsiren:shadow_duality")));
        }
        if (this.dualCard != null || this instanceof UniqueCard) {
            compoundList.addAll(dualityTooltip);
        }
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }
}
