package ShadowSiren.actions;

import ShadowSiren.powers.BurnPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.Iterator;

public class BlastBurnAction extends AbstractGameAction {
    public final int[] damages;

    public BlastBurnAction(AbstractCreature source, int[] damages, DamageInfo.DamageType type) {
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

                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);

                int i = 0, sum = 0;
                for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                    if (!aM.isDeadOrEscaped()) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AttackEffect.FIRE));
                        AbstractDungeon.effectList.add(new ExplosionSmallEffect(aM.hb.cX, aM.hb.cY));
                        aM.tint.color.set(Color.RED.cpy());
                        aM.tint.changeColor(Color.WHITE.cpy());
                        aM.damage(new DamageInfo(source, damages[i], damageType));
                        sum += aM.lastDamageTaken;
                    }
                    i++;
                }

                for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                    if (!aM.isDeadOrEscaped()) {
                        this.addToTop(new ApplyPowerAction(aM, source, new BurnPower(aM, source, sum), sum, true));
                    }
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
