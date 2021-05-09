package ShadowSiren.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

public class AddFasterTemporaryHPAction extends AddTemporaryHPAction {
    public static final float DURATION = Settings.ACTION_DUR_XFAST;
    public AddFasterTemporaryHPAction(AbstractCreature target, AbstractCreature source, int amount) {
        super(target, source, amount);
        this.duration = DURATION;
    }

    @Override
    public void update() {
        if (this.duration == DURATION) {
            TempHPField.tempHp.set(this.target, (Integer)TempHPField.tempHp.get(this.target) + this.amount);
            if (this.amount > 0) {
                AbstractDungeon.effectsQueue.add(new HealEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY, this.amount));
                this.target.healthBarUpdatedEvent();
            }
        }

        this.tickDuration();
    }
}
