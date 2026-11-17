package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class csp_SmallBallisticsattunement extends BaseHullMod {

	public static final float COST_REDUCTION  = 2;
	public static final float FLUX_REDUCTION = 10f;
	public static float RANGE_BONUS = 100f;
	public static final float DAMAGE_BONUS_PERCENT = 20f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getDynamic().getMod(Stats.SMALL_BALLISTIC_MOD).modifyFlat(id, -COST_REDUCTION);
		stats.getBallisticWeaponFluxCostMod().modifyMult(id, 1f - (FLUX_REDUCTION * 0.01f));
		stats.getBallisticWeaponDamageMult().modifyPercent(id,DAMAGE_BONUS_PERCENT);
		stats.getBallisticWeaponRangeBonus().modifyFlat(id,RANGE_BONUS);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		return null;
	}
	@Override
	public void addPostDescriptionSection(final TooltipMakerAPI tooltip, final ShipAPI.HullSize hullSize, final ShipAPI ship, final float width, final boolean isForModSpec) {
		final Color green = new Color(55,245,65,255); // Color of the positive value that this hullmod adds which is green colored
		//final Color red = new Color(255,0,0,255); // Here's another example of color, I use this for maluses or negative value
		final float pad = 10f; // The required padding for the paragraphs so it won't look cramped; 10f is the standard
		tooltip.addSectionHeading("Details", Alignment.MID, pad); // Header or the title of the section. Another example is "Incompatibilities"
		tooltip.addPara("- Reduces the ordnance point cost of small ballistic weapons by %s \n- Reduced weapon flux cost of ballistic weaponry by %s \n- Increased ballistic weapon damage by %s \n- Increased weapon range of small ballistics by %s", pad, green,new String[] { Misc.getRoundedValue(2.0f) + "", Misc.getRoundedValue(10.0f) + "%", Misc.getRoundedValue(20.0f) + "%", Misc.getRoundedValue(100.0f) + "" });
		// %s means it has to call the value from 'newString[] { "have sex" };' and if you want to call a string with numbers then it should be-
		// Misc.getRoundedValue(69.0f) + "%" or if it is just a flat value just make it Misc.getRoundedValue(69.0f) + "" without the percent sign within the -> ""
	}

	@Override
	public boolean affectsOPCosts() {
		return true;
	}

}








