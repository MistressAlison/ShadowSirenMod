package ShadowSiren.cards.cutCards;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.blockTypes.ElectricBlock;
import ShadowSiren.blockTypes.FireBlock;
import ShadowSiren.blockTypes.IceBlock;
import ShadowSiren.blockTypes.ShadowBlock;
import ShadowSiren.cards.abstractCards.AbstractMultiElementCard;
import ShadowSiren.cards.interfaces.MagicAnimation;
import ShadowSiren.characters.Vivian;
import ShadowSiren.damageModifiers.*;
import basemod.AutoAdd;
import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static ShadowSiren.ShadowSirenMod.makeCardPath;
@AutoAdd.Ignore
public class Polychrome extends AbstractMultiElementCard implements MagicAnimation {


    // TEXT DECLARATION

    public static final String ID = ShadowSirenMod.makeID(Polychrome.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 4;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    private BlockModContainer fire = new BlockModContainer(new FireBlock());
    private BlockModContainer ice = new BlockModContainer(new IceBlock());
    private BlockModContainer elec = new BlockModContainer(new ElectricBlock());
    private BlockModContainer shadow = new BlockModContainer(new ShadowBlock());

    // /STAT DECLARATION/

    public Polychrome() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        DamageModifierManager.addModifier(this, new FireDamage(AbstractVivianDamageModifier.TipType.BLOCK));
        DamageModifierManager.addModifier(this, new IceDamage(AbstractVivianDamageModifier.TipType.BLOCK));
        DamageModifierManager.addModifier(this, new ElectricDamage(AbstractVivianDamageModifier.TipType.BLOCK));
        DamageModifierManager.addModifier(this, new ShadowDamage(AbstractVivianDamageModifier.TipType.BLOCK));
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        this.addToBot(new GainCustomBlockAction(fire, p, block));
        this.addToBot(new GainCustomBlockAction(ice, p, block));
        this.addToBot(new GainCustomBlockAction(elec, p, block));
        this.addToBot(new GainCustomBlockAction(shadow, p, block));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
