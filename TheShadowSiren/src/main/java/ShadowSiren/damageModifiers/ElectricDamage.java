package ShadowSiren.damageModifiers;

import IconsAddon.icons.AbstractCustomIcon;
import IconsAddon.icons.ElectricIcon;
import IconsAddon.util.DamageModifierHelper;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;

import java.util.ArrayList;

public class ElectricDamage extends AbstractVivianDamageModifier {
    public static final String ID = ShadowSirenMod.makeID("ElectricDamage");

    TooltipInfo electricTooltip = null;
    public boolean innate;

    public ElectricDamage() {
        this(TipType.DAMAGE, true);
    }

    public ElectricDamage(TipType tipType) {
        this(tipType, true);
    }

    public ElectricDamage(boolean innate) {
        this(TipType.DAMAGE, innate);
    }

    public ElectricDamage(TipType tipType, boolean innate) {
        super(ID, tipType);
        this.innate = innate;
        this.priority = Integer.MAX_VALUE;
    }

    @Override
    public void onDamageModifiedByBlock(DamageInfo info, int unblockedAmount, int blockedAmount, AbstractCreature targetHit) {
        if (targetHit instanceof AbstractMonster) {
            if (unblockedAmount+blockedAmount > 1) {
                this.addToTop(new AbstractGameAction() {
                    @Override
                    public void update() {
                        int damage = (unblockedAmount+blockedAmount)/3;
                        if (damage == 0) {
                             damage = 1;
                        }
                        target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                        if (target != null) {
                            AbstractDungeon.effectList.add(new LightningOrbActivateEffect(target.hb.cX, target.hb.cY));
                            CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.2f);
                            this.addToTop(new DamageAction(target, DamageModifierHelper.makeBoundDamageInfoFromArray(DamageModifierManager.getDamageMods(info), info.owner, damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, true));
                            //this.addToTop(new VFXAction(new LightningOrbActivateEffect(target.hb.cX, target.hb.cY)));
                            //this.addToTop(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.2f));
                        }
                        this.isDone = true;
                    }
                });
            }
        }
    }

    @Override
    public ArrayList<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> l = super.getCustomTooltips();
        if (electricTooltip == null) {
            electricTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        l.add(electricTooltip);
        return l;
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

    @Override
    public boolean isInherent() {
        return innate;
    }
}
