package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;

import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import data.shipsystems.scripts.csp_transamSubsystem;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.plugins.MagicTrailPlugin;
import org.magiclib.subsystems.MagicSubsystemsManager;
import org.magiclib.util.MagicIncompatibleHullmods;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class csp_sierramechcaller extends BaseHullMod {
    private final Set<String> BLOCKED_HULLMODS = new HashSet<>();
    {
        // These hullmods will automatically be removed
        // This prevents unexplained hullmod blocking
        BLOCKED_HULLMODS.add("sotf_fervor");
    }
    private static final Color ENGINE_COLOR = new Color(125,125,225,255);
    private static final Color CONTRAIL_COLOR = new Color(255, 100, 100);


    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        boolean haveRaccoon = Global.getSettings().getModManager().isModEnabled("secretsofthefrontier");
        for (String tmp : BLOCKED_HULLMODS) {
            if (ship.getVariant().getHullMods().contains(tmp)) {
                MagicIncompatibleHullmods.removeHullmodWithWarning(ship.getVariant(), tmp, "This ship doesnt have any phase capability");
            }
        }
        if (haveRaccoon) {
            ship.getVariant().addPermaMod("sotf_sierrasconcord");
            ship.getHullSpec().setTravelDriveId("sotf_concordentry");
            ship.getHullSpec().setManufacturer("Anomalous Phase-Tech");
        }
        MagicSubsystemsManager.addSubsystemToShip(ship, new csp_transamSubsystem(ship));
    }
}
