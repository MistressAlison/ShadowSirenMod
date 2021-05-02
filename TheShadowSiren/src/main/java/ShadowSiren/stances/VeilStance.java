package ShadowSiren.stances;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.characters.Vivian;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

public class VeilStance extends AbstractStance {
    public static final String STANCE_ID = ShadowSirenMod.makeID("VeilStance");
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    private static long sfxId = -1L;

    public VeilStance() {
        this.ID = STANCE_ID;
        this.name = stanceString.NAME;
        this.updateDescription();
    }

    public void onEnterStance() {
        if (sfxId != -1L) {
            this.stopIdleSfx();
        }

        CardCrawlGame.sound.play("STANCE_ENTER_CALM");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_CALM");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.BLACK.cpy(), true));

        if (AbstractDungeon.player instanceof Vivian) {
            ((Vivian) AbstractDungeon.player).playAnimation("hide");
        }
    }

    public void onExitStance() {
        this.stopIdleSfx();
        if (AbstractDungeon.player instanceof Vivian) {
            ((Vivian) AbstractDungeon.player).playAnimation("idle");
        }
    }

    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_CALM", sfxId);
            sfxId = -1L;
        }
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * 0.5F : damage;
    }

    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * 0.5F : damage;
    }

    @Override
    public void updateDescription() {
        this.description = stanceString.DESCRIPTION[0];
    }
}
