package ShadowSiren.cards;

import IconsAddon.damageModifiers.AbstractDamageModifier;
import IconsAddon.util.DamageModifierHelper;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractInertCard;
import ShadowSiren.cards.interfaces.MagicAnimation;
import ShadowSiren.cards.interfaces.ModularDescription;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.*;
import ShadowSiren.powers.ElementalPower;
import ShadowSiren.util.ParticleOrbitRenderer;
import ShadowSiren.util.XCostGrabber;
import ShadowSiren.vfx.ElementParticleEffect;
import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;

import java.util.ArrayList;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class StarFall extends AbstractInertCard implements MagicAnimation {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(StarFall.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = -1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DAMAGE = 2;

    // /STAT DECLARATION/

    //TODO rebalance
    public StarFall() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
//        secondMagicNumber = baseSecondMagicNumber = 0;
        //this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = ElementalPower.numActiveElements();
        int hitsPerElement = XCostGrabber.getXCostAmount(this);
        if (effect > 0) {
            this.addToBot(new AbstractGameAction() {
                boolean firstPass = true;
                float waitTimer = 0f;
                int actionPhase = 0;
                boolean mouseReturn;
                AbstractMonster delayedHitCreature;
                String delayedHitSFX = "";
                AbstractGameEffect delayedHitVFX;
                final ArrayList<AbstractVivianDamageModifier> hitArray = new ArrayList<>();
                AbstractMonster mo;
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
                        ParticleOrbitRenderer.moveOrbit(Settings.WIDTH/2f, Settings.HEIGHT);
                        for (AbstractDamageModifier mod : ElementalPower.getActiveElements()) {
                            if (mod instanceof AbstractVivianDamageModifier) {
                                for (int i = 0 ; i < hitsPerElement ; i++) {
                                    hitArray.add((AbstractVivianDamageModifier) mod);
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
                                delayedHitCreature.damage(DamageModifierHelper.makeBoundDamageInfo(this, p, multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(delayedHitCreature)], damageTypeForTurn));
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
                            mo = AbstractDungeon.getRandomMonster();
                            if (mo != null) {
//                                AbstractGameEffect shootStar = new VfxBuilder(ImageMaster.TINY_STAR, Settings.WIDTH/2f, Settings.HEIGHT, 0.3f)
//                                        .scale(0.8f, 1.2f, VfxBuilder.Interpolations.SWING)
//                                        .setColor(Color.GOLD)
//                                        .moveX(Settings.WIDTH/2f, mo.hb.cX, VfxBuilder.Interpolations.EXP5IN)
//                                        .moveY(Settings.HEIGHT, mo.hb.cY, VfxBuilder.Interpolations.EXP5IN)
//                                        .emitEvery((x,y) -> new ElementParticleEffect(hitArray.get(0), x, y, 0, 0, 2f, 0), 0.01f)
//                                        .rotate(-400f)
//                                        .build();
                                AbstractVivianDamageModifier mod = hitArray.get(0);
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
                                AbstractDungeon.effectsQueue.add(new VfxBuilder(ImageMaster.TINY_STAR, Settings.WIDTH/2f, Settings.HEIGHT, 0.3f)
                                        .scale(0.0f, 0.0f, VfxBuilder.Interpolations.SWING)
                                        .setColor(Color.GOLD)
                                        .moveX(Settings.WIDTH/2f, mo.hb.cX, VfxBuilder.Interpolations.EXP5IN)
                                        .moveY(Settings.HEIGHT, mo.hb.cY, VfxBuilder.Interpolations.EXP5IN)
                                        .emitEvery((x,y) -> new ElementParticleEffect(mod, x, y, 0, 0, 2f, 0), 0.01f)
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
            });
        }

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

//    @Override
//    public void applyPowers() {
//        super.applyPowers();
//        updateSecondValue();
//        initializeDescription();
//    }
//
//    private void updateSecondValue() {
//        secondMagicNumber = XCostGrabber.getXCostAmount(this, true) * ElementalPower.getActiveElements().size();
//        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
//    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
        }
    }

//    @Override
//    public void changeDescription() {
//        if (DESCRIPTION != null) {
//            if (secondMagicNumber != 1) {
//                rawDescription = DESCRIPTION;
//            } else {
//                rawDescription = EXTENDED_DESCRIPTION[0];
//            }
//        }
//    }
}
