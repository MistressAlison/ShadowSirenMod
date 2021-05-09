package ShadowSiren.powers;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.relics.DataCollector;
import ShadowSiren.relics.VoltShroom;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ElectricPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = ShadowSirenMod.makeID("ElectricPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public ElectricPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("mastery");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
            this.flash();
            target.tint.color = Color.YELLOW.cpy();
            target.tint.changeColor(Color.WHITE.cpy());
            int bonus = 0;
            if (target.hasPower(DrenchPower.POWER_ID)) {
                target.getPower(DrenchPower.POWER_ID).flash();
                bonus = target.getPower(DrenchPower.POWER_ID).amount*DrenchPower.ELECTRIC_DAMAGE;
            }
            this.addToTop(new DamageAction(target, new DamageInfo(owner, amount+bonus, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, true));
            //this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
            this.addToTop(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.1F));
            this.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    if (owner instanceof AbstractPlayer && ((AbstractPlayer) owner).hasRelic(VoltShroom.ID)) {
                        ((VoltShroom) ((AbstractPlayer) owner).getRelic(VoltShroom.ID)).electricHook();
                    }
                    this.isDone = true;
                }
            });
            int finalEffect = amount+bonus;
            this.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
                        DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
                        dataCollector.onElecDirect(finalEffect);
                    }
                    this.isDone = true;
                }
            });
        }
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner) {
            this.flash();
            info.owner.tint.color = Color.YELLOW.cpy();
            info.owner.tint.changeColor(Color.WHITE.cpy());
            int bonus = 0;
            if (info.owner.hasPower(DrenchPower.POWER_ID)) {
                info.owner.getPower(DrenchPower.POWER_ID).flash();
                bonus = info.owner.getPower(DrenchPower.POWER_ID).amount*DrenchPower.ELECTRIC_DAMAGE;
            }
            this.addToTop(new DamageAction(info.owner, new DamageInfo(this.owner, amount+bonus, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, true));
            //this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
            this.addToTop(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.1F));
            this.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    if (owner instanceof AbstractPlayer && ((AbstractPlayer) owner).hasRelic(VoltShroom.ID)) {
                        ((VoltShroom) ((AbstractPlayer) owner).getRelic(VoltShroom.ID)).electricHook();
                    }
                    this.isDone = true;
                }
            });
            int finalEffect = amount+bonus;
            this.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
                        DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
                        dataCollector.onElecIndirect(finalEffect);
                    }
                    this.isDone = true;
                }
            });
        }

        return damageAmount;
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        this.addToTop(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.1F));
        if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onGainElec(amount);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.addToTop(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.1F));
        if (AbstractDungeon.player.hasRelic(DataCollector.ID)) {
            DataCollector dataCollector = (DataCollector) AbstractDungeon.player.getRelic(DataCollector.ID);
            dataCollector.onGainElec(stackAmount);
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ElectricPower(owner, amount);
    }
}
