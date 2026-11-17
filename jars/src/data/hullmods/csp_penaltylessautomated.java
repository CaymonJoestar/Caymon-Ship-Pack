package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;

public class csp_penaltylessautomated extends BaseHullMod {

    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
            ship.getVariant().addTag("no_auto_penalty");
        }
    }

