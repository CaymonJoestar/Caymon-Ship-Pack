package data.skills;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;
import second_in_command.specs.SCBaseSkillPlugin;

public class RaptorpackDoctrine extends SCBaseSkillPlugin {

	// Sorry, I don't code in K*tlin - Mayu
	public String ID = "CSP_RaptorpackDoctrine";
	public static float DAMAGE_TO_LARGER_BONUS = 10f;
	public static float DAMAGE_TO_LARGER_BONUS_DEST = 10f;
	public static float PEAK_TIME_BONUS = 15f;
	public static float PEAK_TIME_BONUS_DEST = 15f;
	public static float DP_REDUCTION = 0.15f;
	public static float DP_REDUCTION_MAX = 6f;
	// Merc variables
	public static float DAMAGE_TO_LARGER_BONUS_MERC = 10f;
	public static float PEAK_TIME_BONUS_MERC = 15f;

	@Override
	public String getAffectsString() {
		return "All Cruisers and Destroyers";
	}

	@Override
	public void addTooltip(SCData scData, TooltipMakerAPI tooltipMakerAPI) {
		tooltipMakerAPI.addPara("Cruisers and Destroyers deal increased damage against larger targets", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		tooltipMakerAPI.addPara("   - 10%% increased damage against larger hull size for cruisers and destroyers", 0f, Misc.getTextColor(), Misc.getHighlightColor(), "10%");
		tooltipMakerAPI.addPara("Increased peak operating time for cruisers and destroyers", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		tooltipMakerAPI.addPara("   - 15%% increased operating time for cruisers and destroyers", 0f, Misc.getTextColor(), Misc.getHighlightColor(), "15%");
		tooltipMakerAPI.addPara("Deployment point cost reduced by 15%% or 8, whichever is less", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		/// 
		tooltipMakerAPI.addPara("Additional bonus if the officer is a mercenary", 10f, Misc.getStoryOptionColor(), Misc.getStoryOptionColor());
		tooltipMakerAPI.addPara("+10%% increased damage against larger hull size for cruisers and destroyers", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		tooltipMakerAPI.addPara("+15%% increased operating time for cruisers and destroyers", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
	}

	@Override
	public void applyEffectsBeforeShipCreation(SCData data, MutableShipStatsAPI stats, ShipVariantAPI variant, ShipAPI.HullSize hullSize, String id)  {
		if (hullSize == ShipAPI.HullSize.CRUISER) {
			float baseCost = stats.getSuppliesToRecover().getBaseValue();
			float reduction = Math.min(DP_REDUCTION_MAX, baseCost * DP_REDUCTION);
			stats.getDamageToCruisers().modifyPercent(id, DAMAGE_TO_LARGER_BONUS);
			stats.getDamageToCapital().modifyPercent(id, DAMAGE_TO_LARGER_BONUS);
			stats.getPeakCRDuration().modifyPercent(id, PEAK_TIME_BONUS);
			stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).modifyFlat(id, -reduction);
		}
		else if (hullSize == ShipAPI.HullSize.DESTROYER) {
			float baseCost = stats.getSuppliesToRecover().getBaseValue();
			float reduction = Math.min(DP_REDUCTION_MAX, baseCost * DP_REDUCTION);
			stats.getDamageToCruisers().modifyPercent(id, DAMAGE_TO_LARGER_BONUS_DEST);
			stats.getDamageToCapital().modifyPercent(id, DAMAGE_TO_LARGER_BONUS_DEST);
			stats.getPeakCRDuration().modifyPercent(id, PEAK_TIME_BONUS_DEST);
			stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).modifyFlat(id, -reduction);
		}
		//"Ok so for this one, I just want to make it so that cruisers and destroyers get 20% increased dmg and 30% more ppt" - Caymoni
		if (Global.getCurrentState() != GameState.CAMPAIGN) {
			return;
		}
		FleetMemberAPI ship = stats.getFleetMember();
		if (ship == null){
			return;
		}
		if (ship.getFleetData() == null) {
			return;
		}
		if (ship.getFleetData().getFleet() == null) {
			return;
		}
		boolean isMerc = Misc.isMercenary(stats.getFleetMember().getCaptain());
		if (isMerc) {
			if (hullSize == ShipAPI.HullSize.CRUISER) {
				stats.getDamageToCruisers().modifyPercent(id, DAMAGE_TO_LARGER_BONUS_MERC);
				stats.getDamageToCapital().modifyPercent(id, DAMAGE_TO_LARGER_BONUS_MERC);
				stats.getPeakCRDuration().modifyPercent(id, PEAK_TIME_BONUS_MERC);
			}
			else if (hullSize == ShipAPI.HullSize.DESTROYER) {
				stats.getDamageToCruisers().modifyPercent(id, DAMAGE_TO_LARGER_BONUS_MERC);
				stats.getDamageToCapital().modifyPercent(id, DAMAGE_TO_LARGER_BONUS_MERC);
				stats.getPeakCRDuration().modifyPercent(id, PEAK_TIME_BONUS_MERC);
			}
		}
	}

}