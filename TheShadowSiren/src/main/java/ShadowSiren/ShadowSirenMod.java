package ShadowSiren;

import ShadowSiren.characters.Vivian;
import ShadowSiren.icons.DarkIcon;
import ShadowSiren.icons.ElectricIcon;
import ShadowSiren.icons.FireIcon;
import ShadowSiren.icons.IceIcon;
import ShadowSiren.potions.ChargePotion;
import ShadowSiren.potions.CleansingPotion;
import ShadowSiren.potions.FreezePotion;
import ShadowSiren.potions.VigorPotion;
import ShadowSiren.relics.*;
import ShadowSiren.util.IDCheckDontTouchPls;
import ShadowSiren.util.TextureLoader;
import ShadowSiren.variables.*;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper;
import com.evacipated.cardcrawl.mod.widepotions.WidePotionsMod;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

//Done: DON'T MASS RENAME/REFACTOR
//Done: DON'T MASS RENAME/REFACTOR
//Done: DON'T MASS RENAME/REFACTOR
//Done: DON'T MASS RENAME/REFACTOR
// Please don't just mass replace "theDefault" with "yourMod" everywhere.
// It'll be a bigger pain for you. You only need to replace it in 3 places.
// I comment those places below, under the place where you set your ID.

//Done: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "theDefault:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "theDefault". You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */

@SpireInitializer
public class ShadowSirenMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        StartActSubscriber {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(ShadowSirenMod.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button

    public static final String CARD_BATTLE_TALK_SETTING = "enableCardBattleTalk";
    public static boolean enableCardBattleTalkEffect = true;

    public static final String CARD_BATTLE_TALK_PROBABILITY_SETTING = "cardTalkProbability";
    private static final int BASE_CARD_TALK_PROBABILITY = 0;
    public static int cardTalkProbability = BASE_CARD_TALK_PROBABILITY; //Out of 100

    public static final String DAMAGED_BATTLE_TALK_SETTING = "enableDamagedBattleTalk";
    public static boolean enableDamagedBattleTalkEffect = true;

    public static final String DAMAGED_BATTLE_TALK_PROBABILITY_SETTING = "damagedTalkProbability";
    private static final int BASE_DAMAGED_TALK_PROBABILITY = 0;
    public static int damagedTalkProbability = BASE_DAMAGED_TALK_PROBABILITY; //Out of 100

    public static final String PRE_BATTLE_TALK_SETTING = "enablePreBattleTalk";
    public static boolean enablePreBattleTalkEffect = true;

    public static final String PRE_BATTLE_TALK_PROBABILITY_SETTING = "preTalkProbability";
    private static final int BASE_PRE_TALK_PROBABILITY = 0;
    public static int preTalkProbability = BASE_PRE_TALK_PROBABILITY; //Out of 100


    //This is for the in-game mod settings panel.
    private static final String MODNAME = "The Shadow Siren";
    private static final String AUTHOR = "Mistress Autumn";
    private static final String DESCRIPTION = "Adds Vivian from Paper Mario: The Thousand-Year Door!";
    
    // =============== INPUT TEXTURE LOCATION =================
    
    // Colors (RGB)
    // Character Color
    //public static final Color DEFAULT_GRAY = CardHelper.getColor(64.0f, 70.0f, 70.0f);
    public static final Color VOODOO = CardHelper.getColor(83.0f, 52.0f, 85.0f);
    //public static final Color SHADOW = CardHelper.getColor(51.0f, 41.0f, 47.0f);
    //public static final Color VOID = CardHelper.getColor(149.0f, 147.0f, 150.0f);
    
    // Potion Colors in RGB
    public static final Color VIGOR_POTION_LIQUID = CardHelper.getColor(227, 91, 0); // Orange/Red
    public static final Color VIGOR_POTION_HYBRID = CardHelper.getColor(255, 102, 0); // Lighter Orange/Red
    public static final Color VIGOR_POTION_SPOTS = new Color(0.0f, 0.0f, 0.0f, 0.0f); // Invisible

    public static final Color BURN_POTION_LIQUID = CardHelper.getColor(168, 56, 50); // Dark Red
    public static final Color BURN_POTION_HYBRID = CardHelper.getColor(207, 118, 35); // Lighter Orange
    public static final Color BURN_POTION_SPOTS = CardHelper.getColor(255, 102, 0); // Lighter Orange/Red

    public static final Color CHILL_POTION_LIQUID = CardHelper.getColor(37, 116, 176); // Dark Blue
    public static final Color CHILL_POTION_HYBRID = CardHelper.getColor(0, 145, 255); // Lighter Blue
    public static final Color CHILL_POTION_SPOTS = CardHelper.getColor(112, 193, 255); // Light Blue

    public static final Color SOFT_POTION_LIQUID = CardHelper.getColor(252, 247, 227); // Off White
    public static final Color SOFT_POTION_HYBRID = CardHelper.getColor(255, 255, 255); // White
    public static final Color SOFT_POTION_SPOTS = CardHelper.getColor(255, 255, 200); // Offer White

    public static final Color CHARGE_POTION_LIQUID = CardHelper.getColor(207, 190, 0); // Darker Yellow
    public static final Color CHARGE_POTION_HYBRID = CardHelper.getColor(255, 255, 0); // Yellow AF
    public static final Color CHARGE_POTION_SPOTS = CardHelper.getColor(255, 255, 0); // Yellow AF

    public static final Color ELECTRIC_POTION_LIQUID = CardHelper.getColor(207, 190, 0); // Darker Yellow
    public static final Color ELECTRIC_POTION_HYBRID = CardHelper.getColor(255, 255, 0); // Yellow AF
    public static final Color ELECTRIC_POTION_SPOTS = new Color(0.0f, 0.0f, 0.0f, 0.0f); // Invisible

    public static final Color FREEZE_POTION_LIQUID = CardHelper.getColor(161, 255, 253); // Light Cyan
    public static final Color FREEZE_POTION_HYBRID = CardHelper.getColor(189, 255, 254); // Lighter Cyan
    public static final Color FREEZE_POTION_SPOTS = null; // Cant pass a spots or it crashes

    public static final Color CLEANSING_POTION_LIQUID = CardHelper.getColor(71, 255, 61); // Green
    public static final Color CLEANSING_POTION_HYBRID = CardHelper.getColor(192, 255, 189); // Light Green
    public static final Color CLEANSING_POTION_SPOTS = CardHelper.getColor(0, 255, 0); // Green AF

    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
  
    // Card backgrounds - The actual rectangular card.
    public static final String ATTACK_VOODOO = "ShadowSirenResources/images/512/bg_attack_shadow2.png"; //
    public static final String ATTACK_SHADOW = "ShadowSirenResources/images/512/bg_attack_shadow3.png"; //
    public static final String ATTACK_VOID = "ShadowSirenResources/images/512/bg_attack_abyss.png"; //
    public static final String ATTACK_SMOKE = "ShadowSirenResources/images/512/bg_attack_void2.png"; //
    public static final String ATTACK_HUGE = "ShadowSirenResources/images/512/bg_attack_huge.png"; //
    public static final String ATTACK_HYPER = "ShadowSirenResources/images/512/bg_attack_hyper.png"; //
    public static final String ATTACK_PRISMATIC = "ShadowSirenResources/images/512/bg_attack_prismatic.png"; //
    public static final String ATTACK_STAR = "ShadowSirenResources/images/512/bg_attack_space4.png"; //
    public static final String SKILL_VOODOO = "ShadowSirenResources/images/512/bg_skill_shadow2.png"; //
    public static final String SKILL_SHADOW = "ShadowSirenResources/images/512/bg_skill_shadow3.png"; //
    public static final String SKILL_VOID = "ShadowSirenResources/images/512/bg_skill_abyss.png"; //
    public static final String SKILL_SMOKE = "ShadowSirenResources/images/512/bg_skill_void2.png"; //
    public static final String SKILL_HUGE = "ShadowSirenResources/images/512/bg_skill_huge.png"; //
    public static final String SKILL_HYPER = "ShadowSirenResources/images/512/bg_skill_hyper.png"; //
    public static final String SKILL_PRISMATIC = "ShadowSirenResources/images/512/bg_skill_prismatic.png"; //
    public static final String SKILL_STAR = "ShadowSirenResources/images/512/bg_skill_space4.png"; //
    public static final String POWER_VOODOO = "ShadowSirenResources/images/512/bg_power_shadow2.png"; //
    public static final String POWER_SHADOW = "ShadowSirenResources/images/512/bg_power_shadow3.png"; //
    public static final String POWER_VOID = "ShadowSirenResources/images/512/bg_power_abyss.png"; //
    public static final String POWER_SMOKE = "ShadowSirenResources/images/512/bg_power_void2.png"; //
    public static final String POWER_HUGE = "ShadowSirenResources/images/512/bg_power_huge.png"; //
    public static final String POWER_HYPER = "ShadowSirenResources/images/512/bg_power_hyper.png"; //
    public static final String POWER_PRISMATIC = "ShadowSirenResources/images/512/bg_power_prismatic.png"; //
    public static final String POWER_STAR = "ShadowSirenResources/images/512/bg_power_space4.png"; //

    //private static final String ENERGY_ORB_WHITE_ICE = "ShadowSirenResources/images/512/card_orb.png"; //
    //private static final String CARD_ENERGY_ORB = "ShadowSirenResources/images/512/card_small_orb2.png"; //
    //private static final String ENERGY_ORB_WHITE_ICE = "ShadowSirenResources/images/512/card_orb_star.png"; //
    //private static final String CARD_ENERGY_ORB = "ShadowSirenResources/images/512/card_small_orb_star.png"; //
    //public static final String ENERGY_ORB_VOODOO = "ShadowSirenResources/images/512/card_orb_voodoo.png"; //
    //public static final String ENERGY_ORB_SHADOW = "ShadowSirenResources/images/512/card_orb_shadow.png"; //
    //public static final String ENERGY_ORB_VOID = "ShadowSirenResources/images/512/card_orb_void.png"; //
    public static final String ENERGY_ORB_FLOWER = "ShadowSirenResources/images/512/card_orb_flower2.png"; //

    public static final String CARD_ENERGY_ORB = "ShadowSirenResources/images/512/card_small_orb_voodoo.png"; //
    public static final String CARD_ENERGY_ORB_FLOWER = "ShadowSirenResources/images/512/card_small_flower.png"; //

    public static final String ATTACK_VOODOO_PORTRAIT = "ShadowSirenResources/images/1024/bg_attack_shadow2.png"; //
    public static final String ATTACK_SHADOW_PORTRAIT = "ShadowSirenResources/images/1024/bg_attack_shadow3.png"; //
    public static final String ATTACK_VOID_PORTRAIT = "ShadowSirenResources/images/1024/bg_attack_abyss.png"; //
    public static final String ATTACK_SMOKE_PORTRAIT = "ShadowSirenResources/images/1024/bg_attack_void2.png"; //
    public static final String ATTACK_HUGE_PORTRAIT = "ShadowSirenResources/images/1024/bg_attack_huge.png"; //
    public static final String ATTACK_HYPER_PORTRAIT = "ShadowSirenResources/images/1024/bg_attack_hyper.png"; //
    public static final String ATTACK_PRISMATIC_PORTRAIT = "ShadowSirenResources/images/1024/bg_attack_prismatic.png"; //
    public static final String ATTACK_STAR_PORTRAIT = "ShadowSirenResources/images/1024/bg_attack_space4.png"; //
    public static final String SKILL_VOODOO_PORTRAIT = "ShadowSirenResources/images/1024/bg_skill_shadow2.png"; //
    public static final String SKILL_SHADOW_PORTRAIT = "ShadowSirenResources/images/1024/bg_skill_shadow3.png"; //
    public static final String SKILL_VOID_PORTRAIT = "ShadowSirenResources/images/1024/bg_skill_abyss.png"; //
    public static final String SKILL_SMOKE_PORTRAIT = "ShadowSirenResources/images/1024/bg_skill_void2.png"; //
    public static final String SKILL_HUGE_PORTRAIT = "ShadowSirenResources/images/1024/bg_skill_huge.png"; //
    public static final String SKILL_HYPER_PORTRAIT = "ShadowSirenResources/images/1024/bg_skill_hyper.png"; //
    public static final String SKILL_PRISMATIC_PORTRAIT = "ShadowSirenResources/images/1024/bg_skill_prismatic.png"; //
    public static final String SKILL_STAR_PORTRAIT = "ShadowSirenResources/images/1024/bg_skill_space4.png"; //
    public static final String POWER_VOODOO_PORTRAIT = "ShadowSirenResources/images/1024/bg_power_shadow2.png"; //
    public static final String POWER_SHADOW_PORTRAIT = "ShadowSirenResources/images/1024/bg_power_shadow3.png"; //
    public static final String POWER_VOID_PORTRAIT = "ShadowSirenResources/images/1024/bg_power_abyss.png"; //
    public static final String POWER_SMOKE_PORTRAIT = "ShadowSirenResources/images/1024/bg_power_void2.png"; //
    public static final String POWER_HUGE_PORTRAIT = "ShadowSirenResources/images/1024/bg_power_huge.png"; //
    public static final String POWER_HYPER_PORTRAIT = "ShadowSirenResources/images/1024/bg_power_hyper.png"; //
    public static final String POWER_PRISMATIC_PORTRAIT = "ShadowSirenResources/images/1024/bg_power_prismatic.png"; //
    public static final String POWER_STAR_PORTRAIT = "ShadowSirenResources/images/1024/bg_power_space4.png"; //

    //public static final String ENERGY_ORB_VOODOO_PORTRAIT = "ShadowSirenResources/images/1024/card_orb_voodoo.png"; //
    //public static final String ENERGY_ORB_SHADOW_PORTRAIT = "ShadowSirenResources/images/1024/card_orb_shadow.png"; //
    //public static final String ENERGY_ORB_VOID_PORTRAIT = "ShadowSirenResources/images/1024/card_orb_void.png"; //
    public static final String ENERGY_ORB_FLOWER_PORTRAIT = "ShadowSirenResources/images/1024/card_orb_flower3.png"; //
    
    // Character assets
    //private static final String THE_DEFAULT_BUTTON = "ShadowSirenResources/images/charSelect/DefaultCharacterButton.png";
    private static final String VIVIAN_BUTTON = "ShadowSirenResources/images/charSelect/VivianButton4.png"; //
    //private static final String THE_DEFAULT_PORTRAIT = "ShadowSirenResources/images/charSelect/DefaultCharacterPortraitBG.png";
    private static final String VIVIAN_BG = "ShadowSirenResources/images/charSelect/VivianBG2.png";
    public static final String VIVIAN_SHOULDER_1 = "ShadowSirenResources/images/char/vivian/shoulder.png"; //
    public static final String VIVIAN_SHOULDER_2 = "ShadowSirenResources/images/char/vivian/shoulder2.png";
    public static final String VIVIAN_CORPSE = "ShadowSirenResources/images/char/vivian/VivianHat.png"; //
    
    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "ShadowSirenResources/images/Badge.png";
    
    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }
    
    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }
    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public ShadowSirenMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);
        
      /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */
      
        setModID("ShadowSiren");
        // cool
        // Done: NOW READ THIS!!!!!!!!!!!!!!!:
        
        // 1. Go to your resources folder in the project panel, and refactor> rename theDefaultResources to
        // yourModIDResources.
        
        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project)
        // replace all instances of theDefault with yourModID.
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.
        
        // 3. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than theDefault. They get loaded before getID is a thing.
        
        logger.info("Done subscribing");
        
        logger.info("Creating the color " + Vivian.Enums.VOODOO_CARD_COLOR.toString());
        
        BaseMod.addColor(Vivian.Enums.VOODOO_CARD_COLOR, VOODOO, VOODOO, VOODOO,
                VOODOO, VOODOO, VOODOO, VOODOO,
                ATTACK_VOODOO, SKILL_VOODOO, POWER_VOODOO, ENERGY_ORB_FLOWER,
                ATTACK_VOODOO_PORTRAIT, SKILL_VOODOO_PORTRAIT, POWER_VOODOO_PORTRAIT,
                ENERGY_ORB_FLOWER_PORTRAIT, CARD_ENERGY_ORB_FLOWER);
        
        logger.info("Done creating the color");
        
        
        //logger.info("Adding mod settings");

        //logger.info("Done adding mod settings");
        
    }
    
    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP
    
    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = ShadowSirenMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO
    
    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH
    
    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = ShadowSirenMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = ShadowSirenMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO
    
    // ====== YOU CAN EDIT AGAIN ======
    
    
    public static void initialize() {
        logger.info("========================= Initializing Vivian. =========================");
        ShadowSirenMod shadowSirenMod = new ShadowSirenMod();
        logger.info("========================= /Vivian, The Shadow Siren, Initialized/ =========================");
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================
    
    
    // =============== LOAD THE CHARACTER =================
    
    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + Vivian.Enums.THE_SHADOW_SIREN.toString());
        
        BaseMod.addCharacter(new Vivian("The Shadow Siren", Vivian.Enums.THE_SHADOW_SIREN),
                VIVIAN_BUTTON, VIVIAN_BG, Vivian.Enums.THE_SHADOW_SIREN);
        
        receiveEditPotions();
        logger.info("Added " + Vivian.Enums.THE_SHADOW_SIREN.toString());
    }
    
    // =============== /LOAD THE CHARACTER/ =================
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        //Add WidePotion Compatibility
        if (Loader.isModLoaded("widepotions")) {

            logger.info("Wide Potions: Detected. Shenanigans: Engaged.");

            //Simple Potions

            //WidePotionsMod.whitelistSimplePotion(BurnPotion.POTION_ID);
            WidePotionsMod.whitelistSimplePotion(ChargePotion.POTION_ID);
            //WidePotionsMod.whitelistSimplePotion(ElectricPotion.POTION_ID);
            WidePotionsMod.whitelistSimplePotion(FreezePotion.POTION_ID);
            //WidePotionsMod.whitelistSimplePotion(SoftPotion.POTION_ID);
            WidePotionsMod.whitelistSimplePotion(VigorPotion.POTION_ID);

            //Complex Potions

            //WidePotionsMod.whitelistComplexPotion(MyOtherPotion.POTION_ID, new WideMyOtherPotion());
        }

        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        //Get the longest slider text for positioning
        ArrayList<String> labelStrings = new ArrayList<>();
        labelStrings.add(CardCrawlGame.languagePack.getUIString(ShadowSirenMod.makeID("ModConfigBattleTalkButton")).TEXT[0]);
        labelStrings.add(CardCrawlGame.languagePack.getUIString(ShadowSirenMod.makeID("ModConfigDamagedTalkButton")).TEXT[0]);
        labelStrings.add(CardCrawlGame.languagePack.getUIString(ShadowSirenMod.makeID("ModConfigPreBattleTalkButton")).TEXT[0]);
        float sliderOffset = getSliderPosition(labelStrings);
        labelStrings.clear();
        float currentYposition = 740f;
        float spacingY = 55f;
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        // =============== SAVABLES =================
        //logger.info("Preparing CustomSavables");

        // =============== /SAVABLES/ =================

        // =============== EVENTS =================
        
        // This event will be exclusive to the City (act 2). If you want an event that's present at any
        // part of the game, simply don't include the dungeon ID
        // If you want to have a character-specific event, look at slimebound (CityRemoveEventPatch).
        // Essentially, you need to patch the game and say "if a player is not playing my character class, remove the event from the pool"

        //No events have been made yet, dont add the default event, that would be silly
        //BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);
        
        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }

    //Get the longest text so all sliders are centered
    private float getSliderPosition (ArrayList<String> stringsToCompare) {
        float longest = 0;
        for (String s : stringsToCompare) {
            longest = Math.max(longest, FontHelper.getWidth(FontHelper.charDescFont, s, 1f / Settings.scale));
        }
        return longest + 60f;
    }
    
    // =============== / POST-INITIALIZE/ =================
    
    
    // ================ ADD POTIONS ===================
    
    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");
        
        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_DEFAULT".
        // Remember, you can press ctrl+P inside parentheses like addPotions)

        //BaseMod.addPotion(BurnPotion.class, BURN_POTION_LIQUID, BURN_POTION_HYBRID, BURN_POTION_SPOTS, BurnPotion.POTION_ID, Vivian.Enums.THE_SHADOW_SIREN);
        BaseMod.addPotion(ChargePotion.class, CHARGE_POTION_LIQUID, CHARGE_POTION_HYBRID, CHARGE_POTION_SPOTS, ChargePotion.POTION_ID, Vivian.Enums.THE_SHADOW_SIREN);
        //BaseMod.addPotion(ChillPotion.class, CHILL_POTION_LIQUID, CHILL_POTION_HYBRID, CHILL_POTION_SPOTS, ChillPotion.POTION_ID, Vivian.Enums.THE_SHADOW_SIREN);
        BaseMod.addPotion(CleansingPotion.class, CLEANSING_POTION_LIQUID, CLEANSING_POTION_HYBRID, CLEANSING_POTION_SPOTS, CleansingPotion.POTION_ID, Vivian.Enums.THE_SHADOW_SIREN);
        //BaseMod.addPotion(ElectricPotion.class, ELECTRIC_POTION_LIQUID, ELECTRIC_POTION_HYBRID, ELECTRIC_POTION_SPOTS, ElectricPotion.POTION_ID, Vivian.Enums.THE_SHADOW_SIREN);
        BaseMod.addPotion(FreezePotion.class, FREEZE_POTION_LIQUID, FREEZE_POTION_HYBRID, FREEZE_POTION_SPOTS, FreezePotion.POTION_ID, Vivian.Enums.THE_SHADOW_SIREN);
        //BaseMod.addPotion(SoftPotion.class, SOFT_POTION_LIQUID, SOFT_POTION_HYBRID, SOFT_POTION_SPOTS, SoftPotion.POTION_ID, Vivian.Enums.THE_SHADOW_SIREN);
        BaseMod.addPotion(VigorPotion.class, VIGOR_POTION_LIQUID, VIGOR_POTION_HYBRID, VIGOR_POTION_SPOTS, VigorPotion.POTION_ID, Vivian.Enums.THE_SHADOW_SIREN);

        logger.info("Done editing potions");
    }
    
    // ================ /ADD POTIONS/ ===================
    
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // Take a look at https://github.com/daviscook477/BaseMod/wiki/AutoAdd
        // as well as
        // https://github.com/kiooeht/Bard/blob/e023c4089cc347c60331c78c6415f489d19b6eb9/src/main/java/com/evacipated/cardcrawl/mod/bard/BardMod.java#L319
        // for reference as to how to turn this into an "Auto-Add" rather than having to list every relic individually.
        // Of note is that the bard mod uses it's own custom relic class (not dissimilar to our AbstractDefaultCard class for cards) that adds the 'color' field,
        // in order to automatically differentiate which pool to add the relic too.

        //BaseMod.addRelicToCustomPool(new Chronometer(), Vivian.Enums.VOODOO_CARD_COLOR);
        //UnlockTracker.markRelicAsSeen(Chronometer.ID);

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new BlackKey(), Vivian.Enums.VOODOO_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new BooSheet(), Vivian.Enums.VOODOO_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new BottledStar(), Vivian.Enums.VOODOO_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new CourageShell(), Vivian.Enums.VOODOO_CARD_COLOR);
        //BaseMod.addRelicToCustomPool(new DataCollector(), Vivian.Enums.VOODOO_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new EarthQuake(), Vivian.Enums.VOODOO_CARD_COLOR);
        //BaseMod.addRelicToCustomPool(new ElectroPop(), Vivian.Enums.VOODOO_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new FireFlower(), Vivian.Enums.VOODOO_CARD_COLOR);
        //BaseMod.addRelicToCustomPool(new FirePop(), Vivian.Enums.VOODOO_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new GoldenLeaf(), Vivian.Enums.VOODOO_CARD_COLOR);
        //BaseMod.addRelicToCustomPool(new HotSauce(), Vivian.Enums.VOODOO_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new IceStorm(), Vivian.Enums.VOODOO_CARD_COLOR);
        //BaseMod.addRelicToCustomPool(new IciclePop(), Vivian.Enums.VOODOO_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new LetterP(), Vivian.Enums.VOODOO_CARD_COLOR);
        //BaseMod.addRelicToCustomPool(new Mystery(), Vivian.Enums.VOODOO_CARD_COLOR);
        //BaseMod.addRelicToCustomPool(new PointSwap(), Vivian.Enums.VOODOO_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new POWBlock(), Vivian.Enums.VOODOO_CARD_COLOR);
        //BaseMod.addRelicToCustomPool(new PowerPunch(), Vivian.Enums.VOODOO_CARD_COLOR);
        //BaseMod.addRelicToCustomPool(new RepelCape(), Vivian.Enums.VOODOO_CARD_COLOR);
        //BaseMod.addRelicToCustomPool(new RuinPowder(), Vivian.Enums.VOODOO_CARD_COLOR);
        //BaseMod.addRelicToCustomPool(new SpitePowder(), Vivian.Enums.VOODOO_CARD_COLOR);
        //BaseMod.addRelicToCustomPool(new StarPiece(), Vivian.Enums.VOODOO_CARD_COLOR);
        //BaseMod.addRelicToCustomPool(new StopWatch(), Vivian.Enums.VOODOO_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new SuperSheet(), Vivian.Enums.VOODOO_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new TastyTonic(), Vivian.Enums.VOODOO_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new UpArrow(), Vivian.Enums.VOODOO_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new VoltShroom(), Vivian.Enums.VOODOO_CARD_COLOR);

        // This adds a relic to the Shared pool. Every character can find this relic.
        //BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);

        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        UnlockTracker.markRelicAsSeen(BlackKey.ID);
        UnlockTracker.markRelicAsSeen(BooSheet.ID);
        UnlockTracker.markRelicAsSeen(BottledStar.ID);
        UnlockTracker.markRelicAsSeen(CourageShell.ID);
        //UnlockTracker.markRelicAsSeen(DataCollector.ID);
        UnlockTracker.markRelicAsSeen(EarthQuake.ID);
        //UnlockTracker.markRelicAsSeen(ElectroPop.ID);
        UnlockTracker.markRelicAsSeen(FireFlower.ID);
        //UnlockTracker.markRelicAsSeen(FirePop.ID);
        UnlockTracker.markRelicAsSeen(GoldenLeaf.ID);
        //UnlockTracker.markRelicAsSeen(HotSauce.ID);
        UnlockTracker.markRelicAsSeen(IceStorm.ID);
        //UnlockTracker.markRelicAsSeen(IciclePop.ID);
        UnlockTracker.markRelicAsSeen(LetterP.ID);
        //UnlockTracker.markRelicAsSeen(Mystery.ID);
        //UnlockTracker.markRelicAsSeen(PointSwap.ID);
        UnlockTracker.markRelicAsSeen(POWBlock.ID);
        //UnlockTracker.markRelicAsSeen(PowerPunch.ID);
        //UnlockTracker.markRelicAsSeen(RepelCape.ID);
        //UnlockTracker.markRelicAsSeen(RuinPowder.ID);
        UnlockTracker.markRelicAsSeen(SpitePowder.ID);
        //UnlockTracker.markRelicAsSeen(StarPiece.ID);
        UnlockTracker.markRelicAsSeen(StopWatch.ID);
        UnlockTracker.markRelicAsSeen(SuperSheet.ID);
        UnlockTracker.markRelicAsSeen(TastyTonic.ID);
        UnlockTracker.markRelicAsSeen(UpArrow.ID);
        UnlockTracker.markRelicAsSeen(VoltShroom.ID);
        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================
    
    @Override
    public void receiveEditCards() {
        //Add icons
        logger.info("Adding Icons");
        CustomIconHelper.addCustomIcon(FireIcon.get());
        CustomIconHelper.addCustomIcon(IceIcon.get());
        CustomIconHelper.addCustomIcon(ElectricIcon.get());
        CustomIconHelper.addCustomIcon(DarkIcon.get());

        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variables");
        // Add the Custom Dynamic variables
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new SecondMagicNumber());
        BaseMod.addDynamicVariable(new ThirdMagicNumber());
        BaseMod.addDynamicVariable(new UsesVariable());
        BaseMod.addDynamicVariable(new DefaultInvertedNumber());
        
        logger.info("Adding cards");
        // Add the cards
        // Don't delete these default cards yet. You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.

        // This method automatically adds any cards inside the cards package, found under yourModName.cards.
        // For more specific info, including how to exclude classes from being added:
        // https://github.com/daviscook477/BaseMod/wiki/AutoAdd

        // The ID for this function isn't actually your modid as used for prefixes/by the getModID() method.
        // It's the mod id you give MTS in ModTheSpire.json - by default your artifact ID in your pom.xml

        new AutoAdd("ShadowSiren")
            .packageFilter("ShadowSiren.cards")
            .setDefaultSeen(true)
            .cards();

        // .setDefaultSeen(true) unlocks the cards
        // This is so that they are all "seen" in the library,
        // for people who like to look at the card list before playing your mod

        logger.info("Done adding cards!");
    }
    
    // ================ /ADD CARDS/ ===================


    // ================ LOAD THE LOCALIZATION ===================

    private String loadLocalizationIfAvailable(String fileName) {
        if (!Gdx.files.internal(getModID() + "Resources/localization/" + Settings.language.toString().toLowerCase()+ "/" + fileName).exists()) {
            logger.info("Language: " + Settings.language.toString().toLowerCase() + ", not currently supported for " +fileName+".");
            return "eng" + "/" + fileName;
        } else {
            logger.info("Loaded Language: "+ Settings.language.toString().toLowerCase() + ", for "+fileName+".");
            return Settings.language.toString().toLowerCase() + "/" + fileName;
        }
    }

    // ================ /LOAD THE LOCALIZATION/ ===================

    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        
        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("ShadowSiren-Card-Strings.json"));

        // ChatterStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("ShadowSiren-Chatter-Strings.json"));

        // DamageModStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("ShadowSiren-DamageModifier-Strings.json"));
        
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("ShadowSiren-Power-Strings.json"));
        
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("ShadowSiren-Relic-Strings.json"));
        
        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("ShadowSiren-Event-Strings.json"));
        
        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("ShadowSiren-Potion-Strings.json"));
        
        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("ShadowSiren-Character-Strings.json"));
        
        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("ShadowSiren-Orb-Strings.json"));

        // UIStrings
        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("ShadowSiren-UI-Strings.json"));

        // Stance Strings
        BaseMod.loadCustomStringsFile(StanceStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("ShadowSiren-Stance-Strings.json"));

        logger.info("Done editing strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================
    
    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword
        
        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID()+"Resources/localization/"+loadLocalizationIfAvailable("ShadowSiren-Keyword-Strings.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    @Override
    public void receiveStartAct() {
//        if (AbstractDungeon.actNum == 1) {
//            StarBarManager.resetVars();
//        } else if (AbstractDungeon.actNum <= 4) {
//            StarBarManager.modifyMaxAmount(2);
//        }
    }
}
