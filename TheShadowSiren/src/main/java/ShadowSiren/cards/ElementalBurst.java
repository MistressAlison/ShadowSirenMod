package ShadowSiren.cards;

import IconsAddon.util.DamageModifierHelper;
import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.abstractCards.AbstractInertCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.ElementalPower;
import ShadowSiren.util.ParticleOrbitRenderer;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class ElementalBurst extends AbstractInertCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(ElementalBurst.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DAMAGE = 3;

    // /STAT DECLARATION/


    public ElementalBurst() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = ElementalPower.numActiveElements();
        if (effect > 0) {
            this.addToBot(new AbstractGameAction() {
                boolean firstPass = true;
                float waitTimer = 0f;
                int actionPhase = 0;
                int hits = effect;
                boolean mouseReturn;
                @Override
                public void update() {
                    if (firstPass) {
                        firstPass = false;
                        mouseReturn = ParticleOrbitRenderer.getCurrentTarget() == ParticleOrbitRenderer.TargetLocation.MOUSE;
                        ParticleOrbitRenderer.moveOrbit(m.hb);
                    }
                    if (waitTimer > 0) {
                        waitTimer -= Gdx.graphics.getDeltaTime();
                    } else if (actionPhase == 0) {
                        if (ParticleOrbitRenderer.reachedDestination()) {
                            actionPhase++;
                            waitTimer += 0.15f;
                        }
                    } else if (actionPhase == 1) {
                        if (hits > 0) {
                            AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(m.hb.cX, m.hb.cY));
                            m.damage(DamageModifierHelper.makeBoundDamageInfo(this, p, damage, damageTypeForTurn));
                            hits--;
                            waitTimer += 0.15f;
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
            this.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    ElementalPower.removeAllElements();
                    this.isDone = true;
                }
            });
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
        }
    }
}
