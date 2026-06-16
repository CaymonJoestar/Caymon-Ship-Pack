package data.skills;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;
import second_in_command.SCData;
import second_in_command.specs.SCBaseSkillPlugin;

public class Divideandconquer3 extends SCBaseSkillPlugin {
	//private static final float RANGE_BONUS = 10f;
	public static float CAP_RANGE = 150f;
	public static float CAP_RATE = 2f;
	public static float DEPLOYMENT_BONUS = 0.05f;
	//public static float CP_REGEN_FRIGATES = 25f;
	//public static float CP_REGEN_DESTROYERS = 75f;
	//public static float CP_REGEN_CRUISERS = 50f;

	public String getAffectsString() {
		return "all ships";
	}

	public void addTooltip(SCData scData, TooltipMakerAPI tooltipMakerAPI) {
		tooltipMakerAPI.addPara("Combat objectives are captured 2x faster and can be captured from 150 units further away", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		//tooltipMakerAPI.addPara("+75%% to command point recovery from deployed destroyers, +50%% from cruisers and +25%% from frigates", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		tooltipMakerAPI.addPara("Deployment points bonus from objectives is at least 5%% of the battle size, even if holding no objectives", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
	}

	//public static boolean isFrigateOrDestroyerOrCruiserAndOfficer(MutableShipStatsAPI stats) {
		//FleetMemberAPI member = stats.getFleetMember();
		//if (member == null) return false;
		// applies at least 1% in all cases now
		//if (!member.isFrigate() && !member.isDestroyer()) return false;

		//return !member.getCaptain().isDefault();
	//}

	public void applyEffectsBeforeShipCreation(SCData data, MutableShipStatsAPI stats, ShipVariantAPI variant, HullSize hullSize, String id) {
		stats.getDynamic().getMod(Stats.SHIP_OBJECTIVE_CAP_RANGE_MOD).modifyFlat(id, CAP_RANGE);
		stats.getDynamic().getStat(Stats.SHIP_OBJECTIVE_CAP_RATE_MULT).modifyMult(id, CAP_RATE);
		stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MIN_FRACTION_OF_BATTLE_SIZE_BONUS_MOD).modifyFlat(id, DEPLOYMENT_BONUS);

		//if (isFrigateOrDestroyerOrCruiserAndOfficer(stats)) {
			//float bonus = 0f;
			//if (hullSize == HullSize.FRIGATE) bonus = CP_REGEN_FRIGATES;
			//if (hullSize == HullSize.DESTROYER) bonus = CP_REGEN_DESTROYERS;
			//if (hullSize == HullSize.CRUISER) bonus = CP_REGEN_CRUISERS;
			//if (bonus > 0f) {
				//stats.getDynamic().getMod(Stats.COMMAND_POINT_RATE_FLAT).modifyFlat(id, bonus * 0.01f);
			//}
		//}
	}
}












