//package ShadowSiren.cards.starCards;
//
//import ShadowSiren.ShadowSirenMod;
//import ShadowSiren.cards.abstractCards.AbstractStarCard;
//import ShadowSiren.characters.Vivian;
//import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
//import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
//import com.megacrit.cardcrawl.actions.common.HealAction;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.powers.RegenPower;
//
//import static ShadowSiren.ShadowSirenMod.makeCardPath;
//
//public class SweetFeast extends AbstractStarCard {
//
//    // TEXT DECLARATION
//
//    public static final String ID = ShadowSirenMod.makeID(SweetFeast.class.getSimpleName());
//    public static final String IMG = makeCardPath("PlaceholderSkill.png");
//
//    // /TEXT DECLARATION/
//
//
//    // STAT DECLARATION
//
//    private static final CardRarity RARITY = CardRarity.SPECIAL;
//    private static final CardTarget TARGET = CardTarget.SELF;
//    private static final CardType TYPE = CardType.SKILL;
//    public static final CardColor COLOR = Vivian.Enums.VOODOO_CARD_COLOR;
//
//    private static final int COST = 4;
//    private static final int HEAL = 10;
//    private static final int UPGRADE_PLUS_HEAL = 5;
//    private static final int ENERGY = 2;
//    private static final int REGEN = 5;
//    // /STAT DECLARATION/
//
//
//    public SweetFeast() {
//        this(false);
//    }
//
//    public SweetFeast(boolean showStarCost) {
//        super(ID, IMG, TYPE, COLOR, TARGET, showStarCost);
//        baseMagicNumber = magicNumber = HEAL;
//        baseSecondMagicNumber = secondMagicNumber = REGEN;
//        purgeOnUse = true;
//    }
//
//    // Actions the card should do.
//    @Override
//    public void use(AbstractPlayer p, AbstractMonster m) {
//        this.addToBot(new HealAction(p, p, magicNumber));
//        this.addToBot(new GainEnergyAction(ENERGY));
//        this.addToBot(new ApplyPowerAction(p, p, new RegenPower(p, secondMagicNumber)));
//    }
//
//    //Upgraded stats.
//    @Override
//    public void upgrade() {
//        if (!upgraded) {
//            upgradeName();
//            upgradeMagicNumber(UPGRADE_PLUS_HEAL);
//            initializeDescription();
//        }
//    }
//
//    @Override
//    public int getSpawnCost() {
//        return COST;
//    }
//}
