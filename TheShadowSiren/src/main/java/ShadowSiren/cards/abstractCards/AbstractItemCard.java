package ShadowSiren.cards.abstractCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.ImmediateExhaustCardAction;
import ShadowSiren.patches.TypeOverridePatch;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractItemCard extends AbstractDynamicCard {

    private static ArrayList<TooltipInfo> itemTooltip;
    public boolean hasUses;
    public boolean isActive;

    public AbstractItemCard(String id, String img, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, -2, type, color, rarity, target);
        TypeOverridePatch.setOverride(this, BaseMod.getKeywordTitle("shadowsiren:item"));
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
    }

    /*public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add(BaseMod.getKeywordTitle("shadowsiren:item"));
        tags.addAll(super.getCardDescriptors());
        return tags;
    }*/

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (itemTooltip == null)
        {
            itemTooltip = new ArrayList<>();
            itemTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("shadowsiren:item"), BaseMod.getKeywordDescription("shadowsiren:item")));
        }
        List<TooltipInfo> compoundList = new ArrayList<>(itemTooltip);
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }

    public void checkIfActive() {
        checkUses();
        this.isActive = isInHand() && hasUses;
    }

    public void checkUses() {
        this.hasUses = usesCount > 0;
        this.selfRetain = hasUses; //Retain while we have uses
        this.exhaust = !hasUses; //Exhaust and Ethereal if we dont. This is for if we pull it back from the exhaust pile with no uses
        this.isEthereal = !hasUses;
    }

    @Override
    public void moveToDiscardPile() {
        super.moveToDiscardPile();
        this.isActive = false;
    }

    @Override
    public void teleportToDiscardPile() {
        super.teleportToDiscardPile();
        this.isActive = false;
    }

    /*@Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        this.isActive = false;
    }*/

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        checkIfActive();
    }

    public boolean isInHand() {
        if (AbstractDungeon.player == null) {
            return false;
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c == this) return true;
        }
        return false;
    }

    public void modifyUses(int uses) {
        this.usesCount += uses;
        this.isUsesCountModified = this.usesCount != this.baseUsesCount;
        checkUses();
        this.selfRetain = hasUses; //Retain while we have uses
        this.exhaust = !hasUses; //Exhaust and Ethereal if we dont. This is for if we pull it back from the exhaust pile with no uses
        this.isEthereal = !hasUses;
        if (!hasUses) { //If we hit 0, or below 0 somehow, exhaust immediately
            //rawDescription = this.spentDescription;
            initializeDescription(); //Initialize before we move to exhaust
            this.unhover();
            this.untip();
            this.stopGlowing();
            AbstractDungeon.actionManager.removeFromQueue(this);
            this.addToTop(new ImmediateExhaustCardAction(this)); //Hijack this wither code I wrote before, lol
        } else {
            //We dont set active is true here, since it might not be in our hand, and shouldnt be active if it isnt
            //rawDescription = this.DESCRIPTION;
            initializeDescription();
        }
    }

    public void applyEffect() { //If we need additional effects, they can be defined on a card by card basis, and simply call this to know to decrement the uses
        //Maybe make the card flash or something nifty?
        this.flash();
        modifyUses(-1); //Reduce our uses by 1 each time the effect happens
        initializeDescription();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractItemCard copy = (AbstractItemCard) super.makeStatEquivalentCopy();
        copy.hasUses = hasUses;
        return copy;
    }
}
