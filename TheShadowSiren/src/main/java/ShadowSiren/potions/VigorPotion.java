package ShadowSiren.potions;

import ShadowSiren.ShadowSirenMod;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class VigorPotion extends CustomPotion {


    public static final String POTION_ID = ShadowSirenMod.makeID("VigorPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public VigorPotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.FAIRY, PotionColor.FIRE);

        // Do you throw this potion at an enemy or do you just consume it.
        isThrown = false;
    }

    @Override
    public void use(AbstractCreature target) {
        this.getPotency(AbstractDungeon.ascensionLevel);
        AbstractPlayer p = AbstractDungeon.player;
        this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, potency)));
    }

    // This is your potency.
    @Override
    public int getPotency(final int ascensionLevel) {
        return 10;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
        /*tips.add(new PowerTip(
                BaseMod.getKeywordTitle("vigor"),
                BaseMod.getKeywordDescription("vigor")
        ));*/
        tips.add(new PowerTip(
                TipHelper.capitalize(GameDictionary.VIGOR.NAMES[0]),
                GameDictionary.keywords.get(GameDictionary.VIGOR.NAMES[0])));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new VigorPotion();
    }
}