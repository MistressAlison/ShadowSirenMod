package ShadowSiren.actions;

import ShadowSiren.relics.DataCollector;
import ShadowSiren.relics.VoltShroom;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class ElectricDamageAction extends AbstractGameAction {
    private final DamageInfo info;
    private static final float DURATION = 0.1F;

    public ElectricDamageAction(AbstractCreature target, DamageInfo info, AttackEffect effect) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
    }

    public void update() {
        if (this.shouldCancelAction() && this.info.type != DamageInfo.DamageType.THORNS) {
            this.isDone = true;
        } else {
            if (this.duration == DURATION) {
                if (this.info.type != DamageInfo.DamageType.THORNS && (this.info.owner.isDying || this.info.owner.halfDead)) {
                    this.isDone = true;
                    return;
                }

                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
            }

            this.tickDuration();
            if (this.isDone) {
                if (this.attackEffect == AttackEffect.POISON) {
                    this.target.tint.color.set(Color.CHARTREUSE.cpy());
                    this.target.tint.changeColor(Color.WHITE.cpy());
                } else if (this.attackEffect == AttackEffect.FIRE) {
                    this.target.tint.color.set(Color.RED);
                    this.target.tint.changeColor(Color.WHITE.cpy());
                }

                CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.1F);
                this.target.damage(this.info);
                if (info.owner instanceof AbstractPlayer && ((AbstractPlayer) info.owner).hasRelic(VoltShroom.ID)) {
                    ((VoltShroom) ((AbstractPlayer) info.owner).getRelic(VoltShroom.ID)).electricHook();
                }
                if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
                    DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
                    dataCollector.onElecIndirect(this.info.output);
                }
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
        }
    }
}
