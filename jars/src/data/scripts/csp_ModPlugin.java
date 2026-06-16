package data.scripts;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.impl.hullmods.ShardSpawner;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import data.scripts.campaign.csp_DerelictsSpawnScript;
import data.scripts.campaign.fleets.LGDragoon;


public class csp_ModPlugin extends BaseModPlugin {
    public static boolean hasGraphicsLib;
    public static boolean haveGettag;
    public static final String MEMKEY_VERSION = "$Dragoon_version";
    public static final String MEMKEY_SPECIAL_FLEETS_INITIALIZED = "$DragoonSpecial";

    @Override
    public void onApplicationLoad() {
        hasGraphicsLib = Global.getSettings().getModManager().isModEnabled("shaderLib");

        ShardSpawner.ShardTypeVariants fighters = com.fs.starfarer.api.impl.hullmods.ShardSpawner.variantData.get(ShipAPI.HullSize.FIGHTER);
        fighters.get(ShardSpawner.ShardType.GENERAL).add("csp_bravais_attack_wing", 2f);
        fighters.get(ShardSpawner.ShardType.ANTI_ARMOR).add("csp_bravais_attack_wing", 2f);
        fighters.get(ShardSpawner.ShardType.ANTI_SHIELD).add("csp_bravais_shieldbreaker_wing", 1f);
        fighters.get(ShardSpawner.ShardType.POINT_DEFENSE).add("csp_bravais_shockmaster_wing", 1f);
        ShardSpawner.ShardTypeVariants medium = com.fs.starfarer.api.impl.hullmods.ShardSpawner.variantData.get(ShipAPI.HullSize.DESTROYER);
        medium.get(ShardSpawner.ShardType.GENERAL).add("csp_cutlet_attack", 2f);
        medium.get(ShardSpawner.ShardType.GENERAL).add("facet_Attack2");
        medium.get(ShardSpawner.ShardType.GENERAL).add("csp_cutlet_attack2", 2f);
        medium.get(ShardSpawner.ShardType.ANTI_ARMOR).add("facet_Armorbreaker");
        medium.get(ShardSpawner.ShardType.ANTI_ARMOR).add("csp_cutlet_attack", 2f);
        medium.get(ShardSpawner.ShardType.ANTI_ARMOR).add("csp_cutlet_assault", 2f);
        ShardSpawner.ShardTypeVariants large = com.fs.starfarer.api.impl.hullmods.ShardSpawner.variantData.get(ShipAPI.HullSize.CRUISER);
        large.get(ShardSpawner.ShardType.GENERAL).add("csp_boron_attack", 2f);
        large.get(ShardSpawner.ShardType.ANTI_ARMOR).add("csp_boron_hullbuster", 2f);
        large.get(ShardSpawner.ShardType.POINT_DEFENSE).add("csp_boron_shockmaster", 2f);
        large.get(ShardSpawner.ShardType.ANTI_SHIELD).add("csp_pyramid_shieldbreaker", 2f);
        large.get(ShardSpawner.ShardType.GENERAL).add("csp_pyramid_attack", 2f);

    }

    public void onGameLoad(boolean newGame) {
        haveGettag = Global.getSettings().getModManager().isModEnabled("armaa");
        if (newGame) {
            if (haveGettag) {
                if (Global.getSettings().getHullSpec("csp_luster") != null) {
                    Global.getSector().getFaction("remnant").getKnownShips().add("csp_luster");
                }
                if (Global.getSettings().getHullSpec("csp_remnantarmor") != null) {
                    Global.getSector().getFaction("remnant").getKnownShips().add("csp_remnantarmor");
                }
                if (Global.getSettings().getHullSpec("csp_jug") != null) {
                    Global.getSector().getFaction("derelict").getKnownShips().add("csp_jug");
                }
                if (Global.getSettings().getHullSpec("csp_dijeh") != null) {
                    Global.getSector().getFaction("hegemony").getKnownShips().add("csp_dijeh");
                }
                if (Global.getSettings().getHullSpec("csp_diazxiv") != null) {
                    Global.getSector().getFaction("hegemony").getKnownShips().add("csp_diazxiv");
                }
                if (Global.getSettings().getHullSpec("csp_rickdiaz") != null) {
                    Global.getSector().getFaction("luddic_church").getKnownShips().add("csp_rickdiaz");
                }
                if (Global.getSettings().getHullSpec("csp_dijeh") != null) {
                    Global.getSector().getFaction("mercenary").getKnownShips().add("csp_dijeh"); }

                { if (Global.getSettings().getHullSpec("csp_dijeh") != null)
                    Global.getSettings().getHullSpec("csp_dijeh").addTag("lowtech_bp");}

                { if (Global.getSettings().getHullSpec("csp_diazxiv") != null)
                    Global.getSettings().getHullSpec("csp_dijeh").addTag("XIV_bp");}

                Global.getSector().getFaction("remnant").clearShipRoleCache();
                Global.getSector().getFaction("derelict").clearShipRoleCache();
                Global.getSector().getFaction("hegemony").clearShipRoleCache();
                Global.getSector().getFaction("luddic_church").clearShipRoleCache();
                Global.getSector().getFaction("mercenary").clearShipRoleCache();
            }
        }
    }
    @Override
    public void onNewGameAfterEconomyLoad() {
        //LGDragoon.spawnDragoon();
        Global.getSector().getMemoryWithoutUpdate().set(MEMKEY_SPECIAL_FLEETS_INITIALIZED, true);
        Global.getSector().getMemoryWithoutUpdate().set(MEMKEY_VERSION, 2.84);
    }

    public void onNewGameAfterProcGen() {
        csp_DerelictsSpawnScript.spawnDerelicts(Global.getSector());
    }
}
