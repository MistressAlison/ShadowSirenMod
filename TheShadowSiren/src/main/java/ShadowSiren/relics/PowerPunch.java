package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.cards.interfaces.FistAttack;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class PowerPunch extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("PowerPunch");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("PowerPunch.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("PowerPunch.png"));

    private static final int BONUS_DAMAGE = 3;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String DAMAGE_STAT = DESCRIPTIONS[2];
    private final String PER_COMBAT_STRING = DESCRIPTIONS[3];
    private final String PER_TURN_STRING = DESCRIPTIONS[4];

    public PowerPunch() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.FLAT);
        resetStats();
    }

    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        return c instanceof FistAttack ? damage+BONUS_DAMAGE : damage;
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        //We expressly ignore other damage modifiers here for simplicity
        //If the card we just played is a Fist Attack
        if (targetCard instanceof FistAttack) {
            flash();
            //See if it has a singular target. If so, we added bonus damage on that specific target
            if (useCardAction.target != null) {
                stats.put(DAMAGE_STAT, stats.get(DAMAGE_STAT) + BONUS_DAMAGE);
            } else {
                //Else we can assume it was a multihit attack and add the bonus for each monster that we will actually hit
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDeadOrEscaped()) {
                        stats.put(DAMAGE_STAT, stats.get(DAMAGE_STAT) + BONUS_DAMAGE);
                    }
                }
            }
        }
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BONUS_DAMAGE + DESCRIPTIONS[1];
    }

    public String getStatsDescription() {
        return DAMAGE_STAT + stats.get(DAMAGE_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());
        float stat = (float)stats.get(DAMAGE_STAT);
        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");
        builder.append(PER_TURN_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        builder.append(PER_COMBAT_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));
        return builder.toString();
    }

    public void resetStats() {
        stats.put(DAMAGE_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(DAMAGE_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(DAMAGE_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        PowerPunch newRelic = new PowerPunch();
        newRelic.stats = this.stats;
        return newRelic;
    }
}
