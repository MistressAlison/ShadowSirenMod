package ShadowSiren.stances;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.ChargePower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

public class HyperStance extends AbstractStance {
    public static final String STANCE_ID = ShadowSirenMod.makeID("HyperStance");
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    private static long sfxId = -1L;
    private static final int CHARGE_AMOUNT = 2;

    public HyperStance() {
        this.ID = STANCE_ID;
        this.name = stanceString.NAME;
        this.updateDescription();
    }

    public void onEnterStance() {
        if (sfxId != -1L) {
            this.stopIdleSfx();
        }

        CardCrawlGame.sound.play("STANCE_ENTER_DIVINITY");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_DIVINITY");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.YELLOW.cpy(), true));

        if (AbstractDungeon.player instanceof Vivian) {
            //((Vivian) AbstractDungeon.player).playAnimation("hide");
            //((Vivian) AbstractDungeon.player).getAnimation().myPlayer.setScale(((Vivian) AbstractDungeon.player).getAnimation().myPlayer.getScale()*FACTOR);
            AbstractDungeon.player.tint.color = Color.YELLOW.cpy();

        }
    }

    public void onExitStance() {
        this.stopIdleSfx();
        //((Vivian) AbstractDungeon.player).getAnimation().myPlayer.setScale(((Vivian) AbstractDungeon.player).getAnimation().myPlayer.getScale()/FACTOR);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargePower(AbstractDungeon.player, CHARGE_AMOUNT)));
        AbstractDungeon.player.tint.changeColor(Color.WHITE.cpy());
        AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.1F));
        if (AbstractDungeon.player instanceof Vivian) {
            ((Vivian) AbstractDungeon.player).playAnimation("idle");
        }
    }

    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_DIVINITY", sfxId);
            sfxId = -1L;
        }
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * 1.2F : damage;
    }

    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * 0.8F : damage;
    }

    @Override
    public void updateDescription() {
        this.description = stanceString.DESCRIPTION[0];
    }
}
