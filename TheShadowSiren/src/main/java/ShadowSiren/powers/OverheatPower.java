package ShadowSiren.powers;

import IconsAddon.cardmods.AddIconToDescriptionMod;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cardModifiers.AddIconHelper;
import ShadowSiren.cards.abstractCards.AbstractInertCard;
import ShadowSiren.cards.abstractCards.AbstractModdedCard;
import ShadowSiren.cards.interfaces.ElementallyInert;
import ShadowSiren.damageModifiers.FireDamage;
import ShadowSiren.patches.ElementalPatches;
import basemod.helpers.CardModifierManager;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class OverheatPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = ShadowSirenMod.makeID("OverheatPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public OverheatPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("explosive");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK){
            if (ElementalPatches.noElementalModifiers(card) && !(card instanceof ElementallyInert) && !(card instanceof AbstractInertCard)) {
                DamageModifierManager.addModifier(card, new FireDamage(false));
                CardModifierManager.addModifier(card, new AddIconHelper.AddFireIconMod(AddIconToDescriptionMod.DAMAGE));
                if (card instanceof AbstractModdedCard) {
                    ((AbstractModdedCard)card).setBackgroundTexture(ShadowSirenMod.ATTACK_HUGE, ShadowSirenMod.ATTACK_HUGE_PORTRAIT);
                }
            }
            card.baseDamage += amount;
            card.applyPowers();
            flash();
            this.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new OverheatPower(owner, amount);
    }

}
