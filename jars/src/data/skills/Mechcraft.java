package data.skills;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;
import second_in_command.specs.SCBaseSkillPlugin;

public class Mechcraft extends SCBaseSkillPlugin {

	public static float DAMAGE_TO_FRIGATES = 5;
	public static float DAMAGE_TO_DESTROYERS = 10;
	public static float DAMAGE_TO_CRUISERS = 10;
	public static float DAMAGE_TO_CAPITALS = 10;
	public static float FLUX_COST_MULT = .90f;
	public static float RATE_DECREASE_MODIFIER = 20f;
	public static float RATE_INCREASE_MODIFIER = 20f;
	public static final float BEAM_DAMAGE_REDUCTION = 0.20f;
	public static String MECHCRAFT_DP_REDUCTION_ID = "Mechcraft_dp_reduction";
	public static float DP_REDUCTION = 0.10f;
	public static float DP_REDUCTION_MAX = 3f;
	public static boolean haveGettag;


	//public static float DAMAGE_BONUS = 100f;

	public String getAffectsString() {
		return "all ships with fighters";
	}

	public void addTooltip(SCData scData, TooltipMakerAPI tooltipMakerAPI) {
		tooltipMakerAPI.addPara("+20%% faster fighter replacement rate", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		tooltipMakerAPI.addPara("Ships equipped with the \"Strikecraft\" hullmod gain the following effects", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		tooltipMakerAPI.addPara("   -Deployment point cost reduced by 10%% or 3, whichever is less", 0f, Misc.getTextColor(), Misc.getHighlightColor(), "10%");
		tooltipMakerAPI.addPara("   -Increased damage against cruisers and capitals by 10%%", 0f, Misc.getTextColor(), Misc.getHighlightColor(), "10%");
		tooltipMakerAPI.addPara("   -Decreased energy and ballistic weapons flux cost by 10%%", 0f, Misc.getTextColor(), Misc.getHighlightColor(), "10%");
		tooltipMakerAPI.addPara("   -Decreased damage taken by beam weapons by 20%%", 0f, Misc.getTextColor(), Misc.getHighlightColor(), "20%");
	}

	public void applyEffectsBeforeShipCreation(SCData data, MutableShipStatsAPI stats, ShipVariantAPI variant, HullSize hullSize, String id) {
		haveGettag = Global.getSettings().getModManager().isModEnabled("armaa");
		stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_DECREASE_MULT).modifyMult(id, 1f - RATE_DECREASE_MODIFIER / 100f);
		stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_INCREASE_MULT).modifyPercent(id, RATE_INCREASE_MODIFIER);
		if (haveGettag) {
			if (variant.hasHullMod("strikeCraft")) {
				stats.getDamageToCruisers().modifyPercent(id, DAMAGE_TO_CRUISERS);
				stats.getDamageToCapital().modifyPercent(id, DAMAGE_TO_CAPITALS);
				stats.getBeamDamageTakenMult().modifyMult(id, BEAM_DAMAGE_REDUCTION);
				stats.getEnergyWeaponFluxCostMod().modifyMult(id, FLUX_COST_MULT);
				stats.getBallisticWeaponFluxCostMod().modifyMult(id, FLUX_COST_MULT);
//			stats.getBallisticWeaponDamageMult().modifyPercent(id, DAMAGE_BONUS);
//			stats.getEnergyWeaponDamageMult().modifyPercent(id, DAMAGE_BONUS);
//			stats.getMissileWeaponDamageMult().modifyPercent(id, DAMAGE_BONUS);
				float baseCost = stats.getSuppliesToRecover().getBaseValue();
				float reduction = Math.min(DP_REDUCTION_MAX, baseCost * DP_REDUCTION);

				if (stats.getFleetMember() == null || stats.getFleetMember().getVariant() == null ||
						(!stats.getFleetMember().getVariant().hasHullMod(HullMods.NEURAL_INTERFACE) &&
								!stats.getFleetMember().getVariant().hasHullMod(HullMods.NEURAL_INTEGRATOR))
				) {
					stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).modifyFlat(MECHCRAFT_DP_REDUCTION_ID, -reduction);
				}
			}
		}
	}
}

