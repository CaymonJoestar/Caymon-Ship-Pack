package data.scripts.world;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin.DerelictShipData;
import com.fs.starfarer.api.impl.campaign.enc.AbyssalRogueStellarObjectEPEC;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator.StarSystemType;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.AddedEntity;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.EntityLocation;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.LocationType;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial.PerShipData;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial.ShipCondition;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial.ShipRecoverySpecialData;
import com.fs.starfarer.api.impl.campaign.world.GateHaulerLocation;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import org.lwjgl.util.vector.Vector2f;

import java.util.LinkedHashMap;
import java.util.Random;

public class Lone_world_gen {
	boolean haveRaccoon = Global.getSettings().getModManager().isModEnabled("secretsofthefrontier");
	public static String Lone_World_LOCATION_ID = "lone_world_location";

	public static Lone_world_gen addsystem;

	public void generate(SectorAPI sector) {
		if (haveRaccoon) {
			MemoryAPI memory = Global.getSector().getPlayerPerson().getMemoryWithoutUpdate();
			StarSystemAPI system = sector.createStarSystem("Deep Space");
			//system.setType(StarSystemType.NEBULA);
			system.setName("Deep Space"); // to get rid of "Star System" at the end of the name
			system.setOptionalUniqueId(Lone_World_LOCATION_ID);

			system.setType(StarSystemType.DEEP_SPACE);
			system.addTag(Tags.THEME_HIDDEN);
			system.addTag(Tags.THEME_SPECIAL);
			system.addTag(Tags.SYSTEM_ABYSSAL);


			system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");

			Random random = StarSystemGenerator.random;

			float w = Global.getSettings().getFloat("sectorWidth");
			float h = Global.getSettings().getFloat("sectorHeight");

			Vector2f systemLoc = new Vector2f();
			float outsideMapPad = 1750f;
			float outsideMapRand = 1750f;
			float r = random.nextFloat();
			// avoid the actual lower-left corner somewhat, since the gate hauler is there, too, but
			if (r < 0.5f) { // left
				systemLoc.x = -w / 2f - outsideMapPad - outsideMapRand * random.nextFloat();
				//systemLoc.y = -h/2f - outsideMapPad + (h + outsideMapPad * 2f) * random.nextFloat();
				systemLoc.y = -h / 2f + (h + outsideMapPad * 1f) * random.nextFloat();
			} else { //if (r < 0.75f) { // bottom
				systemLoc.x = -w / 2f + (w + outsideMapPad * 1f) * random.nextFloat();
				systemLoc.y = -h / 2f - outsideMapPad - outsideMapRand * random.nextFloat();
			}
//		else if (r < 0.5f) { // right
//			systemLoc.x = w/2f + outsideMapPad + outsideMapRand * random.nextFloat();
//			systemLoc.y = -h/2f - outsideMapPad + (h + outsideMapPad * 2f) * random.nextFloat();
//		} else { // top
//			systemLoc.x = -w/2f - outsideMapPad + (w + outsideMapPad * 2f) * random.nextFloat();
//			systemLoc.y = h/2f + outsideMapPad + outsideMapRand * random.nextFloat();
//		}
			system.getLocation().set(systemLoc.x, systemLoc.y);

//		Vector2f systemLoc = new Vector2f(3000f, 21000f);
//		systemLoc = Misc.getPointWithinRadius(systemLoc, 1000f, random);
//		system.getLocation().set(-w/2f + systemLoc.x, -h/2f + systemLoc.y);

			SectorEntityToken center = system.initNonStarCenter();

			system.setLightColor(GateHaulerLocation.ABYSS_AMBIENT_LIGHT_COLOR); // light color in entire system, affects all entities
			center.addTag(Tags.AMBIENT_LS);

			//String name = Misc.genEntityCatalogId(2700, 11, 7, CatalogEntryType.PLANET);
			String name = "The Lone World";

			PlanetAPI rock = system.addPlanet("A Lone World", null, name, Planets.FROZEN2, 0, 150, 0, 0);
			rock.setDescriptionIdOverride("toxic_cold");

			//rock.setCustomDescriptionId("???");
			rock.getMemoryWithoutUpdate().set("$lone_world", true);

			rock.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
			rock.getMarket().addCondition(Conditions.VERY_COLD);
			rock.getMarket().addCondition(Conditions.DARK);
			rock.getMarket().addCondition(Conditions.ORE_RICH);
			rock.getMarket().addCondition(Conditions.RARE_ORE_MODERATE);

			rock.setOrbit(null);
			rock.setLocation(0, 0);

				addDiabellstar(rock);


			system.autogenerateHyperspaceJumpPoints(false, false);

			AbyssalRogueStellarObjectEPEC.setAbyssalDetectedRanges(system);
		}
	}


		protected void addDiabellstar(PlanetAPI rock) {
		PerShipData ship = new PerShipData("csp_sierramech_sinful", ShipCondition.WRECKED, 0f);
		ship.shipName = "Sinful Spoil";
		DerelictShipData params = new DerelictShipData(ship, false);
		CustomCampaignEntityAPI entity = (CustomCampaignEntityAPI) BaseThemeGenerator.addSalvageEntity(
									rock.getContainingLocation(),
									Entities.WRECK, Factions.NEUTRAL, params);
		//Misc.makeImportant(entity, "onslaughtMkI");
		entity.getMemoryWithoutUpdate().set("$Diabellstar", true);
		entity.setSensorProfile(1f);
		entity.setDiscoverable(true);

		Random random = StarSystemGenerator.random;
		float orbitRadius = rock.getRadius() + 200f;
		float orbitDays = orbitRadius / (10f + random.nextFloat() * 5f);
		entity.setCircularOrbit(rock, random.nextFloat() * 360f, orbitRadius, orbitDays);

		ShipRecoverySpecialData data = new ShipRecoverySpecialData(null);
		data.notNowOptionExits = true;
		DerelictShipEntityPlugin dsep = (DerelictShipEntityPlugin) entity.getCustomPlugin();
		PerShipData copy = (PerShipData) dsep.getData().ship.clone();
		copy.variant = Global.getSettings().getVariant(copy.variantId).clone();
		copy.variantId = null;
		copy.variant.addTag(Tags.SHIP_CAN_NOT_SCUTTLE);
		copy.variant.addTag(Tags.SHIP_UNIQUE_SIGNATURE);
		copy.nameAlwaysKnown = true;
		//copy.addDmods = false;
		copy.pruneWeapons = false;

		// makes it unpilotable by the the player with Neural Link, don't want that 
//		AICoreOfficerPlugin plugin = Misc.getAICoreOfficerPlugin(Commodities.GAMMA_CORE);
//		if (plugin != null) {
//			copy.captain = plugin.createPerson(Commodities.GAMMA_CORE, Factions.PLAYER, null);
//			copy.captain.getStats().setSkillLevel(Skills.POLARIZED_ARMOR, 2);
//			copy.captain.getStats().setLevel(copy.captain.getStats().getLevel() + 1);
//			Misc.setUnremovable(copy.captain, true);
//			Misc.setKeepOnShipRecovery(copy.captain, true);
//		}

		data.addShip(copy);

		Misc.setSalvageSpecial(entity, data);
	}

}













