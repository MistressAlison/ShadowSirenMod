package ShadowSiren.cards.abstractCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.SwapDualityCardsAction;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.stances.HugeStance;
import ShadowSiren.stances.VeilStance;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHugeCard extends AbstractDualityCard {

    public boolean glowOnStance = true; //Set false for cards that only shift and have no bonuses in the given stance
    private static ArrayList<TooltipInfo> dualityTooltip;

    public AbstractHugeCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        this(id, img, cost, type, color, rarity, target, null);
    }

    public AbstractHugeCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, AbstractDualityCard dualCard) {
        super(id, img, cost, type, color, rarity, target, dualCard);
        switch (type) {
            case ATTACK:
                this.setBackgroundTexture(ShadowSirenMod.ATTACK_HUGE, ShadowSirenMod.ATTACK_HUGE_PORTRAIT);
                break;
            case POWER:
                this.setBackgroundTexture(ShadowSirenMod.POWER_HUGE, ShadowSirenMod.POWER_HUGE_PORTRAIT);
                break;
            default:
                this.setBackgroundTexture(ShadowSirenMod.SKILL_HUGE, ShadowSirenMod.SKILL_HUGE_PORTRAIT);
                break;
        }
        //setOrbTexture(ShadowSirenMod.ENERGY_ORB_SHADOW, ShadowSirenMod.ENERGY_ORB_SHADOW_PORTRAIT);
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (glowOnStance && AbstractDungeon.player.stance.ID.equals(HugeStance.STANCE_ID)) {
            this.glowColor = Color.RED.cpy();
        }
        if (this.dualCard != null) {
            if (AbstractDungeon.player.stance.ID.equals(HugeStance.STANCE_ID) ^ this instanceof UniqueCard) {
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
            dualityTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("shadowsiren:huge_duality"), BaseMod.getKeywordDescription("shadowsiren:huge_duality")));
        }
        if (this.dualCard != null || this instanceof UniqueCard) {
            compoundList.addAll(dualityTooltip);
        }
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }

}
