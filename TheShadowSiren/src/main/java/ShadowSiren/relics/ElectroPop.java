package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.ChargePower;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class ElectroPop extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("ElectroPop");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ElectroPop.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ElectroPop.png"));

    private static final int CHARGE_GAINED = 1;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String CHARGE_STAT = DESCRIPTIONS[2];
    private final String PER_COMBAT_STRING = DESCRIPTIONS[3];

    private boolean activated;

    public ElectroPop() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.SOLID);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + CHARGE_GAINED + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        activated = false;
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if(!activated) {
            activated = true;
            if (targetCard.type == AbstractCard.CardType.SKILL) {
                this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargePower(AbstractDungeon.player, CHARGE_GAINED)));
                stats.put(CHARGE_STAT, stats.get(CHARGE_STAT) + CHARGE_GAINED);
            }
        }
    }

    public String getStatsDescription() {
        return CHARGE_STAT + stats.get(CHARGE_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());
        float stat = (float)stats.get(CHARGE_STAT);
        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");
        builder.append(PER_COMBAT_STRING);
        builder.append(perTurnFormat.format((stat / CHARGE_GAINED) * 100 / Math.max(totalCombats, 1))).append("%");
        return builder.toString();
    }

    public void resetStats() {
        stats.put(CHARGE_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(CHARGE_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(CHARGE_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        ElectroPop newRelic = new ElectroPop();
        newRelic.stats = this.stats;
        return newRelic;
    }
}
