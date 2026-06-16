package data.skills;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;
import second_in_command.specs.SCAptitudeSection;
import second_in_command.specs.SCBaseAptitudePlugin;

public class MercAptitude extends SCBaseAptitudePlugin {
    public static boolean haveGettag;

    public void addCodexDescription(TooltipMakerAPI tooltipMakerAPI) {
        tooltipMakerAPI.addPara("Mercenary offers the wisdom of the veteran, those who fought and lived through the sector's many conflicts, passing on their flexible but niche skills that can round out any fleet, with a lean toward benefits for destroyers and cruisers. " +
                        "It also offers benefits for a specialized set of fighter-like frigates. ",
                0f, Misc.getTextColor(), Misc.getHighlightColor(), "Mercenary", "");
    }
    //The ID of the skill that is always active
    @Override
    public String getOriginSkillId() {
        return "sc_veteranmods";
    }

    //Determines which skills are added and how they are sectioned off in the UI
    @Override
    public void createSections() {
        haveGettag = Global.getSettings().getModManager().isModEnabled("armaa");

        //Parameters:
        // - canChooseMultiple: Can only one skill be selected from this section?
        // - requiredPreviousSkills: How many skills in previous sections are required for accessing this one?
        // - soundId what skill is played when clicking on skills from this section?
        SCAptitudeSection section1 = new SCAptitudeSection(true, 0, "leadership1");
        section1.addSkill("sc_coordinatedstrikeattack");
        section1.addSkill("sc_overhauledarmorstructure");
        section1.addSkill("sc_escortduty");
        if (haveGettag) {
            section1.addSkill("sc_mechcraft");
        }
        section1.addSkill("sc_wellequipped");
        section1.addSkill("sc_veteranstandard");
        addSection(section1); // Finalize the section by adding it.

        SCAptitudeSection section2 = new SCAptitudeSection(true, 2, "leadership2");
        section2.addSkill("sc_raptorpackdoctrine");
        section2.addSkill("sc_divideandconquer");
        addSection(section2);

        SCAptitudeSection section3 = new SCAptitudeSection(false, 4, "leadership3");
        section3.addSkill("sc_cyberneticaugmentation");
        section3.addSkill("sc_strengthofthepackisthewolf");
        addSection(section3);

    }

    //Chance for the aptitude to appear on NPC fleets.
    //Lower value is rarer, higher is more common.
    //Should be around 1 on average.
    @Override
    public Float getNPCFleetSpawnWeight(SCData data, CampaignFleetAPI fleet) {
        return 0.5f;
    }
}
