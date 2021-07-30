package ShadowSiren.blockTypes;

import IconsAddon.blockModifiers.AbstractCustomBlockType;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.ChillPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class IceBlock extends AbstractCustomBlockType {

    public static final String ID = ShadowSirenMod.makeID("IceBlock");
    public final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public IceBlock(AbstractCreature owner, int amount) {
        super(owner, amount);
    }

    @Override
    public void onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.owner != owner) {
            this.addToBot(new ApplyPowerAction(info.owner, owner, new ChillPower(info.owner, damageAmount)));
        }
    }

    @Override
    public String getName() {
        return cardStrings.NAME;
    }

    @Override
    public String getDescription() {
        return cardStrings.DESCRIPTION;
    }
}
