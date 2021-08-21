package ShadowSiren.util;

import ShadowSiren.characters.Vivian;
import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;

public class StarBarPanel {
    private static final int BASE_W = 128;
    private static final float THIS_IMG_SCALE = 1.00F * Settings.scale;
    private static final float MAIN_ORB_IMG_SCALE = 1.15F * Settings.scale;
    private static final float X_OFFSET = 0f * Settings.scale;
    private static final float Y_OFFSET = 128 * Settings.scale;
    private static final float OFFSET_PER_ORB = 50f * Settings.scale;
    private static final float SECONDS_PER_SHIFT = 10;
    private static final float HUE_SHIFT_PER_LAYER = 0.05f;
    private static final Color ENERGY_TEXT_COLOR = new Color(1.0F, 1.0F, 0.86F, 1.0F);
    protected static Texture baseLayer;
    protected static Texture[] orbTextures;
    protected static String[] orbTextureStrings;
    protected static float[] layerSpeeds;
    protected static float[] layerAngles;
    protected static Color[] layerColors;
    private static Texture orbVfx;
    private static float hueSlider;
    private static float vfxScale;
    private static float vfxAngle;
    public static float vfxTimer;
    private static Color vfxColor;
    public static float fontScale;

    
    static {
        layerSpeeds = new float[]{-20.0F, 20.0F, -40.0F, 40.0F, 360.0F};
        orbTextureStrings = new String[]{
                "ShadowSirenResources/images/starBar/layer1.png",
                "ShadowSirenResources/images/starBar/layer2.png",
                "ShadowSirenResources/images/starBar/layer3.png",
                "ShadowSirenResources/images/starBar/layer4.png",
                "ShadowSirenResources/images/starBar/layer5.png",
                "ShadowSirenResources/images/starBar/layer6.png",};
        orbTextures = new Texture[5];
        layerColors = new Color[5];
        layerAngles = new float[5];
        baseLayer = ImageMaster.loadImage(orbTextureStrings[5]);
        for(int i = 0; i < 5; ++i) {
            orbTextures[i] = ImageMaster.loadImage(orbTextureStrings[i]);
            layerColors[i] = new Color(-1);
        }
        orbVfx = ImageMaster.loadImage("ShadowSirenResources/images/starBar/vfx.png");
        hueSlider = 0;
        vfxAngle = 1;
        vfxAngle = 0;
        vfxTimer = 0;
        vfxColor = new Color(-1);
        fontScale = 2.0F;
    }

    public static void update() {
        if (fontScale != 1.0F) {
            fontScale = MathHelper.scaleLerpSnap(fontScale, 1.0F);
        }
        hueSlider += Gdx.graphics.getDeltaTime()/SECONDS_PER_SHIFT;
        if (hueSlider > 1) {
            hueSlider = 0;
        }
        updateOrb();
        updateVfx();
    }

    public static void updateOrb() {
        int l = layerAngles.length;
        float h;
        float multi = StarBarManager.makeCurrentAmount();
        if (multi == 0) {
            multi = 0.1f;
        }
        for (int i = 0; i < layerAngles.length ; i++) {
            layerAngles[i] += Gdx.graphics.getDeltaTime() * layerSpeeds[l-1-i] * multi;
            h = (hueSlider+(HUE_SHIFT_PER_LAYER*i))%1;
            updateColorFromHSB(layerColors[i], h, 1, 1);
        }
    }

    private static void updateVfx() {
        if (vfxTimer != 0.0F) {
            vfxColor.a = Interpolation.exp10In.apply(0.5F, 0.0F, 1.0F - vfxTimer / 2.0F);
            vfxAngle += Gdx.graphics.getDeltaTime() * -30.0F;
            vfxScale = Settings.scale * Interpolation.exp10In.apply(1.0F, 0.1F, 1.0F - vfxTimer / 2.0F);
            vfxTimer -= Gdx.graphics.getDeltaTime();
            if (vfxTimer < 0.0F) {
                vfxTimer = 0.0F;
                vfxColor.a = 0.0F;
            }
        }

    }

    public static void render(SpriteBatch sb, float current_x, float current_y) {
        renderOrb(sb, current_x, current_y);
        if (vfxTimer != 0.0F) {
            renderVFX(sb, current_x, current_y);
        }
        String energyMsg = (int)(StarBarManager.makeCurrentAmount()*100f)+"%";
        AbstractDungeon.player.getEnergyNumFont().getData().setScale(fontScale*THIS_IMG_SCALE);
        FontHelper.renderFontCentered(sb, AbstractDungeon.player.getEnergyNumFont(), energyMsg, current_x + X_OFFSET, current_y + Y_OFFSET, ENERGY_TEXT_COLOR);
    }
    public static void renderOrb(SpriteBatch sb, float current_x, float current_y) {
        for(int i = 0; i < orbTextures.length; ++i) {
            sb.setColor(layerColors[i]);
            sb.draw(orbTextures[i], current_x + X_OFFSET - BASE_W /2F, current_y + Y_OFFSET - BASE_W /2F, BASE_W /2F, BASE_W /2F, BASE_W, BASE_W, THIS_IMG_SCALE, THIS_IMG_SCALE, layerAngles[i], 0, 0, BASE_W, BASE_W, false, false);
        }
        sb.setColor(Color.WHITE);
        sb.draw(baseLayer, current_x + X_OFFSET - BASE_W /2F, current_y + Y_OFFSET - BASE_W /2F, BASE_W /2F, BASE_W /2F, BASE_W, BASE_W, THIS_IMG_SCALE, THIS_IMG_SCALE, 0.0F, 0, 0, BASE_W, BASE_W, false, false);
    }

    public static void renderVFX(SpriteBatch sb, float current_x, float current_y) {
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.setColor(vfxColor);
        sb.draw(orbVfx, current_x + X_OFFSET - BASE_W, current_y + Y_OFFSET - BASE_W, BASE_W, BASE_W, BASE_W *2F, BASE_W *2F, vfxScale* THIS_IMG_SCALE / MAIN_ORB_IMG_SCALE, vfxScale* THIS_IMG_SCALE / MAIN_ORB_IMG_SCALE, vfxAngle - 50.0F, 0, 0, BASE_W *2, BASE_W *2, true, false);
        sb.draw(orbVfx, current_x + X_OFFSET - BASE_W, current_y + Y_OFFSET - BASE_W, BASE_W, BASE_W, BASE_W *2F, BASE_W *2F, vfxScale* THIS_IMG_SCALE / MAIN_ORB_IMG_SCALE, vfxScale* THIS_IMG_SCALE / MAIN_ORB_IMG_SCALE, -vfxAngle, 0, 0, BASE_W *2, BASE_W *2, false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void updateColorFromHSB(Color color, float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float)Math.floor(hue)) * 6.0f;
            float f = h - (float)java.lang.Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 1:
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 2:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                    break;
                case 3:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 4:
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 5:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                    break;
            }
        }
        color.set(r/255f, g/255f, b/255f, 1);
    }

    @SpirePatch(clz = CustomEnergyOrb.class, method = "renderOrb")
    public static class RenderStarBarAfterOrb {
        @SpirePostfixPatch
        public static void renderPls(CustomEnergyOrb __instance, SpriteBatch sb, boolean enabled, float current_x, float current_y) {
            if (AbstractDungeon.player instanceof Vivian) {
                //render(sb, current_x, current_y);
            }
        }
    }

    @SpirePatch(clz = CustomEnergyOrb.class, method = "updateOrb")
    public static class UpdateStarBarAfterOrb {
        @SpirePostfixPatch
        public static void updatePls(CustomEnergyOrb __instance, int energyCount) {
            //update();
        }
    }
}
