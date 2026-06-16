package data.scripts.campaign.fleets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.events.OfficerManagerEvent;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.characters.FullName.Gender;
import org.magiclib.util.MagicCampaign;

import java.util.HashMap;
import java.util.Map;

public class LGDragoon  {
	/**
	 * To add a new fleet:
	 * 1) Make a copy of this method
	 * 2) Call it from spawn()
	 */

	public static void spawnDragoon() {
		SectorEntityToken target = null;
		if (Global.getSector().getEntityById("sindria") != null && Global.getSector().getEntityById("sindria").getFaction() == Global.getSector().getFaction("sindrian_diktat")) {
			target = Global.getSector().getEntityById("sindria");
		} else {
			for (MarketAPI m : Global.getSector().getEconomy().getMarketsCopy()) {
				if (m.getFaction().getId().equals("sindrian_diktat")) {
					if (target == null || (m.hasSubmarket(Submarkets.GENERIC_MILITARY)
							&& (!target.getMarket().hasSubmarket(Submarkets.GENERIC_MILITARY) || m.getSize() > target.getMarket().getSize()))) {
						target = m.getPrimaryEntity();
					}
				}
			}
		}

		//Map<String,Integer> spearhead=new HashMap<>();

		//spearhead.put("csp_skystrikerLG_standard", 2);
		//spearhead.put("csp_LG_Mech_elite", 3);
		//spearhead.put("csp_garudalg_standard", 1);
		//spearhead.put("csp_ruination_support", 2);
		//spearhead.put("csp_bonnetheadlg_assault", 2);

		if (target != null) {
			PersonAPI DragoonCaptain = MagicCampaign.createCaptainBuilder("sindrian_diktat")

					.setFirstName("Regis")
					.setLastName("Artorius")
					.setPortraitId("csp_Dragoon")
					.setGender(Gender.FEMALE)
					.setRankId(Ranks.SPACE_ADMIRAL)
					.setPostId(Ranks.POST_FLEET_COMMANDER)
					.setPersonality(Personalities.RECKLESS)
					.setLevel(10)
					.setEliteSkillsOverride(10)
					.setSkillPreference(OfficerManagerEvent.SkillPickPreference.YES_ENERGY_YES_BALLISTIC_YES_MISSILE_YES_DEFENSE)
					.create();

			CampaignFleetAPI Dragoon = MagicCampaign.createFleetBuilder()
					.setFleetFaction("sindrian_diktat")
					.setFleetName("The Lion's Spear")
					.setFleetType(FleetTypes.TASK_FORCE)
					.setFlagshipName("The Deathless Dragoon")
					.setFlagshipAlwaysRecoverable(true)
					.setFlagshipVariant("csp_unicetan_Dragoon")
					.setFlagshipAutofit(false)
					.setCaptain(DragoonCaptain)
					.setMinFP(500)
					//.setSupportFleet(spearhead)
					.setReinforcementFaction("lions_guard")
					.setQualityOverride(2f)
					.setAssignment(FleetAssignment.PATROL_SYSTEM)
					.setAssignmentTarget(target)
					.setSpawnLocation(target)
					.setIsImportant(true)
					.setTransponderOn(true)
					.create();

			Dragoon.setDiscoverable(true);
		}
	}
}