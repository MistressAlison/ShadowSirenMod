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
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class CataclysmActionSingle extends AbstractGameAction {
    private final DamageInfo info;

    public CataclysmActionSingle(AbstractCreature target, DamageInfo info) {
        this.source = info.owner;
        this.info = info;
        this.target = target;
        this.actionType = ActionType.DAMAGE;
        this.duration = this.startDuration = Settings.ACTION_DUR_FASTER;
    }

    public void update() {
        if (duration == startDuration) {
            if (target == null) {
                this.isDone = true;
                return;
            }
            this.addToBot(new SFXAction("THUNDERCLAP", 0.05F));
            CardCrawlGame.sound.play("THUNDERCLAP", 0.1F);
            if (!target.isDeadOrEscaped()) {
                AbstractDungeon.effectList.add(new LightningEffect(target.drawX, target.drawY));
            }
        }

        tickDuration();

        if (this.isDone) {
            int alive = (int) AbstractDungeon.getMonsters().monsters.stream().filter(mon -> !mon.isDeadOrEscaped()).count();

            if (!target.isDeadOrEscaped()) {
                AbstractDungeon.effectList.add(new BurnToAshEffect(target.drawX, target.drawY));
                target.damage(info);
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
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped() && m.currentHealth > 0) {
                    current++;
                }
            }
            if (initial - current > 0) {
                this.addToTop(new GainEnergyAction(initial - current));
            }
            this.isDone = true;
        }
    }
}
