package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;

public class csp_selector_rynex extends BaseHullMod {
    
    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
 		if (index == 0) return "Rynex Pulse Laser Arm";
		if (index == 1) return "Remove this hullmod to cycle between weapons.";
        return null;    
    }
}
