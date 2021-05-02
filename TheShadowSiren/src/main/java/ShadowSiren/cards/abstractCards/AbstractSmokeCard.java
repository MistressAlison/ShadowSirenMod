package ShadowSiren.cards.abstractCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.SwapDualityCardsAction;
import ShadowSiren.cards.uniqueCards.UniqueCard;
import ShadowSiren.stances.AbyssStance;
import ShadowSiren.stances.SmokeStance;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSmokeCard extends AbstractDualityCard {

    public boolean glowOnStance = true; //Set false for cards that only shift and have no bonuses in the given stance
    private static ArrayList<TooltipInfo> dualityTooltip;

    public AbstractSmokeCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        this(id, img, cost, type, color, rarity, target, null);
    }

    public AbstractSmokeCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, AbstractDualityCard dualCard) {
        super(id, img, cost, type, color, rarity, target, dualCard);
        switch (type) {
            case ATTACK:
                this.setBackgroundTexture(ShadowSirenMod.ATTACK_SMOKE, ShadowSirenMod.ATTACK_SMOKE_PORTRAIT);
                break;
            case POWER:
                this.setBackgroundTexture(ShadowSirenMod.POWER_SMOKE, ShadowSirenMod.POWER_SMOKE_PORTRAIT);
                break;
            default:
                this.setBackgroundTexture(ShadowSirenMod.SKILL_SMOKE, ShadowSirenMod.SKILL_SMOKE_PORTRAIT);
                break;
        }
        //setOrbTexture(ShadowSirenMod.ENERGY_ORB_VOID, ShadowSirenMod.ENERGY_ORB_VOID_PORTRAIT);
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (glowOnStance && AbstractDungeon.player.stance.ID.equals(SmokeStance.STANCE_ID)) {
            this.glowColor = Color.GRAY.cpy();
        }
        if (this.dualCard != null) {
            if (AbstractDungeon.player.stance.ID.equals(SmokeStance.STANCE_ID) ^ this instanceof UniqueCard) {
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
            dualityTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("shadowsiren:smoke_duality"), BaseMod.getKeywordDescription("shadowsiren:smoke_duality")));
        }
        if (this.dualCard != null || this instanceof UniqueCard) {
            compoundList.addAll(dualityTooltip);
        }
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }
}
