package ShadowSiren.util;

import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
import ShadowSiren.powers.ElementalPower;
import ShadowSiren.vfx.ElementParticleEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class ParticleOrbitRenderer {
    public enum TargetLocation {
        PLAYER,
        MOUSE,
        OTHER
    }
    public static final float NORMAL_BOOST = 1f;
    private static final float renderScale = 2f;
    private static final float BASE_INTERVAL = 0.1f;
    private static final float CLOSE_ENOUGH = 5f * Settings.scale;
    private static final float BASE_ANGLE_SPEED = 100f;
    private static final Vector2 movementVector = new Vector2();
    private static float particleTimer;
    private static float angleSpeedMulti = 1f;
    private static float angle;
    private static float x,y,tx,ty,dx,dy;
    private static float da, a;
    private static float orbitHeight = 110;
    private static float orbitNormalOffset = 0;
    private static float moveSpeed = 50f;
    private static TargetLocation target = TargetLocation.PLAYER;

    public static void increaseSpeed(float increment) {
        angleSpeedMulti += increment;
    }

    public static void returnOrbitToPlayer() {
        target = TargetLocation.PLAYER;
    }

    public static void returnOrbitToMouse() {
        target = TargetLocation.MOUSE;
    }

    public static void moveOrbit(Hitbox h) {
        moveOrbit(h.cX, h.cY);
    }

    public static void moveOrbit(float x, float y) {
        tx = x;
        ty = y;
        target = TargetLocation.OTHER;
    }

    public static TargetLocation getCurrentTarget() {
        return target;
    }

    public static boolean reachedDestination() {
        return Math.abs(tx-x) < CLOSE_ENOUGH && Math.abs(ty-y) < CLOSE_ENOUGH;
    }

    public static void setOrbitHeight(float height) {
        orbitHeight = height;
    }

    public static void setOrbitNormalOffset(float offset) {
        orbitNormalOffset = offset;
    }

    private static float limitMovement(float desiredSpeed, float maxSpeed) {
        return (Math.abs(desiredSpeed) > Math.abs(maxSpeed)) ? maxSpeed : desiredSpeed;
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "render")
    public static class RenderFloatyElementIcons {
        @SpirePrefixPatch
        public static void patch(AbstractPlayer __instance, SpriteBatch sb) {
            if (__instance.hb.hovered && HitboxRightClick.rightClicked.get(__instance.hb)) {
                if (target == TargetLocation.PLAYER) {
                    target = TargetLocation.MOUSE;
                } else if (target == TargetLocation.MOUSE) {
                    target = TargetLocation.PLAYER;
                }
            }
            if (target == TargetLocation.PLAYER) {
                tx = __instance.hb.cX;
                ty = __instance.hb.cY;
            } else if (target == TargetLocation.MOUSE) {
                tx = InputHelper.mX;
                ty = InputHelper.mY;
            }
            movementVector.set(tx-x, ty-y);
            movementVector.nor();
            dx = limitMovement(moveSpeed*movementVector.x, tx-x);
            dy = limitMovement(moveSpeed*movementVector.y, ty-y);
            x += dx;
            y += dy;
            particleTimer -= Gdx.graphics.getRawDeltaTime();
            if (angleSpeedMulti > 1) {
                angleSpeedMulti *= (1-(Gdx.graphics.getDeltaTime()/2));
            }
            angle -= BASE_ANGLE_SPEED*angleSpeedMulti*Gdx.graphics.getDeltaTime();
            angle %= 360;
            if (__instance.hasPower(ElementalPower.POWER_ID)) {
                da = 360f/ ElementalPower.getActiveElements().stream().filter(m -> m instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) m).isAnElement).count();
                a = 0;
                for (AbstractDamageModifier mod : ElementalPower.getActiveElements()) {
                    if (mod instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) mod).isAnElement) {
                        if (particleTimer < 0.0F) {
                            AbstractDungeon.effectList.add(new ElementParticleEffect((AbstractVivianDamageModifier) mod, x + orbitNormalOffset, y + orbitHeight, -orbitNormalOffset, -orbitHeight, renderScale, angle+a));
                        }
                        a += da;
                    }
                }
                if (particleTimer < 0.0F) {
                    particleTimer = BASE_INTERVAL / angleSpeedMulti;
                    if (!reachedDestination()) {
                        particleTimer /= 2;
                    }
                }
            }
        }
    }
}
