package data.scripts.world;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;

public class csp_Gen implements SectorGeneratorPlugin {
	boolean haveRaccoon = Global.getSettings().getModManager().isModEnabled("secretsofthefrontier");

	public void generate(SectorAPI sector) {
		if (haveRaccoon){
			new Lone_world_gen().generate(sector);
	  }
	}
}