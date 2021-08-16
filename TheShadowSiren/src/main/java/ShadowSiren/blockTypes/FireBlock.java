package ShadowSiren.blockTypes;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import IconsAddon.util.DamageModifierHelper;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.damageModifiers.FireDamage;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class FireBlock extends AbstractBlockModifier {

    public static final String ID = ShadowSirenMod.makeID("FireBlock");
    public final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final Color c = Color.RED.cpy();

    public FireBlock() {}

    @Override
    public void atEndOfRound() {
        this.addToBot(new ApplyPowerAction(owner, owner, new VigorPower(owner, getCurrentAmount())));
    }

    @Override
    public Color blockImageColor() {
        return c;
    }

    @Override
    public Color blockTextColor() {
        return c;
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new FireBlock();
    }

    @Override
    public String getName() {
        return cardStrings.NAME;
    }

    @Override
    public String getDescription() {
        return cardStrings.DESCRIPTION;
    }

    @Override
    public boolean isInherent() {
        return true;
    }
}
