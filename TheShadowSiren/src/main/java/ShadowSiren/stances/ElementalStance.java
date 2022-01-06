//package ShadowSiren.stances;
//
//import IconsAddon.damageModifiers.AbstractDamageModifier;
//import IconsAddon.util.DamageModifierManager;
//import ShadowSiren.ShadowSirenMod;
//import ShadowSiren.cards.abstractCards.AbstractElementalCard;
//import ShadowSiren.damageModifiers.AbstractVivianDamageModifier;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.localization.StanceStrings;
//import com.megacrit.cardcrawl.stances.AbstractStance;
//
//import java.util.ArrayList;
//
//public class ElementalStance extends AbstractStance {
//    public static final String STANCE_ID = ShadowSirenMod.makeID("ElementalStance");
//    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
//
//
//    public ElementalStance() {
//        this(null);
//    }
//
//    public ElementalStance(AbstractCard card) {
//        this.ID = STANCE_ID;
//        this.name = stanceString.NAME;
//        this.updateDescription();
//        if (card != null) {
//            grabElementsOffCard(card);
//        }
//    }
//
//    @Override
//    public void onPlayCard(AbstractCard card) {
//        if (card instanceof AbstractElementalCard) {
//            grabElementsOffCard(card);
//        }
//    }
//
//    public void grabElementsOffCard(AbstractCard card) {
//        ArrayList<AbstractDamageModifier> mods = new ArrayList<>();
//        for (AbstractDamageModifier mod : DamageModifierManager.modifiers(card)) {
//            if (mod instanceof AbstractVivianDamageModifier && ((AbstractVivianDamageModifier) mod).isAnElement) {
//                if (!mods.contains(mod)) {
//                    mods.add(mod);
//                }
//            }
//        }
//        if (!mods.isEmpty()) {
//            DamageModifierManager.removeAllModifiers(this);
//            for (AbstractDamageModifier mod : mods) {
//                DamageModifierManager.addModifier(this, mod);
//            }
//            updateDescription();
//        }
//    }
//
//    @Override
//    public void updateDescription() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(stanceString.DESCRIPTION[0]);
//        if (DamageModifierManager.modifiers(this).size() > 0) {
//            sb.append(stanceString.DESCRIPTION[1]);
//        }
//        for (AbstractDamageModifier mod : DamageModifierManager.modifiers(this)) {
//            if (mod instanceof AbstractVivianDamageModifier) {
//                sb.append(" NL ").append(((AbstractVivianDamageModifier) mod).cardStrings.DESCRIPTION);
//            }
//        }
//        this.description = sb.toString();
//    }
//}
