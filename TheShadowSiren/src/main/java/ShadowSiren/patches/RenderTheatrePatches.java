package ShadowSiren.patches;
/*
import ShadowSiren.characters.Vivian;
import ShadowSiren.util.TextureLoader;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import javassist.CtBehavior;

public class RenderTheatrePatches {
    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class RenderCurtains {
        public static AtlasRegion bg = new AtlasRegion(TextureLoader.getTexture("ShadowSirenResources/images/scene/curtainsScaled.png"), 0, 0, 1920, 1080);
        public static AtlasRegion curtainTop = new AtlasRegion(TextureLoader.getTexture("ShadowSirenResources/images/scene/curtainT.png"), 0, 0, 1920, 90);
        public static AtlasRegion curtainLeft = new AtlasRegion(TextureLoader.getTexture("ShadowSirenResources/images/scene/curtainL.png"), 0, 0, 297, 917);
        public static AtlasRegion curtainRight = new AtlasRegion(TextureLoader.getTexture("ShadowSirenResources/images/scene/curtainR.png"), 0, 0, 297, 917);
        public static AtlasRegion audienceZone = new AtlasRegion(TextureLoader.getTexture("ShadowSirenResources/images/scene/audience.png"), 0, 0, 1920, 203);
        public static float tx,ty,lx,ly,rx,ry;
        private static final float TOP_DESIRED_X = Settings.WIDTH/2f;
        private static final float TOP_DESIRED_Y = Settings.HEIGHT - (curtainTop.packedHeight * Settings.scale);
        public static final float TOP_START_X = TOP_DESIRED_X;
        public static final float TOP_START_Y = Settings.HEIGHT + (curtainTop.packedHeight * Settings.scale);
        private static final float LEFT_DESIRED_X = 0;
        private static final float LEFT_DESIRED_Y = Settings.HEIGHT/2f + (25f * Settings.scale);
        public static final float LEFT_START_X = 0 - (curtainLeft.packedWidth * Settings.scale);
        public static final float LEFT_START_Y = Settings.HEIGHT/2f + (50f * Settings.scale);
        private static final float RIGHT_DESIRED_X = Settings.WIDTH;
        private static final float RIGHT_DESIRED_Y = Settings.HEIGHT/2f + (25f * Settings.scale);
        public static final float RIGHT_START_X = Settings.WIDTH + (curtainRight.packedWidth * Settings.scale);
        public static final float RIGHT_START_Y = Settings.HEIGHT/2f + (50f * Settings.scale);

        @SpireInsertPatch(locator = Locator.class)
        public static void curtainsPls(AbstractDungeon __instance, SpriteBatch sb) {
            if (AbstractDungeon.rs == AbstractDungeon.RenderScene.NORMAL && AbstractDungeon.player instanceof Vivian) {
                lx = MathHelper.scaleLerpSnap(lx, LEFT_DESIRED_X);
                ly = MathHelper.scaleLerpSnap(ly, LEFT_DESIRED_Y);
                rx = MathHelper.scaleLerpSnap(rx, RIGHT_DESIRED_X);
                ry = MathHelper.scaleLerpSnap(ry, RIGHT_DESIRED_Y);
                tx = MathHelper.scaleLerpSnap(tx, TOP_DESIRED_X);
                ty = MathHelper.scaleLerpSnap(ty, TOP_DESIRED_Y);
                renderPls(sb, audienceZone, Settings.WIDTH/2f, 100f, 1, 0);
                renderPls(sb, curtainLeft, lx, ly, 1, 0);
                renderPls(sb, curtainRight, rx, ry, 1, 0);
                renderPls(sb, curtainTop, tx, ty, 1, 0);
            }
        }

        private static void renderPls(SpriteBatch sb, AtlasRegion img, float x, float y, float scale, float rot) {
            sb.draw(img, x - img.packedWidth/2f, y - img.packedHeight/2f, img.packedWidth/2f, img.packedHeight/2f, img.packedWidth, img.packedHeight, scale * Settings.scale, scale * Settings.scale, rot);
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractScene.class, "renderCombatRoomFg");
                int[] ret = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                ret[0]++;
                return ret;
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class MoveCurtainsPreBattle {
        @SpirePostfixPatch
        public static void curtainsMovePls(AbstractPlayer __instance) {
            if (__instance instanceof Vivian) {
                RenderCurtains.tx = RenderCurtains.TOP_START_X;
                RenderCurtains.ty = RenderCurtains.TOP_START_Y;
                RenderCurtains.lx = RenderCurtains.LEFT_START_X;
                RenderCurtains.ly = RenderCurtains.LEFT_START_Y;
                RenderCurtains.rx = RenderCurtains.RIGHT_START_X;
                RenderCurtains.ry = RenderCurtains.RIGHT_START_Y;
            }
        }
    }
}
*/