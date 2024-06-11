package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.BurningPower;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class FireFlower extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("FireFlower");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("FireFlower.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("FireFlower.png"));

    private static final int BURN = 1;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String BURN_STAT = DESCRIPTIONS[2];
    private final String PER_TURN_STRING = DESCRIPTIONS[4];
    private final String PER_COMBAT_STRING = DESCRIPTIONS[3];

    public FireFlower() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.FLAT);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BURN + DESCRIPTIONS[1];
    }

    @Override
    public void atTurnStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if(!aM.isDeadOrEscaped()) {
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        if (!aM.hasPower(ArtifactPower.POWER_ID)) {
                            stats.put(BURN_STAT, stats.get(BURN_STAT) + BURN);
                        }
                        this.isDone = true;
                    }
                });
                addToBot(new ApplyPowerAction(aM, AbstractDungeon.player, new BurningPower(aM, AbstractDungeon.player, BURN)));
                /*this.addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        int loss = aM.maxHealth > BURN ? BURN : aM.maxHealth - 1;
                        int hpLoss = aM.maxHealth - loss >= aM.currentHealth ? 0 : aM.currentHealth - (aM.maxHealth - loss);
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AbstractGameAction.AttackEffect.FIRE));
                        aM.decreaseMaxHealth(loss);
                        stats.put(MAX_HP_LOSS_STAT, stats.get(MAX_HP_LOSS_STAT) + loss);
                        stats.put(HP_LOSS_STAT, stats.get(HP_LOSS_STAT) + hpLoss);
                        this.isDone = true;
                    }
                });*/
            }
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        FireFlower newRelic = new FireFlower();
        newRelic.stats = this.stats;
        return newRelic;
    }

    public String getStatsDescription() {
        return BURN_STAT + stats.get(BURN_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        StringBuilder builder = new StringBuilder();
        //builder.append(getStatsDescription());
        builder.append(BURN_STAT).append(stats.get(BURN_STAT));
        float stat = (float)stats.get(BURN_STAT);
        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");
        builder.append(PER_TURN_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        builder.append(PER_COMBAT_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));
        return builder.toString();
    }

    public void resetStats() {
        stats.put(BURN_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(BURN_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(BURN_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }
}
