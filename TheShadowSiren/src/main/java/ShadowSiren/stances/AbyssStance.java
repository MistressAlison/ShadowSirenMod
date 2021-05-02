package ShadowSiren.stances;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.BurnPower;
import ShadowSiren.powers.DrenchPower;
import ShadowSiren.vfx.ShaderContainer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ShaderHelper;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import java.util.HashSet;

public class AbyssStance extends AbstractStance {
    public static final String STANCE_ID = ShadowSirenMod.makeID("AbyssStance");
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    private static long sfxId = -1L;
    private static final HashSet<Integer> usedCosts = new HashSet<>();
    private static final int STACKS = 1;

    public AbyssStance() {
        this.ID = STANCE_ID;
        this.name = stanceString.NAME;
        this.updateDescription();
    }

    @Override
    public void onEnterStance() {
        if (sfxId != -1L) {
            this.stopIdleSfx();
        }
        usedCosts.clear();
        CardCrawlGame.sound.play("STANCE_ENTER_CALM");
        //ShaderContainer.shade.begin();
        //CardCrawlGame.psb.setShader(ShaderContainer.shade);
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_CALM");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.BLUE.cpy(), true));

        if (AbstractDungeon.player instanceof Vivian) {
            ((Vivian) AbstractDungeon.player).playAnimation("void");
        }
    }

    @Override
    public void onExitStance() {
        usedCosts.clear();
        //AbstractDungeon.actionManager.addToBottom(new DrawCardAction(2));
        this.stopIdleSfx();
        if (AbstractDungeon.player instanceof Vivian) {
            ((Vivian) AbstractDungeon.player).playAnimation("idle");
        }
    }

    @Override
    public void atStartOfTurn() {
        usedCosts.clear();
    }

    @Override
    public void onEndOfTurn() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            //c.retain = true;
        }
        AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, AbstractDungeon.player.hand.size()));
    }

    @Override
    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_CALM", sfxId);
            sfxId = -1L;
        }
    }

    @Override
    public void onPlayCard(AbstractCard card) {
        if (!usedCosts.contains(card.costForTurn)) {
            usedCosts.add(card.costForTurn);
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerToRandomEnemyAction(AbstractDungeon.player, new DrenchPower(null, 1), 1));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(1));
        }
    }

    @Override
    public void updateDescription() {
        this.description = stanceString.DESCRIPTION[0];
    }
}
