package data.skills

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.MutableShipStatsAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.combat.ShipVariantAPI
import com.fs.starfarer.api.impl.campaign.ids.HullMods
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.impl.campaign.ids.Strings
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import second_in_command.SCData
import second_in_command.specs.SCBaseSkillPlugin

class DivideandConquer : SCBaseSkillPlugin() {

    var CAP_RANGE = 250f
    var CAP_RATE = 2f

    override fun getAffectsString(): String {
        return "all ships"
    }

    override fun addTooltip(data: SCData, tooltip: TooltipMakerAPI) {

        tooltip.addPara("Combat objectives are captured 2${Strings.X} faster and can be captured from 250 units further away", 0f, Misc.getHighlightColor(), Misc.getHighlightColor())
    }

    override fun applyEffectsBeforeShipCreation(data: SCData, stats: MutableShipStatsAPI?, variant: ShipVariantAPI, hullSize: ShipAPI.HullSize?, id: String?) {
        stats!!.dynamic.getMod(Stats.SHIP_OBJECTIVE_CAP_RANGE_MOD).modifyFlat(id, CAP_RANGE)
        stats.dynamic.getStat(Stats.SHIP_OBJECTIVE_CAP_RATE_MULT).modifyMult(id, CAP_RATE)
        stats.dynamic.getMod(Stats.CAN_DEPLOY_LEFT_RIGHT_MOD).modifyFlat(id, 1f)
}
    override fun applyEffectsAfterShipCreation(data: SCData, ship: ShipAPI?, variant: ShipVariantAPI, id: String?) {
    }

}