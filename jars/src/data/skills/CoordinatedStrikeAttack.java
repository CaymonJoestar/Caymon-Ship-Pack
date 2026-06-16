package data.skills;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;
import second_in_command.specs.SCBaseSkillPlugin;

public class CoordinatedStrikeAttack extends SCBaseSkillPlugin {

	public static float FLUX_PER_OP = 2f;
	public static float CAP_PER_OP = 20;
	//
	public static float RANGE_BONUS = 100f;

	public static int computeFighterOPCost(MutableShipStatsAPI stats) {
		int totalOp = 0;
		for (String wingId : stats.getVariant().getFittedWings()){
			totalOp += (int)Global.getSettings().getFighterWingSpec(wingId).getOpCost(stats);
		}
		return totalOp;
	}

	@Override
	public String getAffectsString() {
		return "all ships with fighter bays";
	}

	@Override
	public void addTooltip(SCData scData, TooltipMakerAPI tooltipMakerAPI) {
		tooltipMakerAPI.addPara("+2 flux dissipation per ordnance point spend on fighters", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		tooltipMakerAPI.addPara("+20 flux capacity per ordnance point spend on fighters", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		///
		tooltipMakerAPI.addPara("Additional bonus if the officer is a mercenary", 10f, Misc.getStoryOptionColor(), Misc.getStoryOptionColor());
		tooltipMakerAPI.addPara("+100 range to non-missile weapons on fighters if their engagement range are below 500 units", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
	}

	@Override
	public void applyEffectsBeforeShipCreation(SCData data, MutableShipStatsAPI stats, ShipVariantAPI variant, ShipAPI.HullSize hullSize, String id) {
		if (stats.getVariant() != null) {
			float flux = FLUX_PER_OP * computeFighterOPCost(stats);
			stats.getFluxDissipation().modifyFlat(id, flux);
		}
		if (stats.getVariant() != null) {
			float flux = CAP_PER_OP * computeFighterOPCost(stats);
			stats.getFluxCapacity().modifyFlat(id, flux);
		}
	}

	@Override
	public void applyEffectsToFighterSpawnedByShip(SCData data, ShipAPI fighter, ShipAPI ship, String id) {
		boolean isMerc = Misc.isMercenary(ship.getCaptain());
		if (isMerc) {
			float engagement_range = 500f;
			if (fighter.getWing().getSpec().getRange() == engagement_range) {
				fighter.getMutableStats().getBallisticWeaponRangeBonus().modifyFlat(id, RANGE_BONUS);
				fighter.getMutableStats().getEnergyWeaponRangeBonus().modifyFlat(id, RANGE_BONUS);
			}
		}
	}

}