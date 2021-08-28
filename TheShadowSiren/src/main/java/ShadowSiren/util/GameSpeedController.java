package ShadowSiren.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import javassist.CtBehavior;

import java.util.ArrayList;

public class GameSpeedController {
    private static float deltaDivisor = 1f;
    private static final ArrayList<SlowMotionEffect> slowMotionList = new ArrayList<>();

    public static void addSlowMotionEffect(SlowMotionEffect e) {
        slowMotionList.add(e);
        calculateDeltaDivisor();
    }

    public static float getDeltaDivisor() {
        return deltaDivisor;
    }

    public static float getEffectiveGameSpeed() {
        return 1f/deltaDivisor;
    }

    private static void calculateDeltaDivisor() {
        deltaDivisor = 1f;
        for (SlowMotionEffect e : slowMotionList) {
            deltaDivisor *= e.getSpeedDivisor();
        }
        if (CardCrawlGame.dungeon != null && AbstractDungeon.player != null) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof GameSpeedModifyingObject) {
                    deltaDivisor *= ((GameSpeedModifyingObject) p).getGameSpeedDivisor();
                }
            }
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof GameSpeedModifyingObject) {
                    deltaDivisor *= ((GameSpeedModifyingObject) r).getGameSpeedDivisor();
                }
            }
        }
    }

    @SpirePatch(clz = LwjglGraphics.class, method = "getDeltaTime")
    public static class WhatTheHell {
        @SpirePostfixPatch
        public static float speedAdjustment(float result, LwjglGraphics self) {
            return result / deltaDivisor;
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "update")
    public static class UpdateSlowMotion {
        @SpirePostfixPatch
        public static void update() {
            for (SlowMotionEffect e : slowMotionList) {
                e.update();
            }
            slowMotionList.removeIf(e -> e.isDone);
            calculateDeltaDivisor();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class RenderSlowMotion {
        @SpireInsertPatch(locator = Locator.class)
        public static void render(AbstractDungeon __instance, SpriteBatch sb) {
            for (SlowMotionEffect e : slowMotionList) {
                e.render(sb);
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(OverlayMenu.class, "render");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = SaveFile.class)
    public static class ClearList {
        @SpirePostfixPatch
        public static void die() {
            slowMotionList.clear();
            calculateDeltaDivisor();
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "reset")
    public static class ClearList2 {
        @SpirePostfixPatch
        public static void die() {
            slowMotionList.clear();
            calculateDeltaDivisor();
        }
    }

    @SpirePatch2(clz = Sfx.class, method = "play", paramtypez = {float.class, float.class, float.class})
    public static class PitchShift {
        @SpirePrefixPatch
        public static void pls(Sfx __instance, float volume, @ByRef float[] y, float z) {
            y[0] /= deltaDivisor;
        }
    }

    @SpirePatch2(clz = Sfx.class, method = "play", paramtypez = {float.class})
    public static class deferToPitchAdjusted {
        @SpirePrefixPatch
        public static SpireReturn<?> pls(Sfx __instance, float volume) {
            if (deltaDivisor != 1) {
                return SpireReturn.Return(__instance.play(volume, 1f/deltaDivisor, 0f));
            }
            return SpireReturn.Continue();
        }
    }

    public interface GameSpeedModifyingObject {
        float getGameSpeedDivisor();
    }

    public abstract static class AbstractRealTimeGameAction extends AbstractGameAction {
        @Override
        protected void tickDuration() {
            this.duration -= Gdx.graphics.getDeltaTime() * GameSpeedController.getDeltaDivisor();
            if (this.duration < 0.0F) {
                this.isDone = true;
            }
        }
    }

    public abstract static class AbstractRealTimeGameEffect extends AbstractGameEffect {
        @Override
        public void update() {
            this.duration -= Gdx.graphics.getDeltaTime() * GameSpeedController.getDeltaDivisor();
            if (this.duration < 0.0F) {
                this.isDone = true;
            }
        }
    }

    public static class SlowMotionAction extends AbstractGameAction {

        SlowMotionEffect effect;

        public SlowMotionAction(SlowMotionEffect effect) {
            this.effect = effect;
        }

        @Override
        public void update() {
            addSlowMotionEffect(effect);
            this.isDone = true;
        }
    }

    public static class SlowMotionEffect extends AbstractRealTimeGameEffect {
        protected float speedDivisor;

        public SlowMotionEffect(float speedDivisor, float duration) {
            this.startingDuration = this.duration = duration;
            this.speedDivisor = speedDivisor;
        }

        public float getSpeedDivisor() {
            return speedDivisor;
        }

        public void render(SpriteBatch sb) {}

        public void dispose() {}
    }
}
