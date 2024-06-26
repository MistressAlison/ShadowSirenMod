package ShadowSiren.damageModifiers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.icons.ElectricIcon;
import ShadowSiren.powers.EnergyConversionPower;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;

import java.util.ArrayList;

public class ElectricDamage extends AbstractVivianDamageModifier {
    @SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
    public static class LoopCastCheck {
        public static SpireField<Boolean> loopCast = new SpireField<>(() -> false);
    }
    public static final String ID = ShadowSirenMod.makeID("ElectricDamage");

    public static final int DIVISOR = 2;

    TooltipInfo electricTooltip = null;
    TooltipInfo electricDamageTooltip = null;
    public boolean innate;

    public ElectricDamage() {
        this(TipType.TYPE_AND_DAMAGE, true);
    }

    public ElectricDamage(TipType tipType) {
        this(tipType, true);
    }

    public ElectricDamage(boolean innate) {
        this(TipType.TYPE_AND_DAMAGE, innate);
    }

    public ElectricDamage(TipType tipType, boolean innate) {
        this(tipType, innate, true);
    }

    public ElectricDamage(TipType tipType, boolean innate, boolean autoAdd) {
        super(ID, tipType);
        this.innate = innate;
        this.automaticBindingForCards = autoAdd;
        this.priority = -1;
    }

    @Override
    public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature targetHit) {
        if (targetHit instanceof AbstractMonster && !LoopCastCheck.loopCast.get(info)) {
            if (lastDamageTaken > 0) {
                this.addToTop(new AbstractGameAction() {
                    @Override
                    public void update() {
                        targetHit.tint.color = Color.YELLOW.cpy();
                        targetHit.tint.changeColor(Color.WHITE.cpy());
                        int damage = Math.max(1, lastDamageTaken/DIVISOR);
                        target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                        if (target != null) {
                            AbstractDungeon.effectList.add(new LightningOrbActivateEffect(target.hb.cX, target.hb.cY));
                            CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.2f);
                            DamageInfo di = BindingHelper.makeInfo(DamageModifierManager.getDamageMods(info), info.owner, damage, DamageInfo.DamageType.THORNS);
                            LoopCastCheck.loopCast.set(di, true);
                            this.addToTop(new DamageAction(target, di, AbstractGameAction.AttackEffect.NONE, true));
                            //this.addToTop(new VFXAction(new LightningOrbActivateEffect(target.hb.cX, target.hb.cY)));
                            //this.addToTop(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.2f));
                        }
                        this.isDone = true;
                    }
                });
            }
        }
        if (info.owner != null) {
            AbstractPower p = info.owner.getPower(EnergyConversionPower.POWER_ID);
            if (p != null) {
                p.onSpecificTrigger();
            }
        }
    }

    @Override
    public ArrayList<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> l = super.getCustomTooltips();
        if (electricTooltip == null) {
            electricTooltip = new TooltipInfo(cardStrings.NAME, cardStrings.DESCRIPTION);
        }
        if (electricDamageTooltip == null) {
            electricDamageTooltip = new TooltipInfo(cardStrings.EXTENDED_DESCRIPTION[0], cardStrings.EXTENDED_DESCRIPTION[1]);
        }
        if (tipType == TipType.TYPE_AND_DAMAGE) {
            l.add(electricTooltip);
            l.add(electricDamageTooltip);
        } else if (tipType == TipType.DAMAGE) {
            l.add(electricDamageTooltip);
        }
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

    @Override
    public AbstractDamageModifier makeCopy() {
        return new ElectricDamage(tipType, innate, automaticBindingForCards);
    }
}
