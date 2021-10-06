package ShadowSiren.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class CataclysmAction extends AbstractGameAction {
    public int[] damage;

    public CataclysmAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type) {
        this.source = source;
        this.damage = amount;
        this.damageType = type;
        this.actionType = ActionType.DAMAGE;
        this.duration = this.startDuration = Settings.ACTION_DUR_FASTER;
    }

    public void update() {
        if (duration == startDuration) {
            this.addToBot(new SFXAction("THUNDERCLAP", 0.05F));
            CardCrawlGame.sound.play("THUNDERCLAP", 0.1F);
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    AbstractDungeon.effectList.add(new LightningEffect(m.drawX, m.drawY));
                }
            }
        }

        tickDuration();

        if (this.isDone) {
            int alive = 0;
            for (AbstractPower p : AbstractDungeon.player.powers) {
                p.onDamageAllEnemies(this.damage);
            }

            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    alive++;
                    AbstractDungeon.effectList.add(new BurnToAshEffect(m.drawX, m.drawY));
                    m.damage(new DamageInfo(this.source, this.damage[AbstractDungeon.getMonsters().monsters.indexOf(m)], this.damageType));
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (alive > 0) {
                this.addToBot(new CataclysmFollowupAction(alive));
            } else if (!Settings.FAST_MODE) {
                this.addToTop(new WaitAction(0.1F));
            }
        }
    }

    private static class CataclysmFollowupAction extends AbstractGameAction {
        int initial;

        public CataclysmFollowupAction(int initial) {
            this.initial = initial;
        }

        @Override
        public void update() {
            int current = 0;
            int killed;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped() && m.currentHealth > 0) {
                    current++;
                }
            }
            killed = initial - current;
            if (killed > 0) {
                this.addToTop(new GainEnergyAction(killed));
            }
            this.isDone = true;
        }
    }
}
