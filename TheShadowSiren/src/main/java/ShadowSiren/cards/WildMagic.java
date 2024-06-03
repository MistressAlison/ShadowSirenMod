package ShadowSiren.cards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.actions.IntensifyAction;
import ShadowSiren.cards.abstractCards.AbstractInertCard;
import ShadowSiren.characters.Vivian;
import ShadowSiren.powers.ElementalPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import static ShadowSiren.ShadowSirenMod.makeCardPath;

public class WildMagic extends AbstractInertCard {

    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(WildMagic.class.getSimpleName());
    public static final String IMG = makeCardPath("WildMagic.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DAMAGE = 3;
    private static final int INCREASE = 2;
    private static final int UPGRADE_PLUS_INCREASE = 1;

    // /STAT DECLARATION/

    public WildMagic() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = INCREASE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int vfx = MathUtils.random(0, 2);
        switch (vfx) {
            case 0:
                this.addToBot(new VFXAction(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AbstractGameAction.AttackEffect.FIRE)));
                break;
            case 1:
                this.addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        m.tint.color = Color.CYAN.cpy();
                        m.tint.changeColor(Color.WHITE.cpy());
                        this.isDone = true;
                    }
                });
                this.addToBot(new VFXAction(new FrostOrbActivateEffect(m.hb_x, m.hb_y)));
                this.addToBot(new SFXAction("ORB_FROST_EVOKE", 0.2f));
                break;
            case 2:
                this.addToBot(new VFXAction(new LightningEffect(m.drawX, m.drawY), 0.05F));
                this.addToBot(new SFXAction("THUNDERCLAP", 0.05F));
                break;

        }
        this.addToBot(new DamageAction(m, BindingHelper.makeInfo(this, p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        int effect = ElementalPower.numActiveElements() * magicNumber;
        if (effect > 0) {
            this.addToBot(new IntensifyAction(this, effect, IntensifyAction.EffectType.DAMAGE));
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
