package ShadowSiren.actions;

import ShadowSiren.cards.tempCards.Splash;
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

public class CrushAction extends AbstractGameAction {
    public final int[] damages;

    public CrushAction(AbstractCreature source, int[] damages, DamageInfo.DamageType type) {
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
                for (AbstractPower pow : AbstractDungeon.player.powers) {
                    pow.onDamageAllEnemies(damages);
                }

                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);

                int i = 0, sum = 0;
                for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                    if (!aM.isDeadOrEscaped()) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                        aM.tint.color.set(Color.BLUE.cpy());
                        aM.tint.changeColor(Color.WHITE.cpy());
                        aM.damage(new DamageInfo(AbstractDungeon.player, damages[i], damageType));
                        if (aM.lastDamageTaken > 0) sum++;
                    }
                    i++;
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                } else {
                    this.addToTop(new WaitAction(0.1F));
                    this.addToTop(new MakeTempCardInExhaustAction(new Splash(), sum));
                }
            }
        }
    }

    @Override
    protected boolean shouldCancelAction() {
        return AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead() || (this.source != null && this.source.isDying);
    }
}
