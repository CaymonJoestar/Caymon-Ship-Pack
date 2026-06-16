package data.skills;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;
import second_in_command.specs.SCBaseSkillPlugin;

public class OverhauledArmorStructure extends SCBaseSkillPlugin {

	// I don't code in K*tlin - Mayu
	public String ID = "CSP_OverhauledArmorStructure";
	public static float ARMOR_BONUS = 10f;
	public static float ARMOR_BONUS_UNSHIELDED = 20f;
	public static float MIN_ARMOR_REDUCTION = 5f;
	public static float MIN_ARMOR_REDUCTION_UNSHIELDED = 10f;

	@Override
	public String getAffectsString() {
		return "all ships in the fleet";
	}

	@Override
	public void addTooltip(SCData scData, TooltipMakerAPI tooltipMakerAPI) {
		tooltipMakerAPI.addPara("+10%% armor and hull rating, the effects are doubled for unshielded ships", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		///
		tooltipMakerAPI.addPara("Additional bonus if the officer is a mercenary", 10f, Misc.getStoryOptionColor(), Misc.getStoryOptionColor());
		tooltipMakerAPI.addPara("+5%% minimum armor value for damage reduction, the effects are doubled for unshielded ships", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
	}

	@Override
	public void applyEffectsBeforeShipCreation(SCData data, MutableShipStatsAPI stats, ShipVariantAPI variant, ShipAPI.HullSize hullSize, String id)  {
		if (stats.getVariant().getHullSpec().getShieldType() == ShieldAPI.ShieldType.NONE || stats.getVariant().getHullSpec().getShieldType() == ShieldAPI.ShieldType.PHASE) {
			stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS_UNSHIELDED);
			stats.getHullBonus().modifyPercent(id, ARMOR_BONUS_UNSHIELDED);
		}
		else if (stats.getVariant().getHullSpec().getShieldType() != null) {
			stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS);
			stats.getHullBonus().modifyPercent(id, ARMOR_BONUS);
		}
	}

	@Override
	public void advanceInCombat(SCData data, ShipAPI ship, Float amount) {
		MutableShipStatsAPI stats = ship.getMutableStats();
		boolean isMerc = Misc.isMercenary(ship.getCaptain());
		if (isMerc) {
			if (ship.getShield() == null) {
				stats.getMinArmorFraction().modifyFlat(ID, MIN_ARMOR_REDUCTION_UNSHIELDED);
			}
			else{
				stats.getMinArmorFraction().modifyFlat(ID, MIN_ARMOR_REDUCTION);
			}
		}
		else {
			stats.getMinArmorFraction().unmodify(ID);
		}
	}

}