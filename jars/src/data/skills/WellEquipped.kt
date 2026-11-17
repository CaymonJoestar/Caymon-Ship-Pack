package data.skills

import com.fs.starfarer.api.EveryFrameScript
import com.fs.starfarer.api.Global
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.util.IntervalUtil
import com.fs.starfarer.api.combat.MutableShipStatsAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.combat.ShipVariantAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import second_in_command.SCData
import second_in_command.specs.SCBaseSkillPlugin

class WellEquipped : SCBaseSkillPlugin() {

    override fun getAffectsString(): String {
        return "fleet"
    }

    override fun addTooltip(data: SCData, tooltip: TooltipMakerAPI) {

        tooltip.addPara("You are more likely to find Mercenaries on markets", 0f, Misc.getHighlightColor(), Misc.getHighlightColor())
        tooltip.addPara("-10%% required minimum crew for all ships", 0f, Misc.getHighlightColor(), Misc.getHighlightColor())
        tooltip.addPara("-10%% monthly supply consumption for ship maintenance", 0f, Misc.getHighlightColor(), Misc.getHighlightColor())
        tooltip.addPara("-10%% fuel usage", 0f, Misc.getHighlightColor(), Misc.getHighlightColor())

    }

    override fun applyEffectsBeforeShipCreation(data: SCData, stats: MutableShipStatsAPI?, variant: ShipVariantAPI, hullSize: ShipAPI.HullSize?, id: String?) {
        stats!!.suppliesPerMonth.modifyMult(id, 0.90f)
        stats.fuelUseMod.modifyMult(id, 0.90f)
        stats!!.minCrewMod.modifyMult(id, 0.90f)
    }

    override fun applyEffectsAfterShipCreation(data: SCData, ship: ShipAPI?, variant: ShipVariantAPI, id: String?) {

    }
    override fun onActivation(data: SCData?) {
        if (data!!.isPlayer && !Global.getSector().hasScript(WellEquippedScript::class.java)) {
            Global.getSector().addScript(WellEquippedScript())
        }
    }

    override fun onDeactivation(data: SCData?) {
        if (data!!.isPlayer) {
            var script = Global.getSector().scripts.find { it is WellEquippedScript }
            if (script != null) {
                Global.getSector().removeScript(script)

                for (market in Global.getSector().economy.marketsCopy) {
                    market.stats.dynamic.getMod(Stats.OFFICER_IS_MERC_PROB_MOD).unmodify("csp_well_equipped")
                    market.stats.dynamic.getMod(Stats.OFFICER_ADDITIONAL_PROB_MULT_MOD).unmodify("csp_well_equipped")
                }
            }
        }
    }

    class WellEquippedScript() : EveryFrameScript {

        var interval = IntervalUtil(2f, 3f)

        override fun isDone(): Boolean {
            return false
        }


        override fun runWhilePaused(): Boolean {
            return true
        }

        override fun advance(amount: Float) {
            interval.advance(amount)

            if (interval.intervalElapsed()) {
                for (market in Global.getSector().economy.marketsCopy) {
                    market.stats.dynamic.getMod(Stats.OFFICER_IS_MERC_PROB_MOD).modifyFlat("csp_well_equipped", 0.1f)
                    market.stats.dynamic.getMod(Stats.OFFICER_ADDITIONAL_PROB_MULT_MOD).modifyFlat("csp_well_equipped", 0.1f)

                    market.stats.dynamic.getMod(Stats.OFFICER_IS_MERC_PROB_MOD).modifyMult("csp_well_equipped", 1.2f)
                    market.stats.dynamic.getMod(Stats.OFFICER_ADDITIONAL_PROB_MULT_MOD).modifyMult("csp_well_equipped", 1.2f)
                }
            }


        }

    }

}