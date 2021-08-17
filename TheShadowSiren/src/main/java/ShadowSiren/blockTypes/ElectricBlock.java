package ShadowSiren.blockTypes;

import IconsAddon.blockModifiers.AbstractBlockModifier;
import IconsAddon.util.DamageModifierHelper;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.damageModifiers.ElectricDamage;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class ElectricBlock extends AbstractBlockModifier {

    public static final String ID = ShadowSirenMod.makeID("ElectricBlock");
    public final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final Color c = Color.YELLOW.cpy();
    private static Object electricContainer;
    private static final int DAMAGE = 3;

    public ElectricBlock() {
        if (electricContainer == null) {
            electricContainer = new Object();
            DamageModifierManager.addModifier(electricContainer, new ElectricDamage());
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type == DamageInfo.DamageType.NORMAL && target != owner) {
            triggerEffect(target);
        }
    }

    @Override
    public void onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner != this.owner) {
            triggerEffect(info.owner);
        }
    }

    private void triggerEffect(AbstractCreature target) {
        this.addToBot(new DamageAction(target, DamageModifierHelper.makeBoundDamageInfo(electricContainer, owner, DAMAGE, DamageInfo.DamageType.THORNS)));
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
        return new ElectricBlock();
    }

    @Override
    public String getName() {
        return cardStrings.NAME;
    }

    @Override
    public String getDescription() {
        return cardStrings.EXTENDED_DESCRIPTION[0]+DAMAGE+cardStrings.EXTENDED_DESCRIPTION[1]+DAMAGE+cardStrings.EXTENDED_DESCRIPTION[2];
    }

    @Override
    public boolean isInherent() {
        return true;
    }
}
