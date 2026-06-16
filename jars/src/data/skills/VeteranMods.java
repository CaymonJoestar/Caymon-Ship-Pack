package data.skills;

import com.fs.starfarer.api.characters.CharacterStatsSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.skills.PointDefense;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;
import second_in_command.specs.SCBaseSkillPlugin;

public class VeteranMods extends SCBaseSkillPlugin {
	
	public static float MANEUVERABILITY_BONUS = 20f;
	public static float SPEED_BONUS = 10f;
	public static float SHIELD_DAMAGE_REDUCTION = 10f;
	// I highly suggest that you give your XO skill filename a prefix that belongs to your mod, e.g. (csp_) - Mayu
	public static String ID = "CSP_VeteranMods";
	//public static float DAMAGE_BONUS = 100f;

	@Override
	public String getAffectsString() {
		return "all ships";
	}

	@Override
	public void addTooltip(SCData scData, TooltipMakerAPI tooltipMakerAPI) {
		tooltipMakerAPI.addPara("+10%% Top speed", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		tooltipMakerAPI.addPara("-10%% Damage taken by shields", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		tooltipMakerAPI.addPara("+20%% Maneuverability", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		// I'm adding a separate header - Mayu
		tooltipMakerAPI.addPara("Additional bonus if the officer is a mercenary", 10f, Misc.getStoryOptionColor(), Misc.getStoryOptionColor());
		tooltipMakerAPI.addPara("+10%% Top speed", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		tooltipMakerAPI.addPara("-10%% Damage taken", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
		tooltipMakerAPI.addPara("+10%% Maneuverability", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
	}

	@Override
	public void applyEffectsBeforeShipCreation(SCData data, MutableShipStatsAPI stats, ShipVariantAPI variant, ShipAPI.HullSize hullSize, String id)  {
		stats.getAcceleration().modifyPercent(id, MANEUVERABILITY_BONUS);
		stats.getDeceleration().modifyPercent(id, MANEUVERABILITY_BONUS);
		stats.getTurnAcceleration().modifyPercent(id, MANEUVERABILITY_BONUS * 2f);
		stats.getMaxTurnRate().modifyPercent(id, MANEUVERABILITY_BONUS);
		stats.getMaxSpeed().modifyPercent(id, SPEED_BONUS);
		stats.getShieldDamageTakenMult().modifyMult(id, 1f - SHIELD_DAMAGE_REDUCTION / 100f);
	}

	@Override
	public void advanceInCombat(SCData data, ShipAPI ship, Float amount) {
		MutableShipStatsAPI stats = ship.getMutableStats();
		boolean isMerc = Misc.isMercenary(ship.getCaptain());
		if (isMerc) {
			// Doubled if piloted by a mercenaries
			stats.getAcceleration().modifyPercent(ID, 10f);
			stats.getDeceleration().modifyPercent(ID, 10f);
			stats.getTurnAcceleration().modifyPercent(ID, 10f);
			stats.getMaxTurnRate().modifyPercent(ID, 10f);
			stats.getMaxSpeed().modifyPercent(ID, SPEED_BONUS);
			stats.getShieldDamageTakenMult().modifyMult(ID, 1f - SHIELD_DAMAGE_REDUCTION / 100f);
		}
		else {
			stats.getAcceleration().unmodify(ID);
			stats.getDeceleration().unmodify(ID);
			stats.getTurnAcceleration().unmodify(ID);
			stats.getMaxTurnRate().unmodify(ID);
			stats.getMaxSpeed().unmodify(ID);
			stats.getShieldDamageTakenMult().unmodify(ID);
		}
	}

}