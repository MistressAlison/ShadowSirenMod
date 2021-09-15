package ShadowSiren.damageModifiers;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.icons.AbstractCustomIcon;
import IconsAddon.icons.ElectricIcon;
import IconsAddon.util.DamageModifierHelper;
import IconsAddon.util.DamageModifierManager;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.EnergyConversionPower;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
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
    TooltipInfo electricBlockTooltip = null;
    TooltipInfo shockTooltip = null;
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
        this(tipType, innate, true);
    }

    public ElectricDamage(TipType tipType, boolean innate, boolean autoAdd) {
        super(ID, tipType);
        this.innate = innate;
        this.automaticBindingForCards = autoAdd;
        this.priority = -1;
    }

    @Override
    public void onDamageModifiedByBlock(DamageInfo info, int unblockedAmount, int blockedAmount, AbstractCreature targetHit) {
        if (targetHit instanceof AbstractMonster && !LoopCastCheck.loopCast.get(info)) {
            if (unblockedAmount+blockedAmount > 1) {
                this.addToTop(new AbstractGameAction() {
                    @Override
                    public void update() {
                        targetHit.tint.color = Color.YELLOW.cpy();
                        targetHit.tint.changeColor(Color.WHITE.cpy());
                        int damage = (unblockedAmount+blockedAmount)/DIVISOR;
                        if (damage == 0) {
                             damage = 1;
                        }
                        target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                        if (target != null) {
                            AbstractDungeon.effectList.add(new LightningOrbActivateEffect(target.hb.cX, target.hb.cY));
                            CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.2f);
                            DamageInfo di = DamageModifierHelper.makeBoundDamageInfoFromArray(DamageModifierManager.getDamageMods(info), info.owner, damage, DamageInfo.DamageType.THORNS);
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
            electricTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);
        }
        if (electricDamageTooltip == null) {
            electricDamageTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[1]);
        }
        if (electricBlockTooltip == null) {
            electricBlockTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[2]);
        }
        if (shockTooltip == null) {
            shockTooltip = new TooltipInfo(BaseMod.getKeywordTitle("shadowsiren:shock"), BaseMod.getKeywordDescription("shadowsiren:shock"));
        }
        switch (tipType) {
            case DAMAGE_AND_BLOCK:
                l.add(electricTooltip);
                l.add(shockTooltip);
                break;
            case DAMAGE:
                l.add(electricDamageTooltip);
                break;
            case BLOCK:
                l.add(electricBlockTooltip);
                l.add(shockTooltip);
                break;
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
