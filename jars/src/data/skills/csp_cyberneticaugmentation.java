package data.skills;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;
import second_in_command.specs.SCBaseSkillPlugin;

public class csp_cyberneticaugmentation extends SCBaseSkillPlugin {

	// I don't code in K*tlin - Mayu
	public String ID = "csp_cyberneticaugmentation";
	public static float AMMO_RELOAD_BONUS = 50f;
	// Merc variables
	public static float DAMAGE_TO_LARGER_BONUS_MERC = 20f;
	public static float PEAK_TIME_BONUS_MERC = 30f;

	@Override
	public String getAffectsString() {
		return "all ships without officers";
	}

	@Override
	public void addTooltip(SCData scData, TooltipMakerAPI tooltipMakerAPI) {
		tooltipMakerAPI.addPara("Grants non-elite Gunnery Implants, Target Analysis, Point Defense to ships without officers", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		tooltipMakerAPI.addPara("Gives all ships 50%% more ammo regeneration for weapons that use ammunition", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		/// 
		tooltipMakerAPI.addPara("Additional bonus if the officer is a mercenary", 10f, Misc.getStoryOptionColor(), Misc.getStoryOptionColor());
		tooltipMakerAPI.addPara("Gains the non-elite version of Gunnery Implants, Target Analysis and Point Defense if any of these skill are not learned", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		tooltipMakerAPI.addPara("If the aforementioned skills are already present, gains 5%% deployment points reduction per skill up to a maximum of 15%%", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
	}

	@Override
	public void applyEffectsBeforeShipCreation(SCData data, MutableShipStatsAPI stats, ShipVariantAPI variant, ShipAPI.HullSize hullSize, String id)  {
		stats.getBallisticAmmoRegenMult().modifyPercent(id, AMMO_RELOAD_BONUS);
		stats.getEnergyAmmoRegenMult().modifyPercent(id, AMMO_RELOAD_BONUS);
		// Nullcheck
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
		// apply non-aggregated voids
		if (ship.getCaptain() == null || ship.getCaptain().isDefault()) {
			GunneryImplants(stats,id);
			TargetAnalysis(stats,id);
			PointDefense(stats,id);
		}
		// Merc stuff
		boolean isMerc = Misc.isMercenary(stats.getFleetMember().getCaptain());
		if (ship.getCaptain() != null && isMerc) {
			float DPCostReducer = 0f;
			//if (ship.getCaptain().getStats().getSkillLevel(Skills.GUNNERY_IMPLANTS) < 1) {
			if (!ship.getCaptain().getStats().hasSkill(Skills.GUNNERY_IMPLANTS)) {
				GunneryImplants(stats,id);
			}
			else {
				DPCostReducer += 5f;
			}
			if (!ship.getCaptain().getStats().hasSkill(Skills.TARGET_ANALYSIS)) {
				TargetAnalysis(stats,id);
			}
			else {
				DPCostReducer += 5f;
			}
			if (!ship.getCaptain().getStats().hasSkill(Skills.POINT_DEFENSE)) {
				PointDefense(stats,id);
			}
			else {
				DPCostReducer += 5f;
			}
			stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).modifyFlat(id, -DPCostReducer);
		}
	}

	public static float RECOIL_BONUS = 25f;
	public static float TARGET_LEADING_BONUS = 100f;
	public static float RANGE_BONUS = 15f;
	public void GunneryImplants(MutableShipStatsAPI stats, String id) {
		stats.getMaxRecoilMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getRecoilPerShotMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getAutofireAimAccuracy().modifyFlat(id, TARGET_LEADING_BONUS * 0.01f);
		stats.getBallisticWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
		stats.getEnergyWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);
	}

	public static float DAMAGE_TO_CRUISERS = 15;
	public static float DAMAGE_TO_CAPITALS = 20;
	public void TargetAnalysis(MutableShipStatsAPI stats, String id) {
		stats.getDamageToCruisers().modifyPercent(id, DAMAGE_TO_CRUISERS);
		stats.getDamageToCapital().modifyPercent(id, DAMAGE_TO_CAPITALS);
	}

	public static float FIGHTER_DAMAGE_BONUS = 50f;
	public static float MISSILE_DAMAGE_BONUS = 50f;
	public void PointDefense(MutableShipStatsAPI stats, String id) {
		stats.getDamageToFighters().modifyFlat(id, FIGHTER_DAMAGE_BONUS / 100f);
		stats.getDamageToMissiles().modifyFlat(id, MISSILE_DAMAGE_BONUS / 100f);
	}

}