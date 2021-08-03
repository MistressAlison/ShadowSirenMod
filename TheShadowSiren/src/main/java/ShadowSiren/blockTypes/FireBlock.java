package ShadowSiren.blockTypes;

import IconsAddon.blockModifiers.AbstractCustomBlockType;
import IconsAddon.util.DamageModifierHelper;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.damageModifiers.FireDamage;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class FireBlock extends AbstractCustomBlockType {

    public static final String ID = ShadowSirenMod.makeID("FireBlock");
    public final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static Object fireContainer;
    private static final int DAMAGE = 3;

    public FireBlock(AbstractCreature owner, int amount) {
        super(owner, amount);
        if (fireContainer == null) {
            fireContainer = new Object();
            DamageModifierManager.addModifier(fireContainer, new FireDamage());
        }
    }

    @Override
    public void onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.owner != owner) {
            this.addToBot(new DamageAction(info.owner, DamageModifierHelper.makeBoundDamageInfo(fireContainer, owner, DAMAGE, DamageInfo.DamageType.THORNS)));
        }
    }

    @Override
    public String getName() {
        return cardStrings.NAME;
    }

    @Override
    public String getDescription() {
        return cardStrings.EXTENDED_DESCRIPTION[0] + DAMAGE + cardStrings.EXTENDED_DESCRIPTION[1];
    }
}
