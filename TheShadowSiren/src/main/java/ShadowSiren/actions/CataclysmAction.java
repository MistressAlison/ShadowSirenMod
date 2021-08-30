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
            int energy = 0;
            for (AbstractPower p : AbstractDungeon.player.powers) {
                p.onDamageAllEnemies(this.damage);
            }

            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    AbstractDungeon.effectList.add(new BurnToAshEffect(m.drawX, m.drawY));
                    m.damage(new DamageInfo(this.source, this.damage[AbstractDungeon.getMonsters().monsters.indexOf(m)], this.damageType));
                    if ((m.isDying || m.currentHealth <= 0) && !m.halfDead) {
                        energy++;
                    }
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (energy > 0) {
                this.addToTop(new GainEnergyAction(energy));
            } else if (!Settings.FAST_MODE) {
                this.addToTop(new WaitAction(0.1F));
            }
        }
    }
}
