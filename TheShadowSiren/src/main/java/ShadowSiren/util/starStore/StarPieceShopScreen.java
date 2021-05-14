package ShadowSiren.util.starStore;
/*
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.*;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.ui.buttons.ConfirmButton;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.FloatyEffect;
import com.megacrit.cardcrawl.vfx.ShopSpeechBubble;
import com.megacrit.cardcrawl.vfx.SpeechTextEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class StarPieceShopScreen extends ShopScreen {
    private static final Logger logger = LogManager.getLogger(ShopScreen.class.getName());
    private static final TutorialStrings tutorialStrings;
    public static final String[] MSG;
    public static final String[] LABEL;
    private static final CharacterStrings characterStrings;
    public static final String[] NAMES;
    public static final String[] TEXT;
    public boolean isActive = true;
    private static Texture rugImg;
    private static Texture removeServiceImg;
    private static Texture soldOutImg;
    private static Texture handImg;
    private float rugY;
    private static final float RUG_SPEED = 5.0F;
    public ArrayList<AbstractCard> coloredCards;
    public ArrayList<AbstractCard> colorlessCards;
    private static final float DRAW_START_X;
    private static final int NUM_CARDS_PER_LINE = 5;
    private static final float CARD_PRICE_JITTER = 0.1F;
    private static final float TOP_ROW_Y;
    private static final float BOTTOM_ROW_Y;
    private float speechTimer;
    private static final float MIN_IDLE_MSG_TIME = 40.0F;
    private static final float MAX_IDLE_MSG_TIME = 60.0F;
    private static final float SPEECH_DURATION = 4.0F;
    private static final float SPEECH_TEXT_R_X;
    private static final float SPEECH_TEXT_L_X;
    private static final float SPEECH_TEXT_Y;
    private ShopSpeechBubble speechBubble;
    private SpeechTextEffect dialogTextEffect;
    private static final String WELCOME_MSG;
    private ArrayList<String> idleMessages;
    private boolean saidWelcome;
    private boolean somethingHovered;
    private ArrayList<StoreRelic> relics;
    private static final float RELIC_PRICE_JITTER = 0.05F;
    private ArrayList<StorePotion> potions;
    private static final float POTION_PRICE_JITTER = 0.05F;
    public boolean purgeAvailable;
    public static int purgeCost;
    public static int actualPurgeCost;
    private static final int PURGE_COST_RAMP = 25;
    private boolean purgeHovered;
    private float purgeCardX;
    private float purgeCardY;
    private float purgeCardScale;
    private final FloatyEffect f_effect;
    private float handTimer;
    private float handX;
    private float handY;
    private float handTargetX;
    private float handTargetY;
    private static final float HAND_SPEED = 6.0F;
    private static float HAND_W;
    private static float HAND_H;
    private float notHoveredTimer;
    private static final float GOLD_IMG_WIDTH;
    private static final float COLORLESS_PRICE_BUMP = 1.2F;
    private OnSaleTag saleTag;
    private static final float GOLD_IMG_OFFSET_X;
    private static final float GOLD_IMG_OFFSET_Y;
    private static final float PRICE_TEXT_OFFSET_X;
    private static final float PRICE_TEXT_OFFSET_Y;
    public ConfirmButton confirmButton;
    public StoreRelic touchRelic;
    public StorePotion touchPotion;
    private AbstractCard touchCard;
    private boolean touchPurge;

    public StarPieceShopScreen() {
        super();
        this.rugY = (float) Settings.HEIGHT / 2.0F + 540.0F * Settings.yScale;
        this.coloredCards = new ArrayList();
        this.colorlessCards = new ArrayList();
        this.speechTimer = 0.0F;
        this.speechBubble = null;
        this.dialogTextEffect = null;
        this.idleMessages = new ArrayList();
        this.saidWelcome = false;
        this.somethingHovered = false;
        this.relics = new ArrayList();
        this.potions = new ArrayList();
        this.purgeAvailable = false;
        this.purgeHovered = false;
        this.purgeCardScale = 1.0F;
        this.f_effect = new FloatyEffect(20.0F, 0.1F);
        this.handTimer = 1.0F;
        this.handX = (float)Settings.WIDTH / 2.0F;
        this.handY = (float)Settings.HEIGHT;
        this.handTargetX = 0.0F;
        this.handTargetY = (float)Settings.HEIGHT;
        this.notHoveredTimer = 0.0F;
        this.confirmButton = new ConfirmButton();
        this.touchRelic = null;
        this.touchPotion = null;
        this.touchCard = null;
        this.touchPurge = false;
    }

    public void init(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        this.idleMessages.clear();
        if (AbstractDungeon.id.equals("TheEnding")) {
            Collections.addAll(this.idleMessages, Merchant.ENDING_TEXT);
        } else {
            Collections.addAll(this.idleMessages, TEXT);
        }

        if (rugImg == null) {
            switch(Settings.language) {
                case DEU:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/deu.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/deu.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/deu.png");
                    break;
                case EPO:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/epo.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/epo.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/epo.png");
                    break;
                case FRA:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/fra.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/fra.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/fra.png");
                    break;
                case ITA:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/ita.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/ita.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/ita.png");
                    break;
                case JPN:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/jpn.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/jpn.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/jpn.png");
                    break;
                case KOR:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/kor.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/kor.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/kor.png");
                    break;
                case RUS:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/rus.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/rus.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/rus.png");
                    break;
                case THA:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/tha.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/tha.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/tha.png");
                    break;
                case UKR:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/ukr.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/ukr.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/ukr.png");
                    break;
                case ZHS:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/zhs.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/zhs.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/zhs.png");
                    break;
                default:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/eng.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/eng.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/eng.png");
            }

            handImg = ImageMaster.loadImage("images/npcs/merchantHand.png");
        }

        HAND_W = (float)handImg.getWidth() * Settings.scale;
        HAND_H = (float)handImg.getHeight() * Settings.scale;
        this.coloredCards.clear();
        this.colorlessCards.clear();
        this.coloredCards = coloredCards;
        this.colorlessCards = colorlessCards;
        this.initCards();
        this.initRelics();
        this.initPotions();
        this.purgeAvailable = true;
        this.purgeCardY = -1000.0F;
        this.purgeCardX = (float)Settings.WIDTH * 0.73F * Settings.scale;
        this.purgeCardScale = 0.7F;
        actualPurgeCost = purgeCost;
        if (AbstractDungeon.ascensionLevel >= 16) {
            this.applyDiscount(1.1F, false);
        }

        if (AbstractDungeon.player.hasRelic("The Courier")) {
            this.applyDiscount(0.8F, true);
        }

        if (AbstractDungeon.player.hasRelic("Membership Card")) {
            this.applyDiscount(0.5F, true);
        }

        if (AbstractDungeon.player.hasRelic("Smiling Mask")) {
            actualPurgeCost = 50;
        }

    }

    public static void resetPurgeCost() {
        purgeCost = 75;
        actualPurgeCost = 75;
    }

    private void initCards() {
        int tmp = (int)((float)Settings.WIDTH - DRAW_START_X * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4;
        float padX = (float)((int)((float)tmp + AbstractCard.IMG_WIDTH_S));

        int i;
        float tmpPrice;
        AbstractCard c;
        Iterator var6;
        AbstractRelic r;
        for(i = 0; i < this.coloredCards.size(); ++i) {
            tmpPrice = (float)AbstractCard.getPrice(((AbstractCard)this.coloredCards.get(i)).rarity) * AbstractDungeon.merchantRng.random(1-CARD_PRICE_JITTER, 1+CARD_PRICE_JITTER);
            c = (AbstractCard)this.coloredCards.get(i);
            c.price = (int)tmpPrice;
            c.current_x = (float)(Settings.WIDTH / 2);
            c.target_x = DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * (float)i;
            var6 = AbstractDungeon.player.relics.iterator();

            while(var6.hasNext()) {
                r = (AbstractRelic)var6.next();
                r.onPreviewObtainCard(c);
            }
        }

        for(i = 0; i < this.colorlessCards.size(); ++i) {
            tmpPrice = (float)AbstractCard.getPrice(((AbstractCard)this.colorlessCards.get(i)).rarity) * AbstractDungeon.merchantRng.random(1-CARD_PRICE_JITTER, 1+CARD_PRICE_JITTER);
            tmpPrice *= COLORLESS_PRICE_BUMP;
            c = (AbstractCard)this.colorlessCards.get(i);
            c.price = (int)tmpPrice;
            c.current_x = (float)(Settings.WIDTH / 2);
            c.target_x = DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * (float)i;
            var6 = AbstractDungeon.player.relics.iterator();

            while(var6.hasNext()) {
                r = (AbstractRelic)var6.next();
                r.onPreviewObtainCard(c);
            }
        }

        AbstractCard saleCard = (AbstractCard)this.coloredCards.get(AbstractDungeon.merchantRng.random(0, 4));
        saleCard.price /= 2;
        this.saleTag = new OnSaleTag(saleCard);
        this.setStartingCardPositions();
    }

    public static void purgeCard() {
        AbstractDungeon.player.loseGold(actualPurgeCost);
        CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
        purgeCost += PURGE_COST_RAMP;
        actualPurgeCost = purgeCost;
        if (AbstractDungeon.player.hasRelic("Smiling Mask")) {
            actualPurgeCost = 50;
            AbstractDungeon.player.getRelic("Smiling Mask").stopPulse();
        } else if (AbstractDungeon.player.hasRelic("The Courier") && AbstractDungeon.player.hasRelic("Membership Card")) {
            actualPurgeCost = MathUtils.round((float)purgeCost * 0.8F * 0.5F);
        } else if (AbstractDungeon.player.hasRelic("The Courier")) {
            actualPurgeCost = MathUtils.round((float)purgeCost * 0.8F);
        } else if (AbstractDungeon.player.hasRelic("Membership Card")) {
            actualPurgeCost = MathUtils.round((float)purgeCost * 0.5F);
        }

    }

    public void updatePurge() {
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            purgeCard();
            Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

            while(var1.hasNext()) {
                AbstractCard card = (AbstractCard)var1.next();
                CardCrawlGame.metricData.addPurgedItem(card.getMetricID());
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                AbstractDungeon.player.masterDeck.removeCard(card);
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.shopScreen.purgeAvailable = false;
        }

    }

    public static String getCantBuyMsg() {
        ArrayList<String> list = new ArrayList();
        list.add(NAMES[1]);
        list.add(NAMES[2]);
        list.add(NAMES[3]);
        list.add(NAMES[4]);
        list.add(NAMES[5]);
        list.add(NAMES[6]);
        return (String)list.get(MathUtils.random(list.size() - 1));
    }

    public static String getBuyMsg() {
        ArrayList<String> list = new ArrayList();
        list.add(NAMES[7]);
        list.add(NAMES[8]);
        list.add(NAMES[9]);
        list.add(NAMES[10]);
        list.add(NAMES[11]);
        return (String)list.get(MathUtils.random(list.size() - 1));
    }

    public void applyDiscount(float multiplier, boolean affectPurge) {
        Iterator var3;
        StoreRelic r;
        for(var3 = this.relics.iterator(); var3.hasNext(); r.price = MathUtils.round((float)r.price * multiplier)) {
            r = (StoreRelic)var3.next();
        }

        StorePotion p;
        for(var3 = this.potions.iterator(); var3.hasNext(); p.price = MathUtils.round((float)p.price * multiplier)) {
            p = (StorePotion)var3.next();
        }

        AbstractCard c;
        for(var3 = this.coloredCards.iterator(); var3.hasNext(); c.price = MathUtils.round((float)c.price * multiplier)) {
            c = (AbstractCard)var3.next();
        }

        for(var3 = this.colorlessCards.iterator(); var3.hasNext(); c.price = MathUtils.round((float)c.price * multiplier)) {
            c = (AbstractCard)var3.next();
        }

        if (AbstractDungeon.player.hasRelic("Smiling Mask")) {
            actualPurgeCost = 50;
        } else if (affectPurge) {
            actualPurgeCost = MathUtils.round((float)purgeCost * multiplier);
        }

    }

    private void initRelics() {
        this.relics.clear();
        this.relics = new ArrayList();

        for(int i = 0; i < 3; ++i) {
            AbstractRelic tempRelic = null;
            if (i != 2) {
                tempRelic = AbstractDungeon.returnRandomRelicEnd(rollRelicTier());
            } else {
                tempRelic = AbstractDungeon.returnRandomRelicEnd(AbstractRelic.RelicTier.SHOP);
            }

            StoreRelic relic = new StoreRelic(tempRelic, i, this);
            if (!Settings.isDailyRun) {
                relic.price = MathUtils.round((float)relic.price * AbstractDungeon.merchantRng.random(1-RELIC_PRICE_JITTER, 1+RELIC_PRICE_JITTER));
            }

            this.relics.add(relic);
        }

    }

    private void initPotions() {
        this.potions.clear();
        this.potions = new ArrayList();

        for(int i = 0; i < 3; ++i) {
            StorePotion potion = new StorePotion(AbstractDungeon.returnRandomPotion(), i, this);
            if (!Settings.isDailyRun) {
                potion.price = MathUtils.round((float)potion.price * AbstractDungeon.merchantRng.random(1-POTION_PRICE_JITTER, 1+POTION_PRICE_JITTER));
            }

            this.potions.add(potion);
        }

    }

    public void getNewPrice(StoreRelic r) {
        int retVal = r.price;
        if (!Settings.isDailyRun) {
            retVal = MathUtils.round((float)retVal * AbstractDungeon.merchantRng.random(1-RELIC_PRICE_JITTER, 1+RELIC_PRICE_JITTER));
        }

        if (AbstractDungeon.player.hasRelic("The Courier")) {
            retVal = this.applyDiscountToRelic(retVal, 0.8F);
        }

        if (AbstractDungeon.player.hasRelic("Membership Card")) {
            retVal = this.applyDiscountToRelic(retVal, 0.5F);
        }

        r.price = retVal;
    }

    public void getNewPrice(StorePotion r) {
        int retVal = r.price;
        if (!Settings.isDailyRun) {
            retVal = MathUtils.round((float)retVal * AbstractDungeon.merchantRng.random(1-POTION_PRICE_JITTER, 1+POTION_PRICE_JITTER));
        }

        if (AbstractDungeon.player.hasRelic("The Courier")) {
            retVal = this.applyDiscountToRelic(retVal, 0.8F);
        }

        if (AbstractDungeon.player.hasRelic("Membership Card")) {
            retVal = this.applyDiscountToRelic(retVal, 0.5F);
        }

        r.price = retVal;
    }

    private int applyDiscountToRelic(int price, float multiplier) {
        return MathUtils.round((float)price * multiplier);
    }

    public static AbstractRelic.RelicTier rollRelicTier() {
        int roll = AbstractDungeon.merchantRng.random(99);
        logger.info("ROLL " + roll);
        if (roll < 48) {
            return AbstractRelic.RelicTier.COMMON;
        } else {
            return roll < 82 ? AbstractRelic.RelicTier.UNCOMMON : AbstractRelic.RelicTier.RARE;
        }
    }

    private void setStartingCardPositions() {
        int tmp = (int)((float)Settings.WIDTH - DRAW_START_X * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4;
        float padX = (float)((int)((float)tmp + AbstractCard.IMG_WIDTH_S)) + 10.0F * Settings.scale;

        int i;
        for(i = 0; i < this.coloredCards.size(); ++i) {
            ((AbstractCard)this.coloredCards.get(i)).updateHoverLogic();
            ((AbstractCard)this.coloredCards.get(i)).targetDrawScale = 0.75F;
            ((AbstractCard)this.coloredCards.get(i)).current_x = DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * (float)i;
            ((AbstractCard)this.coloredCards.get(i)).target_x = DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * (float)i;
            ((AbstractCard)this.coloredCards.get(i)).target_y = 9999.0F * Settings.scale;
            ((AbstractCard)this.coloredCards.get(i)).current_y = 9999.0F * Settings.scale;
        }

        for(i = 0; i < this.colorlessCards.size(); ++i) {
            ((AbstractCard)this.colorlessCards.get(i)).updateHoverLogic();
            ((AbstractCard)this.colorlessCards.get(i)).targetDrawScale = 0.75F;
            ((AbstractCard)this.colorlessCards.get(i)).current_x = DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * (float)i;
            ((AbstractCard)this.colorlessCards.get(i)).target_x = DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * (float)i;
            ((AbstractCard)this.colorlessCards.get(i)).target_y = 9999.0F * Settings.scale;
            ((AbstractCard)this.colorlessCards.get(i)).current_y = 9999.0F * Settings.scale;
        }

    }

    public void open() {
        this.resetTouchscreenVars();
        CardCrawlGame.sound.play("SHOP_OPEN");
        this.setStartingCardPositions();
        this.purgeCardY = -1000.0F;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.SHOP;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.cancelButton.show(NAMES[12]);
        Iterator var1 = this.relics.iterator();

        StoreRelic r;
        while(var1.hasNext()) {
            r = (StoreRelic)var1.next();
            r.hide();
        }

        var1 = this.potions.iterator();

        while(var1.hasNext()) {
            StorePotion p = (StorePotion)var1.next();
            p.hide();
        }

        this.rugY = (float)Settings.HEIGHT;
        this.handX = (float)Settings.WIDTH / 2.0F;
        this.handY = (float)Settings.HEIGHT;
        this.handTargetX = this.handX;
        this.handTargetY = this.handY;
        this.handTimer = 1.0F;
        this.speechTimer = 1.5F;
        this.speechBubble = null;
        this.dialogTextEffect = null;
        AbstractDungeon.overlayMenu.showBlackScreen();
        var1 = this.coloredCards.iterator();

        AbstractCard c;
        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            UnlockTracker.markCardAsSeen(c.cardID);
        }

        var1 = this.colorlessCards.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            UnlockTracker.markCardAsSeen(c.cardID);
        }

        var1 = this.relics.iterator();

        while(var1.hasNext()) {
            r = (StoreRelic)var1.next();
            if (r.relic != null) {
                UnlockTracker.markRelicAsSeen(r.relic.relicId);
            }
        }

        if (ModHelper.isModEnabled("Hoarder")) {
            this.purgeAvailable = false;
        }

    }

    private void resetTouchscreenVars() {
        if (Settings.isTouchScreen) {
            this.confirmButton.hide();
            this.confirmButton.isDisabled = false;
            this.touchRelic = null;
            this.touchCard = null;
            this.touchPotion = null;
            this.touchPurge = false;
        }

    }

    public void update() {
        if (Settings.isTouchScreen) {
            this.confirmButton.update();
            if (InputHelper.justClickedLeft && this.confirmButton.hb.hovered) {
                this.confirmButton.hb.clickStarted = true;
            }

            if (InputHelper.justReleasedClickLeft && !this.confirmButton.hb.hovered) {
                this.resetTouchscreenVars();
            } else if (this.confirmButton.hb.clicked) {
                this.confirmButton.hb.clicked = false;
                if (this.touchRelic != null) {
                    this.touchRelic.purchaseRelic();
                } else if (this.touchCard != null) {
                    this.purchaseCard(this.touchCard);
                } else if (this.touchPotion != null) {
                    this.touchPotion.purchasePotion();
                } else if (this.touchPurge) {
                    this.purchasePurge();
                }

                this.resetTouchscreenVars();
            }
        }

        if (this.handTimer != 0.0F) {
            this.handTimer -= Gdx.graphics.getDeltaTime();
            if (this.handTimer < 0.0F) {
                this.handTimer = 0.0F;
            }
        }

        this.f_effect.update();
        this.somethingHovered = false;
        this.updateControllerInput();
        this.updatePurgeCard();
        this.updatePurge();
        this.updateRelics();
        this.updatePotions();
        this.updateRug();
        this.updateSpeech();
        this.updateCards();
        this.updateHand();
        AbstractCard hoveredCard = null;
        Iterator var2 = this.coloredCards.iterator();

        AbstractCard c;
        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c.hb.hovered) {
                hoveredCard = c;
                this.somethingHovered = true;
                this.moveHand(c.current_x - AbstractCard.IMG_WIDTH / 2.0F, c.current_y);
                break;
            }
        }

        var2 = this.colorlessCards.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            if (c.hb.hovered) {
                hoveredCard = c;
                this.somethingHovered = true;
                this.moveHand(c.current_x - AbstractCard.IMG_WIDTH / 2.0F, c.current_y);
                break;
            }
        }

        if (!this.somethingHovered) {
            this.notHoveredTimer += Gdx.graphics.getDeltaTime();
            if (this.notHoveredTimer > 1.0F) {
                this.handTargetY = (float)Settings.HEIGHT;
            }
        } else {
            this.notHoveredTimer = 0.0F;
        }

        if (hoveredCard != null && InputHelper.justClickedLeft) {
            hoveredCard.hb.clickStarted = true;
        }

        if (hoveredCard != null && (InputHelper.justClickedRight || CInputActionSet.proceed.isJustPressed())) {
            InputHelper.justClickedRight = false;
            CardCrawlGame.cardPopup.open(hoveredCard);
        }

        if (hoveredCard != null && (hoveredCard.hb.clicked || CInputActionSet.select.isJustPressed())) {
            hoveredCard.hb.clicked = false;
            if (!Settings.isTouchScreen) {
                this.purchaseCard(hoveredCard);
            } else if (this.touchCard == null) {
                if (AbstractDungeon.player.gold < hoveredCard.price) {
                    this.speechTimer = MathUtils.random(MIN_IDLE_MSG_TIME, MAX_IDLE_MSG_TIME);
                    this.playCantBuySfx();
                    this.createSpeech(getCantBuyMsg());
                } else {
                    this.confirmButton.hideInstantly();
                    this.confirmButton.show();
                    this.confirmButton.isDisabled = false;
                    this.confirmButton.hb.clickStarted = false;
                    this.touchCard = hoveredCard;
                }
            }
        }

    }

    private void purchaseCard(AbstractCard hoveredCard) {
        if (AbstractDungeon.player.gold >= hoveredCard.price) {
            CardCrawlGame.metricData.addShopPurchaseData(hoveredCard.getMetricID());
            AbstractDungeon.topLevelEffects.add(new FastCardObtainEffect(hoveredCard, hoveredCard.current_x, hoveredCard.current_y));
            AbstractDungeon.player.loseGold(hoveredCard.price);
            CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
            if (!AbstractDungeon.player.hasRelic("The Courier")) {
                this.coloredCards.remove(hoveredCard);
                this.colorlessCards.remove(hoveredCard);
            } else if (hoveredCard.color == AbstractCard.CardColor.COLORLESS) {
                AbstractCard.CardRarity tempRarity = AbstractCard.CardRarity.UNCOMMON;
                if (AbstractDungeon.merchantRng.random() < AbstractDungeon.colorlessRareChance) {
                    tempRarity = AbstractCard.CardRarity.RARE;
                }

                AbstractCard c = AbstractDungeon.getColorlessCardFromPool(tempRarity).makeCopy();
                Iterator var8 = AbstractDungeon.player.relics.iterator();

                while(var8.hasNext()) {
                    AbstractRelic r = (AbstractRelic)var8.next();
                    r.onPreviewObtainCard(c);
                }

                c.current_x = hoveredCard.current_x;
                c.current_y = hoveredCard.current_y;
                c.target_x = c.current_x;
                c.target_y = c.current_y;
                this.setPrice(c);
                this.colorlessCards.set(this.colorlessCards.indexOf(hoveredCard), c);
            } else {
                AbstractCard c;
                for(c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), hoveredCard.type, false).makeCopy(); c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), hoveredCard.type, false).makeCopy()) {
                }

                Iterator var3 = AbstractDungeon.player.relics.iterator();

                while(var3.hasNext()) {
                    AbstractRelic r = (AbstractRelic)var3.next();
                    r.onPreviewObtainCard(c);
                }

                c.current_x = hoveredCard.current_x;
                c.current_y = hoveredCard.current_y;
                c.target_x = c.current_x;
                c.target_y = c.current_y;
                this.setPrice(c);
                this.coloredCards.set(this.coloredCards.indexOf(hoveredCard), c);
            }

            hoveredCard = null;
            InputHelper.justClickedLeft = false;
            this.notHoveredTimer = 1.0F;
            this.speechTimer = MathUtils.random(MIN_IDLE_MSG_TIME, MAX_IDLE_MSG_TIME);
            this.playBuySfx();
            this.createSpeech(getBuyMsg());
        } else {
            this.speechTimer = MathUtils.random(MIN_IDLE_MSG_TIME, MAX_IDLE_MSG_TIME);
            this.playCantBuySfx();
            this.createSpeech(getCantBuyMsg());
        }

    }

    private void updateCards() {
        int i;
        for(i = 0; i < this.coloredCards.size(); ++i) {
            ((AbstractCard)this.coloredCards.get(i)).update();
            ((AbstractCard)this.coloredCards.get(i)).updateHoverLogic();
            ((AbstractCard)this.coloredCards.get(i)).current_y = this.rugY + TOP_ROW_Y;
            ((AbstractCard)this.coloredCards.get(i)).target_y = ((AbstractCard)this.coloredCards.get(i)).current_y;
        }

        for(i = 0; i < this.colorlessCards.size(); ++i) {
            ((AbstractCard)this.colorlessCards.get(i)).update();
            ((AbstractCard)this.colorlessCards.get(i)).updateHoverLogic();
            ((AbstractCard)this.colorlessCards.get(i)).current_y = this.rugY + BOTTOM_ROW_Y;
            ((AbstractCard)this.colorlessCards.get(i)).target_y = ((AbstractCard)this.colorlessCards.get(i)).current_y;
        }

    }

    private void setPrice(AbstractCard card) {
        float tmpPrice = (float)AbstractCard.getPrice(card.rarity) * AbstractDungeon.merchantRng.random(1-CARD_PRICE_JITTER, 1+CARD_PRICE_JITTER);
        if (card.color == AbstractCard.CardColor.COLORLESS) {
            tmpPrice *= COLORLESS_PRICE_BUMP;
        }

        if (AbstractDungeon.player.hasRelic("The Courier")) {
            tmpPrice *= 0.8F;
        }

        if (AbstractDungeon.player.hasRelic("Membership Card")) {
            tmpPrice *= 0.5F;
        }

        card.price = (int)tmpPrice;
    }

    public void moveHand(float x, float y) {
        this.handTargetX = x - 50.0F * Settings.xScale;
        this.handTargetY = y + 90.0F * Settings.yScale;
    }

    private void updateControllerInput() {
        if (Settings.isControllerMode && !AbstractDungeon.topPanel.selectPotionMode && AbstractDungeon.topPanel.potionUi.isHidden && !AbstractDungeon.player.viewingRelics) {
            StoreSelectionType type = null;
            int index = 0;

            Iterator var3;
            AbstractCard c;
            for(var3 = this.coloredCards.iterator(); var3.hasNext(); ++index) {
                c = (AbstractCard)var3.next();
                if (c.hb.hovered) {
                    type = StoreSelectionType.COLOR_CARD;
                    break;
                }
            }

            if (type == null) {
                index = 0;

                for(var3 = this.relics.iterator(); var3.hasNext(); ++index) {
                    StoreRelic r = (StoreRelic)var3.next();
                    if (r.relic.hb.hovered) {
                        type = StoreSelectionType.RELIC;
                        break;
                    }
                }
            }

            if (type == null) {
                index = 0;

                for(var3 = this.colorlessCards.iterator(); var3.hasNext(); ++index) {
                    c = (AbstractCard)var3.next();
                    if (c.hb.hovered) {
                        type = StoreSelectionType.COLORLESS_CARD;
                        break;
                    }
                }
            }

            if (type == null) {
                index = 0;

                for(var3 = this.potions.iterator(); var3.hasNext(); ++index) {
                    StorePotion p = (StorePotion)var3.next();
                    if (p.potion.hb.hovered) {
                        type = StoreSelectionType.POTION;
                        break;
                    }
                }
            }

            if (type == null && this.purgeHovered) {
                type = StoreSelectionType.PURGE;
            }

            if (type == null) {
                if (!this.coloredCards.isEmpty()) {
                    Gdx.input.setCursorPosition((int)((AbstractCard)this.coloredCards.get(0)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.coloredCards.get(0)).hb.cY);
                } else if (!this.colorlessCards.isEmpty()) {
                    Gdx.input.setCursorPosition((int)((AbstractCard)this.colorlessCards.get(0)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.colorlessCards.get(0)).hb.cY);
                } else if (!this.relics.isEmpty()) {
                    Gdx.input.setCursorPosition((int)((StoreRelic)this.relics.get(0)).relic.hb.cX, Settings.HEIGHT - (int)((StoreRelic)this.relics.get(0)).relic.hb.cY);
                } else if (!this.potions.isEmpty()) {
                    Gdx.input.setCursorPosition((int)((StorePotion)this.potions.get(0)).potion.hb.cX, Settings.HEIGHT - (int)((StorePotion)this.potions.get(0)).potion.hb.cY);
                } else if (this.purgeAvailable) {
                    Gdx.input.setCursorPosition((int)this.purgeCardX, Settings.HEIGHT - (int)this.purgeCardY);
                }
            } else {
                switch(type) {
                    case COLOR_CARD:
                        if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                            if (!CInputActionSet.right.isJustPressed() && !CInputActionSet.altRight.isJustPressed()) {
                                if (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) {
                                    if (((AbstractCard)this.coloredCards.get(index)).hb.cX < 550.0F * Settings.scale && !this.colorlessCards.isEmpty()) {
                                        Gdx.input.setCursorPosition((int)((AbstractCard)this.colorlessCards.get(0)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.colorlessCards.get(0)).hb.cY);
                                        return;
                                    }

                                    if (((AbstractCard)this.coloredCards.get(index)).hb.cX < 850.0F * Settings.scale) {
                                        if (!this.colorlessCards.isEmpty()) {
                                            if (this.colorlessCards.size() > 1) {
                                                Gdx.input.setCursorPosition((int)((AbstractCard)this.colorlessCards.get(1)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.colorlessCards.get(1)).hb.cY);
                                            } else {
                                                Gdx.input.setCursorPosition((int)((AbstractCard)this.colorlessCards.get(0)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.colorlessCards.get(0)).hb.cY);
                                            }

                                            return;
                                        }

                                        if (!this.relics.isEmpty()) {
                                            Gdx.input.setCursorPosition((int)((StoreRelic)this.relics.get(0)).relic.hb.cX, Settings.HEIGHT - (int)((StoreRelic)this.relics.get(0)).relic.hb.cY);
                                            return;
                                        }

                                        if (!this.potions.isEmpty()) {
                                            Gdx.input.setCursorPosition((int)((StorePotion)this.potions.get(0)).potion.hb.cX, Settings.HEIGHT - (int)((StorePotion)this.potions.get(0)).potion.hb.cY);
                                        } else if (this.purgeAvailable) {
                                            Gdx.input.setCursorPosition((int)this.purgeCardX, Settings.HEIGHT - (int)this.purgeCardY);
                                            return;
                                        }
                                    }

                                    if (((AbstractCard)this.coloredCards.get(index)).hb.cX < 1400.0F * Settings.scale) {
                                        if (!this.relics.isEmpty()) {
                                            Gdx.input.setCursorPosition((int)((StoreRelic)this.relics.get(0)).relic.hb.cX, Settings.HEIGHT - (int)((StoreRelic)this.relics.get(0)).relic.hb.cY);
                                            return;
                                        }

                                        if (!this.potions.isEmpty()) {
                                            Gdx.input.setCursorPosition((int)((StorePotion)this.potions.get(0)).potion.hb.cX, Settings.HEIGHT - (int)((StorePotion)this.potions.get(0)).potion.hb.cY);
                                        }
                                    }

                                    Gdx.input.setCursorPosition((int)this.purgeCardX, Settings.HEIGHT - (int)this.purgeCardY);
                                }
                            } else {
                                ++index;
                                if (index > this.coloredCards.size() - 1) {
                                    --index;
                                }

                                Gdx.input.setCursorPosition((int)((AbstractCard)this.coloredCards.get(index)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.coloredCards.get(index)).hb.cY);
                            }
                        } else {
                            --index;
                            if (index < 0) {
                                index = 0;
                            }

                            Gdx.input.setCursorPosition((int)((AbstractCard)this.coloredCards.get(index)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.coloredCards.get(index)).hb.cY);
                        }
                        break;
                    case COLORLESS_CARD:
                        if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                            if (!CInputActionSet.right.isJustPressed() && !CInputActionSet.altRight.isJustPressed()) {
                                if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && !this.coloredCards.isEmpty()) {
                                    if (((AbstractCard)this.colorlessCards.get(index)).hb.cX < 550.0F * Settings.scale) {
                                        Gdx.input.setCursorPosition((int)((AbstractCard)this.coloredCards.get(0)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.coloredCards.get(0)).hb.cY);
                                    } else if (this.coloredCards.size() > 1) {
                                        Gdx.input.setCursorPosition((int)((AbstractCard)this.coloredCards.get(1)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.coloredCards.get(1)).hb.cY);
                                    } else {
                                        Gdx.input.setCursorPosition((int)((AbstractCard)this.coloredCards.get(0)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.coloredCards.get(0)).hb.cY);
                                    }
                                }
                            } else {
                                ++index;
                                if (index > this.colorlessCards.size() - 1) {
                                    if (!this.relics.isEmpty()) {
                                        Gdx.input.setCursorPosition((int)((StoreRelic)this.relics.get(0)).relic.hb.cX, Settings.HEIGHT - (int)((StoreRelic)this.relics.get(0)).relic.hb.cY);
                                    } else if (!this.potions.isEmpty()) {
                                        Gdx.input.setCursorPosition((int)((StorePotion)this.potions.get(0)).potion.hb.cX, Settings.HEIGHT - (int)((StorePotion)this.potions.get(0)).potion.hb.cY);
                                    } else if (this.purgeAvailable) {
                                        Gdx.input.setCursorPosition((int)this.purgeCardX, Settings.HEIGHT - (int)this.purgeCardY);
                                    } else {
                                        boolean var7 = false;
                                    }
                                } else {
                                    Gdx.input.setCursorPosition((int)((AbstractCard)this.colorlessCards.get(index)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.colorlessCards.get(index)).hb.cY);
                                }
                            }
                        } else {
                            --index;
                            if (index < 0) {
                                index = 0;
                            }

                            Gdx.input.setCursorPosition((int)((AbstractCard)this.colorlessCards.get(index)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.colorlessCards.get(index)).hb.cY);
                        }
                        break;
                    case RELIC:
                        if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                            if (!CInputActionSet.right.isJustPressed() && !CInputActionSet.altRight.isJustPressed()) {
                                if ((CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) && !this.potions.isEmpty()) {
                                    if (this.potions.size() - 1 >= index) {
                                        Gdx.input.setCursorPosition((int)((StorePotion)this.potions.get(index)).potion.hb.cX, Settings.HEIGHT - (int)((StorePotion)this.potions.get(index)).potion.hb.cY);
                                    } else {
                                        Gdx.input.setCursorPosition((int)((StorePotion)this.potions.get(0)).potion.hb.cX, Settings.HEIGHT - (int)((StorePotion)this.potions.get(0)).potion.hb.cY);
                                    }
                                } else if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && !this.coloredCards.isEmpty()) {
                                    if (this.coloredCards.size() > 3) {
                                        Gdx.input.setCursorPosition((int)((AbstractCard)this.coloredCards.get(2)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.coloredCards.get(2)).hb.cY);
                                    } else {
                                        Gdx.input.setCursorPosition((int)((AbstractCard)this.coloredCards.get(0)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.coloredCards.get(0)).hb.cY);
                                    }
                                }
                            } else {
                                ++index;
                                if (index > this.relics.size() - 1 && this.purgeAvailable) {
                                    Gdx.input.setCursorPosition((int)this.purgeCardX, Settings.HEIGHT - (int)this.purgeCardY);
                                } else if (index <= this.relics.size() - 1) {
                                    Gdx.input.setCursorPosition((int)((StoreRelic)this.relics.get(index)).relic.hb.cX, Settings.HEIGHT - (int)((StoreRelic)this.relics.get(index)).relic.hb.cY);
                                }
                            }
                        } else {
                            --index;
                            if (index < 0 && !this.colorlessCards.isEmpty()) {
                                Gdx.input.setCursorPosition((int)((AbstractCard)this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cY);
                            } else {
                                if (index < 0) {
                                    index = 0;
                                }

                                Gdx.input.setCursorPosition((int)((StoreRelic)this.relics.get(index)).relic.hb.cX, Settings.HEIGHT - (int)((StoreRelic)this.relics.get(index)).relic.hb.cY);
                            }
                        }
                        break;
                    case POTION:
                        if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                            if (!CInputActionSet.right.isJustPressed() && !CInputActionSet.altRight.isJustPressed()) {
                                if (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) {
                                    if (!this.relics.isEmpty()) {
                                        if (this.relics.size() - 1 >= index) {
                                            Gdx.input.setCursorPosition((int)((StoreRelic)this.relics.get(index)).relic.hb.cX, Settings.HEIGHT - (int)((StoreRelic)this.relics.get(index)).relic.hb.cY);
                                        } else {
                                            Gdx.input.setCursorPosition((int)((StoreRelic)this.relics.get(0)).relic.hb.cX, Settings.HEIGHT - (int)((StoreRelic)this.relics.get(0)).relic.hb.cY);
                                        }
                                    } else if (!this.coloredCards.isEmpty()) {
                                        if (this.coloredCards.size() > 3) {
                                            Gdx.input.setCursorPosition((int)((AbstractCard)this.coloredCards.get(2)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.coloredCards.get(2)).hb.cY);
                                        } else {
                                            Gdx.input.setCursorPosition((int)((AbstractCard)this.coloredCards.get(0)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.coloredCards.get(0)).hb.cY);
                                        }
                                    }
                                }
                            } else {
                                ++index;
                                if (index > this.potions.size() - 1 && this.purgeAvailable) {
                                    Gdx.input.setCursorPosition((int)this.purgeCardX, Settings.HEIGHT - (int)this.purgeCardY);
                                } else if (index <= this.potions.size() - 1) {
                                    Gdx.input.setCursorPosition((int)((StorePotion)this.potions.get(index)).potion.hb.cX, Settings.HEIGHT - (int)((StorePotion)this.potions.get(index)).potion.hb.cY);
                                }
                            }
                        } else {
                            --index;
                            if (index < 0 && !this.colorlessCards.isEmpty()) {
                                Gdx.input.setCursorPosition((int)((AbstractCard)this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cY);
                            } else {
                                if (index < 0) {
                                    index = 0;
                                }

                                Gdx.input.setCursorPosition((int)((StorePotion)this.potions.get(index)).potion.hb.cX, Settings.HEIGHT - (int)((StorePotion)this.potions.get(index)).potion.hb.cY);
                            }
                        }
                        break;
                    case PURGE:
                        if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                            if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && !this.coloredCards.isEmpty()) {
                                Gdx.input.setCursorPosition((int)((AbstractCard)this.coloredCards.get(this.coloredCards.size() - 1)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.coloredCards.get(this.coloredCards.size() - 1)).hb.cY);
                            }
                        } else if (!this.relics.isEmpty()) {
                            Gdx.input.setCursorPosition((int)((StoreRelic)this.relics.get(this.relics.size() - 1)).relic.hb.cX, Settings.HEIGHT - (int)((StoreRelic)this.relics.get(this.relics.size() - 1)).relic.hb.cY);
                        } else if (!this.potions.isEmpty()) {
                            Gdx.input.setCursorPosition((int)((StorePotion)this.potions.get(this.potions.size() - 1)).potion.hb.cX, Settings.HEIGHT - (int)((StorePotion)this.potions.get(this.potions.size() - 1)).potion.hb.cY);
                        } else if (this.colorlessCards.isEmpty()) {
                            Gdx.input.setCursorPosition((int)((AbstractCard)this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cY);
                        }
                }
            }

        }
    }

    private void updatePurgeCard() {
        this.purgeCardX = 1554.0F * Settings.xScale;
        this.purgeCardY = this.rugY + BOTTOM_ROW_Y;
        if (this.purgeAvailable) {
            float CARD_W = 110.0F * Settings.scale;
            float CARD_H = 150.0F * Settings.scale;
            if ((float)InputHelper.mX > this.purgeCardX - CARD_W && (float)InputHelper.mX < this.purgeCardX + CARD_W && (float)InputHelper.mY > this.purgeCardY - CARD_H && (float)InputHelper.mY < this.purgeCardY + CARD_H) {
                this.purgeHovered = true;
                this.moveHand(this.purgeCardX - AbstractCard.IMG_WIDTH / 2.0F, this.purgeCardY);
                this.somethingHovered = true;
                this.purgeCardScale = Settings.scale;
            } else {
                this.purgeHovered = false;
            }

            if (!this.purgeHovered) {
                this.purgeCardScale = MathHelper.cardScaleLerpSnap(this.purgeCardScale, 0.75F * Settings.scale);
            } else {
                if (InputHelper.justReleasedClickLeft || CInputActionSet.select.isJustPressed()) {
                    if (!Settings.isTouchScreen) {
                        CInputActionSet.select.unpress();
                        this.purchasePurge();
                    } else if (!this.touchPurge) {
                        if (AbstractDungeon.player.gold < actualPurgeCost) {
                            this.playCantBuySfx();
                            this.createSpeech(getCantBuyMsg());
                        } else {
                            this.confirmButton.hideInstantly();
                            this.confirmButton.show();
                            this.confirmButton.hb.clickStarted = false;
                            this.confirmButton.isDisabled = false;
                            this.touchPurge = true;
                        }
                    }
                }

                TipHelper.renderGenericTip((float)InputHelper.mX - 360.0F * Settings.scale, (float)InputHelper.mY - 70.0F * Settings.scale, LABEL[0], MSG[0] + PURGE_COST_RAMP + MSG[1]);
            }
        } else {
            this.purgeCardScale = MathHelper.cardScaleLerpSnap(this.purgeCardScale, 0.75F * Settings.scale);
        }

    }

    private void purchasePurge() {
        this.purgeHovered = false;
        if (AbstractDungeon.player.gold >= actualPurgeCost) {
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.SHOP;
            AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, NAMES[13], false, false, true, true);
        } else {
            this.playCantBuySfx();
            this.createSpeech(getCantBuyMsg());
        }

    }

    private void updateRelics() {
        Iterator i = this.relics.iterator();

        while(i.hasNext()) {
            StoreRelic r = (StoreRelic)i.next();
            if (Settings.isFourByThree) {
                r.update(this.rugY + 50.0F * Settings.yScale);
            } else {
                r.update(this.rugY);
            }

            if (r.isPurchased) {
                i.remove();
                break;
            }
        }

    }

    private void updatePotions() {
        Iterator i = this.potions.iterator();

        while(i.hasNext()) {
            StorePotion p = (StorePotion)i.next();
            if (Settings.isFourByThree) {
                p.update(this.rugY + 50.0F * Settings.scale);
            } else {
                p.update(this.rugY);
            }

            if (p.isPurchased) {
                i.remove();
                break;
            }
        }

    }

    public void createSpeech(String msg) {
        boolean isRight = MathUtils.randomBoolean();
        float x = MathUtils.random(660.0F, 1260.0F) * Settings.scale;
        float y = (float)Settings.HEIGHT - 380.0F * Settings.scale;
        this.speechBubble = new ShopSpeechBubble(x, y, SPEECH_DURATION, msg, isRight);
        float offset_x = 0.0F;
        if (isRight) {
            offset_x = SPEECH_TEXT_R_X;
        } else {
            offset_x = SPEECH_TEXT_L_X;
        }

        this.dialogTextEffect = new SpeechTextEffect(x + offset_x, y + SPEECH_TEXT_Y, SPEECH_DURATION, msg, DialogWord.AppearEffect.BUMP_IN);
    }

    private void updateSpeech() {
        if (this.speechBubble != null) {
            this.speechBubble.update();
            if (this.speechBubble.hb.hovered && this.speechBubble.duration > 0.3F) {
                this.speechBubble.duration = 0.3F;
                this.dialogTextEffect.duration = 0.3F;
            }

            if (this.speechBubble.isDone) {
                this.speechBubble = null;
            }
        }

        if (this.dialogTextEffect != null) {
            this.dialogTextEffect.update();
            if (this.dialogTextEffect.isDone) {
                this.dialogTextEffect = null;
            }
        }

        this.speechTimer -= Gdx.graphics.getDeltaTime();
        if (this.speechBubble == null && this.dialogTextEffect == null && this.speechTimer <= 0.0F) {
            this.speechTimer = MathUtils.random(MIN_IDLE_MSG_TIME, MAX_IDLE_MSG_TIME);
            if (!this.saidWelcome) {
                this.createSpeech(WELCOME_MSG);
                this.saidWelcome = true;
                this.welcomeSfx();
            } else {
                this.playMiscSfx();
                this.createSpeech(this.getIdleMsg());
            }
        }

    }

    private void welcomeSfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_3A");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_3B");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_3C");
        }

    }

    private void playMiscSfx() {
        int roll = MathUtils.random(5);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_MA");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_MB");
        } else if (roll == 2) {
            CardCrawlGame.sound.play("VO_MERCHANT_MC");
        } else if (roll == 3) {
            CardCrawlGame.sound.play("VO_MERCHANT_3A");
        } else if (roll == 4) {
            CardCrawlGame.sound.play("VO_MERCHANT_3B");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_3C");
        }

    }

    public void playBuySfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_KA");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_KB");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_KC");
        }

    }

    public void playCantBuySfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_2A");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_2B");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_2C");
        }

    }

    private String getIdleMsg() {
        return (String)this.idleMessages.get(MathUtils.random(this.idleMessages.size() - 1));
    }

    private void updateRug() {
        if (this.rugY != 0.0F) {
            this.rugY = MathUtils.lerp(this.rugY, (float)Settings.HEIGHT / 2.0F - 540.0F * Settings.yScale, Gdx.graphics.getDeltaTime() * RUG_SPEED);
            if (Math.abs(this.rugY - 0.0F) < 0.5F) {
                this.rugY = 0.0F;
            }
        }

    }

    private void updateHand() {
        if (this.handTimer == 0.0F) {
            if (this.handX != this.handTargetX) {
                this.handX = MathUtils.lerp(this.handX, this.handTargetX, Gdx.graphics.getDeltaTime() * HAND_SPEED);
            }

            if (this.handY != this.handTargetY) {
                if (this.handY > this.handTargetY) {
                    this.handY = MathUtils.lerp(this.handY, this.handTargetY, Gdx.graphics.getDeltaTime() * HAND_SPEED);
                } else {
                    this.handY = MathUtils.lerp(this.handY, this.handTargetY, Gdx.graphics.getDeltaTime() * HAND_SPEED / 4.0F);
                }
            }
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(rugImg, 0.0F, this.rugY, (float)Settings.WIDTH, (float)Settings.HEIGHT);
        this.renderCardsAndPrices(sb);
        this.renderRelics(sb);
        this.renderPotions(sb);
        this.renderPurge(sb);
        sb.draw(handImg, this.handX + this.f_effect.x, this.handY + this.f_effect.y, HAND_W, HAND_H);
        if (this.speechBubble != null) {
            this.speechBubble.render(sb);
        }

        if (this.dialogTextEffect != null) {
            this.dialogTextEffect.render(sb);
        }

        if (Settings.isTouchScreen) {
            this.confirmButton.render(sb);
        }

    }

    private void renderRelics(SpriteBatch sb) {
        Iterator var2 = this.relics.iterator();

        while(var2.hasNext()) {
            StoreRelic r = (StoreRelic)var2.next();
            r.render(sb);
        }

    }

    private void renderPotions(SpriteBatch sb) {
        Iterator var2 = this.potions.iterator();

        while(var2.hasNext()) {
            StorePotion p = (StorePotion)var2.next();
            p.render(sb);
        }

    }

    private void renderCardsAndPrices(SpriteBatch sb) {
        Iterator var2;
        AbstractCard c;
        Color color;
        for(var2 = this.coloredCards.iterator(); var2.hasNext(); FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(c.price), c.current_x + PRICE_TEXT_OFFSET_X, c.current_y + PRICE_TEXT_OFFSET_Y - (c.drawScale - 0.75F) * 200.0F * Settings.scale, color)) {
            c = (AbstractCard)var2.next();
            c.render(sb);
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.UI_GOLD, c.current_x + GOLD_IMG_OFFSET_X, c.current_y + GOLD_IMG_OFFSET_Y - (c.drawScale - 0.75F) * 200.0F * Settings.scale, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);
            color = Color.WHITE.cpy();
            if (c.price > AbstractDungeon.player.gold) {
                color = Color.SALMON.cpy();
            } else if (c.equals(this.saleTag.card)) {
                color = Color.SKY.cpy();
            }
        }

        for(var2 = this.colorlessCards.iterator(); var2.hasNext(); FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(c.price), c.current_x + PRICE_TEXT_OFFSET_X, c.current_y + PRICE_TEXT_OFFSET_Y - (c.drawScale - 0.75F) * 200.0F * Settings.scale, color)) {
            c = (AbstractCard)var2.next();
            c.render(sb);
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.UI_GOLD, c.current_x + GOLD_IMG_OFFSET_X, c.current_y + GOLD_IMG_OFFSET_Y - (c.drawScale - 0.75F) * 200.0F * Settings.scale, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);
            color = Color.WHITE.cpy();
            if (c.price > AbstractDungeon.player.gold) {
                color = Color.SALMON.cpy();
            } else if (c.equals(this.saleTag.card)) {
                color = Color.SKY.cpy();
            }
        }

        if (this.coloredCards.contains(this.saleTag.card)) {
            this.saleTag.render(sb);
        }

        if (this.colorlessCards.contains(this.saleTag.card)) {
            this.saleTag.render(sb);
        }

        var2 = this.coloredCards.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            c.renderCardTip(sb);
        }

        var2 = this.colorlessCards.iterator();

        while(var2.hasNext()) {
            c = (AbstractCard)var2.next();
            c.renderCardTip(sb);
        }

    }

    private void renderPurge(SpriteBatch sb) {
        sb.setColor(Settings.QUARTER_TRANSPARENT_BLACK_COLOR);
        TextureAtlas.AtlasRegion tmpImg = ImageMaster.CARD_SKILL_BG_SILHOUETTE;
        sb.draw(tmpImg, this.purgeCardX + 18.0F * Settings.scale + tmpImg.offsetX - (float)tmpImg.originalWidth / 2.0F, this.purgeCardY - 14.0F * Settings.scale + tmpImg.offsetY - (float)tmpImg.originalHeight / 2.0F, (float)tmpImg.originalWidth / 2.0F - tmpImg.offsetX, (float)tmpImg.originalHeight / 2.0F - tmpImg.offsetY, (float)tmpImg.packedWidth, (float)tmpImg.packedHeight, this.purgeCardScale, this.purgeCardScale, 0.0F);
        sb.setColor(Color.WHITE);
        if (this.purgeAvailable) {
            sb.draw(removeServiceImg, this.purgeCardX - 256.0F, this.purgeCardY - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, this.purgeCardScale, this.purgeCardScale, 0.0F, 0, 0, 512, 512, false, false);
            sb.draw(ImageMaster.UI_GOLD, this.purgeCardX + GOLD_IMG_OFFSET_X, this.purgeCardY + GOLD_IMG_OFFSET_Y - (this.purgeCardScale / Settings.scale - 0.75F) * 200.0F * Settings.scale, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);
            Color color = Color.WHITE;
            if (actualPurgeCost > AbstractDungeon.player.gold) {
                color = Color.SALMON;
            }

            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(actualPurgeCost), this.purgeCardX + PRICE_TEXT_OFFSET_X, this.purgeCardY + PRICE_TEXT_OFFSET_Y - (this.purgeCardScale / Settings.scale - 0.75F) * 200.0F * Settings.scale, color);
        } else {
            sb.draw(soldOutImg, this.purgeCardX - 256.0F, this.purgeCardY - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, this.purgeCardScale, this.purgeCardScale, 0.0F, 0, 0, 512, 512, false, false);
        }

    }

    static {
        tutorialStrings = CardCrawlGame.languagePack.getTutorialString("Shop Tip");
        MSG = tutorialStrings.TEXT;
        LABEL = tutorialStrings.LABEL;
        characterStrings = CardCrawlGame.languagePack.getCharacterString("Shop Screen");
        NAMES = characterStrings.NAMES;
        TEXT = characterStrings.TEXT;
        rugImg = null;
        removeServiceImg = null;
        soldOutImg = null;
        handImg = null;
        DRAW_START_X = (float)Settings.WIDTH * 0.16F;
        TOP_ROW_Y = 760.0F * Settings.yScale;
        BOTTOM_ROW_Y = 337.0F * Settings.yScale;
        SPEECH_TEXT_R_X = 164.0F * Settings.scale;
        SPEECH_TEXT_L_X = -166.0F * Settings.scale;
        SPEECH_TEXT_Y = 126.0F * Settings.scale;
        WELCOME_MSG = NAMES[0];
        purgeCost = 75;
        actualPurgeCost = 75;
        GOLD_IMG_WIDTH = (float)ImageMaster.UI_GOLD.getWidth() * Settings.scale;
        GOLD_IMG_OFFSET_X = -50.0F * Settings.scale;
        GOLD_IMG_OFFSET_Y = -215.0F * Settings.scale;
        PRICE_TEXT_OFFSET_X = 16.0F * Settings.scale;
        PRICE_TEXT_OFFSET_Y = -180.0F * Settings.scale;
    }

    private static enum StoreSelectionType {
        RELIC,
        COLOR_CARD,
        COLORLESS_CARD,
        POTION,
        PURGE;

        private StoreSelectionType() {
        }
    }
}
*/