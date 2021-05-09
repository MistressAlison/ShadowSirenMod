package ShadowSiren.potions;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.FreezePower;
import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class FreezePotion extends CustomPotion {


    public static final String POTION_ID = ShadowSirenMod.makeID("FreezePotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public FreezePotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.SPIKY, PotionColor.FIRE);

        // Do you throw this potion at an enemy or do you just consume it.
        isThrown = true;
    }

    @Override
    public void use(AbstractCreature target) {
        this.getPotency(AbstractDungeon.ascensionLevel);
        AbstractPlayer p = AbstractDungeon.player;
        if (target instanceof AbstractMonster) {
            this.addToBot(new ApplyPowerAction(target, p, new FreezePower((AbstractMonster) target, potency), potency, true));
        }
    }

    // This is your potency.
    @Override
    public int getPotency(final int ascensionLevel) {
        return 1;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(
                BaseMod.getKeywordTitle("shadowsiren:freeze"),
                BaseMod.getKeywordDescription("shadowsiren:freeze")
        ));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new FreezePotion();
    }
}