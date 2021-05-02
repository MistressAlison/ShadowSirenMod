package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.BurnToAshEffect;
import ShadowSiren.actions.MakeTempCardInExhaustAction;
import ShadowSiren.cards.tempCards.Ash;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class BurnPower extends AbstractPower implements CloneablePowerInterface, HealthBarRenderPower {

    public static final String POWER_ID = ShadowSirenMod.makeID("BurnPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final Color hpBarColor = new Color(-2686721);

    private final AbstractCreature source;
    public boolean activated;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public BurnPower(AbstractCreature owner, AbstractCreature source, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.source = source;

        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("explosive");
        //logger.info("Blasting Fuse?");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        drenchInteraction();
        deathCheck(0);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        drenchInteraction();
        deathCheck(0);
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        return super.onAttackedToChangeDamage(info, damageAmount);
    }

    private void drenchInteraction() {
        if (owner.hasPower(DrenchPower.POWER_ID)) {
            owner.getPower(DrenchPower.POWER_ID).flash();
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(owner.hb.cX, owner.hb.cY, AbstractGameAction.AttackEffect.FIRE));
            AbstractDungeon.effectList.add(new ExplosionSmallEffect(owner.hb.cX, owner.hb.cY));
            this.addToTop(new RemoveSpecificPowerAction(owner, owner, DrenchPower.POWER_ID));
            this.addToBot(new DamageAllEnemiesAction(source, DamageInfo.createDamageMatrix(owner.getPower(DrenchPower.POWER_ID).amount*DrenchPower.STEAM_DAMAGE, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    this.addToBot(new ApplyPowerAction(m, source, new BurnPower(m, source, owner.getPower(DrenchPower.POWER_ID).amount*DrenchPower.STEAM_BURN), owner.getPower(DrenchPower.POWER_ID).amount*DrenchPower.STEAM_BURN, true));
                }
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flashWithoutSound();
            this.amount++;
            updateDescription();
            deathCheck(0);
            //this.addToBot(new DamageAction(owner, new DamageInfo(source, amount, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BurnPower(owner, source, amount);
    }

    @Override
    public int getHealthBarAmount() {
        return amount;
    }

    @Override
    public Color getColor() {
        return hpBarColor;
    }

    public void deathCheck(int damage) {
        if (owner.currentHealth-damage > 0 && owner.currentHealth-damage <= this.amount) {
            if (owner instanceof AbstractMonster && !activated) {
                activated = true;
                boolean cannotLoseFlag = AbstractDungeon.getCurrRoom().cannotLose;
                //this.addToTop(new SuicideAction((AbstractMonster)this.owner));
                if (cannotLoseFlag) AbstractDungeon.getCurrRoom().cannotLose = false;
                owner.gold = 0;
                owner.currentHealth = 0;
                ((AbstractMonster) owner).die(true);
                //TODO still bugged, lol
                if (owner instanceof Darkling || owner instanceof AwakenedOne) owner.halfDead = true;
                owner.healthBarUpdatedEvent();
                if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.cleanCardQueue();
                    AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
                    AbstractDungeon.effectList.add(new DeckPoofEffect((float)Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
                    AbstractDungeon.overlayMenu.hideCombatPanels();
                } else if (cannotLoseFlag) AbstractDungeon.getCurrRoom().cannotLose = true;
            }
        }
    }

    @Override
    public void onDeath() {
        if (activated) {
            //this.addToBot(new MakeTempCardInExhaustAction(new Ash(owner), 1));
            AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(owner.hb.cX, owner.hb.cY));
            AbstractDungeon.effectsQueue.add(new BurnToAshEffect(owner.hb.cX, owner.hb.cY));
            owner.halfDead = true;
        }
    }
}
