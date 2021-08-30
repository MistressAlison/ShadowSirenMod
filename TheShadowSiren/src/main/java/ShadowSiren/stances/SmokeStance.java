package ShadowSiren.stances;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.characters.Vivian;
import ShadowSiren.oldStuff.powers.BurnPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

public class SmokeStance extends AbstractStance implements OnLoseHPStance {
    public static final String STANCE_ID = ShadowSirenMod.makeID("SmokeStance");
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    private static long sfxId = -1L;

    public SmokeStance() {
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
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GRAY.cpy(), true));

        if (AbstractDungeon.player instanceof Vivian) {
            ((Vivian) AbstractDungeon.player).playAnimation("void");
        }
    }

    @Override
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

    @Override
    public int onLoseHP(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 /*&& info.type == DamageInfo.DamageType.NORMAL*/) {
            /*if (AbstractDungeon.player.hasPower(SquallPower.POWER_ID)) {
                int delta = (AbstractDungeon.player.hand.size() - damageAmount) * AbstractDungeon.player.getPower(SquallPower.POWER_ID).amount;
                if (delta > 0) {
                    AbstractDungeon.actionManager.addToTop(new DamageAction(info.owner, new DamageInfo(AbstractDungeon.player, delta, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
            }
            damageAmount = Math.max(0, damageAmount-AbstractDungeon.player.hand.size());
            AbstractDungeon.actionManager.addToTop(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, AbstractDungeon.player.hand.size(), true));
            */
            int toRemove = Math.min(damageAmount, AbstractDungeon.player.hand.size());
            damageAmount -= toRemove;
            if (info.owner != AbstractDungeon.player) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(info.owner, AbstractDungeon.player, new BurnPower(info.owner, AbstractDungeon.player, toRemove)));
            }
            AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    for (int i = 0 ; i < toRemove ; i++) {
                        if (AbstractDungeon.player.hand.size() > 0) {
                            AbstractCard card = AbstractDungeon.player.hand.getBottomCard();
                            AbstractDungeon.player.hand.moveToDiscardPile(card);
                            card.triggerOnManualDiscard();
                            GameActionManager.incrementDiscard(false);
                        } else {
                            break;
                        }
                    }
                    this.isDone = true;
                }
            });
        }
        return damageAmount;
    }

    @Override
    public void onEndOfTurn() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            c.retain = true;
        }
    }

    @Override
    public void updateDescription() {
        this.description = stanceString.DESCRIPTION[0];
    }
}
