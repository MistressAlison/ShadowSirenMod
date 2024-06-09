package ShadowSiren.actions;

import ShadowSiren.damageModifiers.*;
import ShadowSiren.util.ElementManager;
import ShadowSiren.util.ParticleOrbitRenderer;
import ShadowSiren.vfx.ElementParticleEffect;
import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;

import java.util.ArrayList;
import java.util.Collections;

public class StarFallAction extends AbstractGameAction {
    private static class HitPair {
        public AbstractVivianDamageModifier mod;
        public AbstractMonster target;
        public HitPair(AbstractVivianDamageModifier mod, AbstractMonster target) {
            this.mod = mod;
            this.target = target;
        }
    }
    boolean firstPass = true;
    float waitTimer = 0f;
    int actionPhase = 0;
    boolean mouseReturn;
    AbstractMonster delayedHitCreature;
    String delayedHitSFX = "";
    AbstractGameEffect delayedHitVFX;
    final ArrayList<HitPair> hitArray = new ArrayList<>();
    AbstractMonster mo;
    int hitsPerElement;
    int[] multiDamage;
    DamageInfo.DamageType damageTypeForTurn;

    public StarFallAction(int[] damage, DamageInfo.DamageType damageTypeForTurn, int hits) {
        this.multiDamage = damage;
        this.damageTypeForTurn = damageTypeForTurn;
        this.hitsPerElement = hits;
    }

    private void setDelayedHit(AbstractMonster mon, String sfx, AbstractGameEffect vfx, float delay) {
        delayedHitCreature = mon;
        delayedHitSFX = sfx;
        delayedHitVFX = vfx;
        waitTimer += delay;
    }

    @Override
    public void update() {
        if (firstPass) {
            firstPass = false;
            mouseReturn = ParticleOrbitRenderer.getCurrentTarget() == ParticleOrbitRenderer.TargetLocation.MOUSE;
            ParticleOrbitRenderer.moveOrbit(Settings.WIDTH / 2f, Settings.HEIGHT);
            ArrayList<AbstractMonster> targets = new ArrayList<>();
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    targets.add(m);
                }
            }
            for (AbstractDamageModifier mod : ElementManager.getActiveElements()) {
                if (mod instanceof AbstractVivianDamageModifier) {
                    for (int i = 0; i < hitsPerElement; i++) {
                        Collections.shuffle(targets);
                        for (AbstractMonster t : targets) {
                            hitArray.add(new HitPair((AbstractVivianDamageModifier) mod, t));
                        }
                    }
                }
            }
        }
        if (waitTimer > 0) {
            waitTimer -= Gdx.graphics.getDeltaTime();
            if (waitTimer <= 0) {
                if (!delayedHitSFX.equals("")) {
                    CardCrawlGame.sound.play(delayedHitSFX, 0.2f);
                    delayedHitSFX = "";
                }
                if (delayedHitVFX != null) {
                    AbstractDungeon.effectList.add(delayedHitVFX);
                    delayedHitVFX = null;
                }
                if (delayedHitCreature != null) {
                    delayedHitCreature.damage(new DamageInfo(AbstractDungeon.player, multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(delayedHitCreature)], damageTypeForTurn));
                    delayedHitCreature = null;
                }
            }
        } else if (actionPhase == 0) {
            if (ParticleOrbitRenderer.reachedDestination()) {
                actionPhase++;
                waitTimer += 0.15f;
            }
        } else if (actionPhase == 1) {
            if (hitArray.size() > 0) {
                mo = hitArray.get(0).target;
                if (mo != null) {
//                                AbstractGameEffect shootStar = new VfxBuilder(ImageMaster.TINY_STAR, Settings.WIDTH/2f, Settings.HEIGHT, 0.3f)
//                                        .scale(0.8f, 1.2f, VfxBuilder.Interpolations.SWING)
//                                        .setColor(Color.GOLD)
//                                        .moveX(Settings.WIDTH/2f, mo.hb.cX, VfxBuilder.Interpolations.EXP5IN)
//                                        .moveY(Settings.HEIGHT, mo.hb.cY, VfxBuilder.Interpolations.EXP5IN)
//                                        .emitEvery((x,y) -> new ElementParticleEffect(hitArray.get(0), x, y, 0, 0, 2f, 0), 0.01f)
//                                        .rotate(-400f)
//                                        .build();
                    AbstractVivianDamageModifier mod = hitArray.get(0).mod;
                    String hitSFX = "";
                    AbstractGameEffect hitVFX;
                    if (mod instanceof FireDamage) {
                        hitVFX = new FlashAtkImgEffect(mo.hb.cX, mo.hb.cY, AttackEffect.FIRE);
                    } else if (mod instanceof IceDamage) {
                        hitVFX = new FrostOrbActivateEffect(mo.hb_x, mo.hb_y);
                        hitSFX = "ORB_FROST_EVOKE";
                    } else if (mod instanceof ElectricDamage) {
                        hitVFX = new LightningOrbActivateEffect(mo.hb.cX, mo.hb.cY);
                        hitSFX = "ORB_LIGHTNING_CHANNEL";
                    } else if (mod instanceof ShadowDamage) {
                        hitVFX = new CardPoofEffect(mo.hb.cX, mo.hb.cY);
                        //hitSFX = "ATTACK_POISON";
                        //hitSFX = "BLUNT_FAST";
                        hitSFX = "APPEAR";
                    } else {
                        hitVFX = new FlashAtkImgEffect(mo.hb.cX, mo.hb.cY, AttackEffect.BLUNT_LIGHT);
                    }
                    AbstractDungeon.effectsQueue.add(new VfxBuilder(ImageMaster.TINY_STAR, Settings.WIDTH / 2f, Settings.HEIGHT, 0.3f)
                            .scale(0.0f, 0.0f, VfxBuilder.Interpolations.SWING)
                            .setColor(Color.GOLD)
                            .moveX(Settings.WIDTH / 2f, mo.hb.cX, VfxBuilder.Interpolations.EXP5IN)
                            .moveY(Settings.HEIGHT, mo.hb.cY, VfxBuilder.Interpolations.EXP5IN)
                            .emitEvery((x, y) -> new ElementParticleEffect(mod, x, y, 0, 0, 2f, 0), 0.01f)
                            //.triggerVfxAt(0.29f, 1, ExplosionSmallEffect::new)
                            //.triggerVfxAt(-.29f, 1, (x,y) -> new FlashAtkImgEffect(mo.hb.cX, mo.hb.cY, AttackEffect.BLUNT_LIGHT))
                            .rotate(-400f)
                            .build());
                    setDelayedHit(mo, hitSFX, hitVFX, 0.15f);
                    //waitTimer += 0.1f;
                }
                if (hitArray.size() > 1) {
                    hitArray.remove(0);
                } else {
                    hitArray.clear();
                }

            } else {
                this.isDone = true;
                //ElementalPower.removeAllElements();
                if (mouseReturn) {
                    ParticleOrbitRenderer.returnOrbitToMouse();
                } else {
                    ParticleOrbitRenderer.returnOrbitToPlayer();
                }
            }
        }
        if (this.isDone) {
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
    }
}
