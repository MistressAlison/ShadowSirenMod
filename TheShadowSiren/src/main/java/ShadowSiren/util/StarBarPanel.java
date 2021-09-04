package ShadowSiren.util;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractStarCard;
import ShadowSiren.cards.starCards.*;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.StarFormPower;
import basemod.ClickableUIElement;
import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;
import java.util.HashMap;

public class StarBarPanel {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ShadowSirenMod.makeID("StarBar"));
    private static final String TITLE = uiStrings.TEXT[0];
    private static final String[] TEXT = uiStrings.EXTRA_TEXT;
    private static final int BASE_W = 128;
    private static final float THIS_IMG_SCALE = 0.7F * Settings.scale;
    private static final float MAIN_ORB_IMG_SCALE = 1.15F * Settings.scale;
    private static final float X_OFFSET = 0f * THIS_IMG_SCALE;
    private static final float Y_OFFSET = BASE_W * THIS_IMG_SCALE * (MAIN_ORB_IMG_SCALE/Settings.scale);
    private static final float X_OFFSET_PER_ORB = BASE_W * THIS_IMG_SCALE / (MAIN_ORB_IMG_SCALE/Settings.scale);
    private static final float Y_OFFSET_PER_ORB = 0;
    private static final float SECONDS_PER_SHIFT = 10;
    private static final float HUE_SHIFT_PER_LAYER = 0.05f;
    private static final float HUE_SHIFT_PER_ORB = -0.15f;
    private static final Color ENERGY_TEXT_COLOR = new Color(0.86F, 1.0F, 1.0F, 1.0F);
    private static final float BAR_ANIM_TIME = 0.5f;
    private static final boolean FORCE_COMPLETE_ANIMATION = false;
    private static final float TIP_OFFSET_X = 0.0F * Settings.scale;
    private static final float TIP_OFFSET_Y = -50.0F * Settings.scale;
    private static final float CARD_OFFSET_X = 0f * Settings.scale;
    private static final float CARD_OFFSET_Y = 120f * Settings.scale;
    private static final float CARD_SCALE = 0.35f;
    private static Texture orbVfx;
    private static float hueSlider;
    private static float vfxScale;
    private static float vfxAngle;
    public static float vfxTimer;
    private static float barAnimationTimer;
    private static boolean open;
    private static float offsetMulti;
    private static Color vfxColor;
    public static float fontScale;
    public static final Clickable clickable = new Clickable();
    private static final ArrayList<StarOrb> orbs = new ArrayList<>();
    private static final CardGroup starCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private static final HashMap<AbstractCard, AbstractCard> previewMap = new HashMap<>();
    private static final ArrayList<AbstractCard> previews = new ArrayList<>();


    
    static {
        orbVfx = ImageMaster.loadImage("ShadowSirenResources/images/starBar/vfx.png");
        hueSlider = 0;
        vfxAngle = 1;
        vfxAngle = 0;
        vfxTimer = 0;
        vfxColor = new Color(-1);
        fontScale = 2.0F;
        barAnimationTimer = 0;
    }

    public static void update() {
        updateFont();
        updateHueSlider();
        updateOrbs();
        updateVfx();
        clickable.update();
    }

    public static void updateFont() {
        if (fontScale != 1.0F) {
            fontScale = MathHelper.scaleLerpSnap(fontScale, 1.0F);
        }
    }

    public static void updateHueSlider() {
        hueSlider += Gdx.graphics.getDeltaTime()/SECONDS_PER_SHIFT;
        if (hueSlider > 1) {
            hueSlider = 0;
        }
    }

    public static void updateOrbs() {
        while (orbs.size() < StarBarManager.maxStarPower) {
            orbs.add(new StarOrb());
        }
        while (orbs.size() > StarBarManager.maxStarPower && orbs.size() != 0) {
            orbs.remove(orbs.size()-1);
        }
        for (StarOrb o : orbs) {
            o.update();
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
        if (barAnimationTimer != 0.0F) {
            if (open) {
                offsetMulti = Interpolation.swing.apply(0.0F, 1.0F, 1.0F - barAnimationTimer / BAR_ANIM_TIME);
            } else {
                offsetMulti = Interpolation.swing.apply(1.0F, 0.0F, 1.0F - barAnimationTimer / BAR_ANIM_TIME);
            }
            barAnimationTimer -= Gdx.graphics.getDeltaTime();
            if (barAnimationTimer < 0.0F) {
                barAnimationTimer = 0.0F;
                if (open) {
                    offsetMulti = 1f;
                } else {
                    offsetMulti = 0f;
                }
            }
        }
    }

    public static void render(SpriteBatch sb, float current_x, float current_y) {
        current_x += X_OFFSET;
        current_y += Y_OFFSET;
        AbstractDungeon.player.getEnergyNumFont().getData().setScale(fontScale*THIS_IMG_SCALE);
        renderOrbs(sb, current_x, current_y);
        if (vfxTimer != 0.0F) {
            renderVFX(sb, current_x, current_y);
        }
        clickable.move(current_x, current_y);
        clickable.render(sb);
    }
    public static void renderOrbs(SpriteBatch sb, float x, float y) {
        if (open || barAnimationTimer != 0) {
            for (int i = orbs.size()-1 ; i >= 0 ; i--) {
                orbs.get(i).render(sb, x+i*X_OFFSET_PER_ORB*offsetMulti, y+i*Y_OFFSET_PER_ORB*offsetMulti);
            }
            if (open) {
                for (int i = previews.size()-1 ; i >= 0 ; i--) {
                    previews.get(i).current_x = x + CARD_OFFSET_X + i*X_OFFSET_PER_ORB*offsetMulti;
                    previews.get(i).current_y = y + CARD_OFFSET_Y + i*Y_OFFSET_PER_ORB*offsetMulti;
                    previews.get(i).drawScale = CARD_SCALE;
                    previews.get(i).render(sb);
                }
            }
        } else if (!orbs.isEmpty()) {
            orbs.get(0).render(sb, x, y);
        }
    }

    public static void renderVFX(SpriteBatch sb, float x, float y) {
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        updateColorFromHSB(vfxColor, hueSlider, 1, 1);
        sb.setColor(vfxColor);
        sb.draw(orbVfx, x - BASE_W, y - BASE_W, BASE_W, BASE_W, BASE_W *2F, BASE_W *2F, vfxScale* THIS_IMG_SCALE / MAIN_ORB_IMG_SCALE, vfxScale* THIS_IMG_SCALE / MAIN_ORB_IMG_SCALE, vfxAngle - 50.0F, 0, 0, BASE_W *2, BASE_W *2, true, false);
        sb.draw(orbVfx, x - BASE_W, y - BASE_W, BASE_W, BASE_W, BASE_W *2F, BASE_W *2F, vfxScale* THIS_IMG_SCALE / MAIN_ORB_IMG_SCALE, vfxScale* THIS_IMG_SCALE / MAIN_ORB_IMG_SCALE, -vfxAngle, 0, 0, BASE_W *2, BASE_W *2, false, false);
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

    public static class StarOrb {
        protected Texture baseLayer;
        protected Texture[] orbTextures;
        protected String[] orbTextureStrings;
        protected float[] layerSpeeds;
        protected float[] layerAngles;
        protected Color[] layerColors;

        public StarOrb() {
            layerSpeeds = new float[]{-20.0F, 20.0F, -40.0F, 40.0F, 360.0F};
            orbTextureStrings = new String[] {
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
        }

        public void update() {
            int l = layerAngles.length;
            float h, s, b;
            s = index() > StarBarManager.starPower ? 0.5f : 1f;
            b = index() > StarBarManager.starPower ? 0.5f : 1f;
            float multi = StarBarManager.makeCurrentAmount();
            if (multi == 0) {
                multi = 0.1f;
            }
            for (int i = 0; i < layerAngles.length ; i++) {
                layerAngles[i] += Gdx.graphics.getDeltaTime() * layerSpeeds[l-1-i] * multi;
                h = (hueSlider+(HUE_SHIFT_PER_ORB*index())+(HUE_SHIFT_PER_LAYER*i))%1;
                updateColorFromHSB(layerColors[i], h, s, b);
            }
        }

        public void render(SpriteBatch sb, float x, float y) {
            for(int i = 0; i < orbTextures.length; ++i) {
                sb.setColor(layerColors[i]);
                sb.draw(orbTextures[i], x - BASE_W /2F, y - BASE_W /2F, BASE_W /2F, BASE_W /2F, BASE_W, BASE_W, THIS_IMG_SCALE, THIS_IMG_SCALE, layerAngles[i], 0, 0, BASE_W, BASE_W, false, false);
            }
            sb.setColor(Color.WHITE);
            sb.draw(baseLayer, x - BASE_W /2F, y - BASE_W /2F, BASE_W /2F, BASE_W /2F, BASE_W, BASE_W, THIS_IMG_SCALE, THIS_IMG_SCALE, 0.0F, 0, 0, BASE_W, BASE_W, false, false);

            String energyMsg = getMsg();
            FontHelper.renderFontCentered(sb, AbstractDungeon.player.getEnergyNumFont(), energyMsg, x, y, ENERGY_TEXT_COLOR);
        }

        public int index() {
            return orbs.indexOf(this);
        }

        public String getMsg() {
            int i = index();
            if (i == 0 && !open && StarBarManager.starPower >= 1) {
                return StarBarManager.starPower+"/"+StarBarManager.maxStarPower;
            }
            if (i > StarBarManager.starPower) {
                return "";
            }
            else if (i < StarBarManager.starPower) {
                return String.valueOf(i+1);
            }
            return (StarBarManager.progress)+"%";
        }
    }

    public static class Clickable extends ClickableUIElement {
        private static final float hitboxSize = BASE_W*THIS_IMG_SCALE;
        private static final float moveDelta = hitboxSize/2;
        private boolean wasClicked = false;
        private boolean needsToOpen = true;
        private String textBackup;

        public Clickable() {
            super(new TextureAtlas(Gdx.files.internal("powers/powers.atlas")).findRegion("48/" + ""), 0, 0, hitboxSize, hitboxSize);
        }

        public void move(float x, float y) {
            this.setX(x-(moveDelta * Settings.scale));
            this.setY(y-(moveDelta * Settings.scale));
        }

        @Override
        protected void onHover() {
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
                TipHelper.renderGenericTip(x + TIP_OFFSET_X, y + TIP_OFFSET_Y, TITLE, TEXT[0]+StarBarManager.starPower+TEXT[1]+StarBarManager.maxStarPower+TEXT[2]+TEXT[3]);
                //TipHelper.renderGenericTip(x + TIP_OFFSET_X, y + TIP_OFFSET_Y, TITLE, TEXT[3]);
                if (!open && (!FORCE_COMPLETE_ANIMATION || barAnimationTimer == 0)) {
                    barAnimationTimer = BAR_ANIM_TIME - barAnimationTimer;
                    open = true;
                }
            }
        }

        @Override
        protected void onUnhover() {
            if (!AbstractDungeon.isScreenUp && open && (!FORCE_COMPLETE_ANIMATION || barAnimationTimer == 0)) {
                barAnimationTimer = BAR_ANIM_TIME - barAnimationTimer;
                open = false;
            }
        }

        @Override
        protected void onClick() {
            if (!AbstractDungeon.actionManager.turnHasEnded && !AbstractDungeon.isScreenUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                wasClicked = true;
            }
        }

        @Override
        public void update() {
            super.update();
            if (wasClicked) {
                if (needsToOpen) {
                    for (AbstractCard c : starCards.group) {
                        c.applyPowers();
                        c.initializeDescription();
                    }
                    needsToOpen = false;
                    AbstractDungeon.gridSelectScreen.open(starCards, 1, TEXT[4], false, false, true, true);
                } else {
                    if (!AbstractDungeon.isScreenUp) {
                        for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                            c.stopGlowing();
                            if (c instanceof AbstractStarCard) {
                                if (AbstractDungeon.player.hand.size() < 10 && StarBarManager.starPower >= ((AbstractStarCard) c).getSpawnCost()) {
                                    StarBarManager.consumeStarPower(((AbstractStarCard) c).getSpawnCost());
                                    if (!AbstractDungeon.player.hasPower(StarFormPower.POWER_ID)) {
                                        starCards.removeCard(c);
                                        previews.remove(previewMap.get(c));
                                    }
                                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(c.makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                                }
                            }
                        }
                        AbstractDungeon.gridSelectScreen.selectedCards.clear();
                        wasClicked = false;
                        needsToOpen = true;
                    }
                }
            }
        }
    }

    public static void populateStarCards(boolean giveAllCards) {
        starCards.clear();
        previews.clear();
        previewMap.clear();
        starCards.addToTop(new SweetTreat(true));
        starCards.addToTop(new EarthTremor(true));
        if (AbstractDungeon.actNum > 1 || giveAllCards) {
            starCards.addToTop(new ClockOut(true));
            starCards.addToTop(new PowerLift(true));
        }
        if (AbstractDungeon.actNum > 2 || giveAllCards) {
            //Art Attack
            //Sweet Feast
            starCards.addToTop(new ShowStopper(true));
            starCards.addToTop(new SuperNova(true));
        }
        for (AbstractCard c : starCards.group) {
            AbstractCard copy = c.makeStatEquivalentCopy();
            previewMap.put(c, copy);
            previews.add(copy);
        }
    }

    @SpirePatch(clz = CustomEnergyOrb.class, method = "renderOrb")
    public static class RenderStarBarAfterOrb {
        @SpirePostfixPatch
        public static void renderPls(CustomEnergyOrb __instance, SpriteBatch sb, boolean enabled, float current_x, float current_y) {
            if (AbstractDungeon.player instanceof Vivian) {
                render(sb, current_x, current_y);
            }
        }
    }

    @SpirePatch(clz = CustomEnergyOrb.class, method = "updateOrb")
    public static class UpdateStarBarAfterOrb {
        @SpirePostfixPatch
        public static void updatePls(CustomEnergyOrb __instance, int energyCount) {
            if (AbstractDungeon.player instanceof Vivian) {
                update();
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class PopulateStarCards {
        @SpirePostfixPatch
        public static void gimmeCards(AbstractPlayer __instance) {
            populateStarCards(false);
        }
    }
}
