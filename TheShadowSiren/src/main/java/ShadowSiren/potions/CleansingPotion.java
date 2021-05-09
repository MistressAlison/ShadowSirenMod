package ShadowSiren.potions;

import ShadowSiren.ShadowSirenMod;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.ExhaustAllNonAttackAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class CleansingPotion extends CustomPotion {


    public static final String POTION_ID = ShadowSirenMod.makeID("CleansingPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public CleansingPotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
        super(NAME, POTION_ID, AbstractPotion.PotionRarity.UNCOMMON, PotionSize.HEART, AbstractPotion.PotionColor.FIRE);

        // Do you throw this potion at an enemy or do you just consume it.
        isThrown = false;
    }

    @Override
    public void use(AbstractCreature target) {
        this.getPotency(AbstractDungeon.ascensionLevel);
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractPower pow : p.powers) {
            if (pow.type == AbstractPower.PowerType.DEBUFF) {
                this.addToBot(new RemoveSpecificPowerAction(p, p, pow));
            }
        }
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            //this.addToBot(new ExhaustAction(false, true, true));
            for (AbstractCard c : p.hand.group) {
                if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) {
                    this.addToTop(new ExhaustSpecificCardAction(c, p.hand));
                }
            }
        }
    }

    // This is your potency.
    @Override
    public int getPotency(final int ascensionLevel) {
        return 0;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0];
        tips.clear();
        tips.add(new PowerTip(name, description));
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.EXHAUST.NAMES[0]), GameDictionary.keywords.get(GameDictionary.EXHAUST.NAMES[0])));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new CleansingPotion();
    }
}