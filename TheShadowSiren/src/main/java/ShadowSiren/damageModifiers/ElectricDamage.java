package ShadowSiren.damageModifiers;

import IconsAddon.icons.AbstractCustomIcon;
import IconsAddon.icons.ElectricIcon;
import IconsAddon.util.DamageModifierHelper;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;

public class ElectricDamage extends AbstractVivianDamageModifier {
    public static final String ID = ShadowSirenMod.makeID("ElectricDamage");

    TooltipInfo electricTooltip = null;
    static Object electricContainer = null;

    public ElectricDamage() {
        super(ID);
        if (electricContainer == null) {
            electricContainer = new Object();
            DamageModifierManager.addModifier(electricContainer, new ElectricDamage());
        }
    }

    @Override
    public void onDamageModifiedByBlock(DamageInfo info, int unblockedAmount, int blockedAmount, AbstractCreature targetHit) {
        if (targetHit instanceof AbstractMonster) {
            if (unblockedAmount+blockedAmount > 1) {
                this.addToTop(new AbstractGameAction() {
                    @Override
                    public void update() {
                        target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                        if (target != null) {
                            this.addToTop(new DamageAction(target, DamageModifierHelper.makeBoundDamageInfo(electricContainer, info.owner, (unblockedAmount+blockedAmount)/2, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, true));
                            this.addToTop(new VFXAction(new LightningOrbActivateEffect(target.hb.cX, target.hb.cY)));
                            this.addToTop(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.2f));
                        }
                        this.isDone = true;
                    }
                });
            }
        }
    }

    @Override
    public TooltipInfo getCustomTooltip() {
        if (electricTooltip == null) {
            electricTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        return electricTooltip;
    }

    @Override
    public String getCardDescriptor() {
        return cardStrings.NAME;
    }

    @Override
    public AbstractCustomIcon getAccompanyingIcon() {
        return ElectricIcon.get();
    }

    @Override
    public String getKeyword() {
        return "shadowsiren:electric_damage";
    }
}
