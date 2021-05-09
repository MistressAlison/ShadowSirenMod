package ShadowSiren.actions;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.BloodShotEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class BloodBarrageAction extends AbstractGameAction {
    public final int[] damages;

    public BloodBarrageAction(AbstractCreature source, int[] damages, DamageInfo.DamageType type) {
        this.source = source;
        this.damages = damages;
        this.actionType = ActionType.DAMAGE;
        this.damageType = type;
        this.startDuration = Settings.ACTION_DUR_XFAST;
        this.duration = this.startDuration;
    }

    public void update() {
        if (this.shouldCancelAction()) {
            this.isDone = true;
        } else {
            this.tickDuration();
            if (this.isDone) {

                for (AbstractPower p : AbstractDungeon.player.powers) {
                    p.onDamageAllEnemies(this.damages);
                }

                //CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);

                int i = 0, sum = 0;
                for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                    if (!aM.isDeadOrEscaped()) {
                        //
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AttackEffect.SLASH_HEAVY, i != 0));
                        aM.tint.color.set(Color.RED.cpy());
                        aM.tint.changeColor(Color.WHITE.cpy());
                        aM.damage(new DamageInfo(source, damages[i], damageType));
                        sum += aM.lastDamageTaken;
                        //AbstractDungeon.effectList.add(new BloodShotEffect(aM.hb.cX, aM.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, aM.lastDamageTaken));
                        //this.addToTop(new VFXAction(new BloodShotEffect(aM.hb.cX, aM.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, aM.lastDamageTaken), 0.25F));
                        this.addToTop(new AddFasterTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, aM.lastDamageTaken));
                    }
                    i++;
                }

                //this.addToTop(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, sum));
                for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                    if (!aM.isDeadOrEscaped()) {
                        //this.addToTop(new AddFasterTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, aM.lastDamageTaken));
                        this.addToTop(new VFXAction(new BloodShotEffect(aM.hb.cX, aM.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, aM.lastDamageTaken), 0.15F));
                    }
                    i++;
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                } else {
                    this.addToTop(new WaitAction(0.1F));
                }
            }
        }
    }

    @Override
    protected boolean shouldCancelAction() {
        return AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead() || (this.source != null && this.source.isDying);
    }
}
