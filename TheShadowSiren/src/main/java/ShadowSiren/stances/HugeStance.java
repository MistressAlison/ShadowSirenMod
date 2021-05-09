package ShadowSiren.stances;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.characters.Vivian;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

public class HugeStance extends AbstractStance {
    public static final String STANCE_ID = ShadowSirenMod.makeID("HugeStance");
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    private static long sfxId = -1L;
    public static final float FACTOR = 1.5f;

    //TODO Mountain? Giant? Massive? Mammoth? (I am the) Tower? Titan?
    public HugeStance() {
        this.ID = STANCE_ID;
        this.name = stanceString.NAME;
        this.updateDescription();
    }

    public void onEnterStance() {
        if (sfxId != -1L) {
            this.stopIdleSfx();
        }

        CardCrawlGame.sound.play("STANCE_ENTER_WRATH");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_WRATH");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.RED.cpy(), true));

        if (AbstractDungeon.player instanceof Vivian) {
            //((Vivian) AbstractDungeon.player).playAnimation("hide");
            ((Vivian) AbstractDungeon.player).getAnimation().myPlayer.setScale(((Vivian) AbstractDungeon.player).getAnimation().myPlayer.getScale()*FACTOR);
            AbstractDungeon.player.hb.height *= FACTOR;
            AbstractDungeon.player.hb_h *= FACTOR;
            AbstractDungeon.player.hb.width *= FACTOR;
            AbstractDungeon.player.hb_w *= FACTOR;
            ((Vivian) AbstractDungeon.player).refreshHealthBar();
        }
    }

    public void onExitStance() {
        this.stopIdleSfx();
        if (AbstractDungeon.player instanceof Vivian) {
            ((Vivian) AbstractDungeon.player).getAnimation().myPlayer.setScale(((Vivian) AbstractDungeon.player).getAnimation().myPlayer.getScale()/FACTOR);
            AbstractDungeon.player.hb.height /= FACTOR;
            AbstractDungeon.player.hb_h /= FACTOR;
            AbstractDungeon.player.hb.width /= FACTOR;
            AbstractDungeon.player.hb_w /= FACTOR;
            ((Vivian) AbstractDungeon.player).refreshHealthBar();
        }
        if (AbstractDungeon.player instanceof Vivian) {
            ((Vivian) AbstractDungeon.player).playAnimation("idle");
        }
    }

    @Override
    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_WRATH", sfxId);
            sfxId = -1L;
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * 2F : damage;
    }

    @Override
    public void onPlayCard(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            AbstractDungeon.actionManager.addToBottom(new LoseEnergyAction(1));
        }
    }

    @Override
    public void updateDescription() {
        this.description = stanceString.DESCRIPTION[0];
    }
}
