package data.skills;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;
import second_in_command.SCData;
import second_in_command.specs.SCBaseSkillPlugin;

public class Divideandconquer2 extends SCBaseSkillPlugin {
	private static final float RANGE_BONUS = 10f;
	public static float CAP_RANGE = 250f;
	public static float CAP_RATE = 2f;

	public String getAffectsString() {
		return "all ships with fighter bays";
	}

	public void addTooltip(SCData scData, TooltipMakerAPI tooltipMakerAPI) {
		tooltipMakerAPI.addPara("Combat objectives are captured 2x faster and can be captured from 250 units further away", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		tooltipMakerAPI.addPara("Ships within 1250 units of a combat objective gain 10%% increase toward all weapon ranges", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
	}

	public void applyEffectsBeforeShipCreation(SCData data, MutableShipStatsAPI stats, ShipVariantAPI variant, HullSize hullSize, String id) {
		stats.getDynamic().getMod(Stats.SHIP_OBJECTIVE_CAP_RANGE_MOD).modifyFlat(id, CAP_RANGE);
		stats.getDynamic().getStat(Stats.SHIP_OBJECTIVE_CAP_RATE_MULT).modifyMult(id, CAP_RATE);
	}

	public void advanceInCombat(ShipAPI ship, CombatEngineAPI engine, String id) {
		Vector2f shipLoc = ship.getLocation();
		boolean takeEffect = false;
		for (BattleObjectiveAPI tmp : Global.getCombatEngine().getObjectives()) {
			if (MathUtils.isWithinRange(tmp.getLocation(), shipLoc, 1250f)) {
				takeEffect = true;
				break;
			}
		}
		MutableShipStatsAPI stats = ship.getMutableStats();
		if (takeEffect) {
			stats.getBallisticWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
			stats.getEnergyWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
			if (ship.isAlly()) {
				Global.getCombatEngine().maintainStatusForPlayerShip("csp_dc", "graphics/icons/hullsys/targeting_feed.png",
						"Divide and Conquer", "10% Weapon Range", false);
			} else {
				stats.getBallisticWeaponRangeBonus().unmodify(id);
				stats.getEnergyWeaponRangeBonus().unmodify(id);
			}
		}
	}
}












