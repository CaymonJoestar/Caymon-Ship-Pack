package data.shipsystems.scripts;


import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import data.scripts.util.MagicAnim;
import data.scripts.util.csp_UniThreatenJudge;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.subsystems.MagicSubsystem;

import java.awt.Color;
import java.util.ArrayList;

public class csp_transamSubsystem extends MagicSubsystem {
    private final Integer TURN_ACC_BUFF = 500;
    private final Integer TURN_RATE_BUFF = 250;
    private final Integer ACCEL_BUFF = 250;
    private final Integer DECCEL_BUFF = 150;
    private final Integer SPEED_BUFF = 100;
    private final Integer TIME_BUFF = 300;
    private static final Color SYSTEM_COLOR = new Color(125,125,225,255);
    public static final String SYSTEM_ID = "csp_transamV2";
    private int BASE_MAX_CHARGES = 3;
    private boolean Need_dodge = false;
    private boolean Need_attack= false;

    public csp_transamSubsystem(ShipAPI ship) {
        super(ship);
    }


    @Override
    public float getBaseInDuration() {
        return 2f;
    }

    @Override
    public float getBaseActiveDuration() {
        return 0f;
    }

    @Override
    public float getBaseOutDuration() {
        return 0.5f;
    }

    @Override
    public float getBaseCooldownDuration() {
        return 0.5f;
    }

    @Override
    public int getMaxCharges() {
        return 3;
    }

    @Override
    public float getBaseChargeRechargeDuration() {
        return 20f;
    }

    @Override
    public boolean shouldActivateAI(float amount) {
        ShipAPI target = ship.getShipTarget();



        if(csp_UniThreatenJudge.isTreatenedbyProjectile(ship,300) )
        {
            Need_dodge=true;
            return true;
        }

        if(csp_UniThreatenJudge.BeamweaponFiring(ship,2000)){
            Need_dodge=true;
            return true;
        }


        if (target != null&& charges==BASE_MAX_CHARGES) {
            float score = 0f;

            if (target.getFluxTracker().isOverloadedOrVenting()) {
                score += 6f;
            } else {
                score += target.getFluxLevel() * 6f;
            }

            float dist = Misc.getDistance(ship.getLocation(), target.getLocation());

//            float avgRange = aiData.getAverageWeaponRange(false);
            score += Math.min(dist/200f, 4f);

            if(score > 8f){
                Need_attack=true;
                return true;
            }

        }

        return false;
    }

    @Override
    public void onActivate() {

        if (!stats.getTimeMult().getPercentMods().containsKey(SYSTEM_ID)) {
            Global.getSoundPlayer().playSound("csp_drift", 1, 1.66f, ship.getLocation(), ship.getVelocity());
                }
                    float effect = Math.min(1, Math.max(0, MagicAnim.smoothReturnNormalizeRange(getEffectLevel(), 0, 1) / 2 + MagicAnim.smoothReturnNormalizeRange(getEffectLevel() * 1.5f, 0, 1) / 2 + MagicAnim.smoothReturnNormalizeRange(getEffectLevel() * 2, 0, 1) / 2));

                    //visual effect
                    ShipAPI ship = (ShipAPI) stats.getEntity();
                    if (ship != null) {
                        ship.setJitterUnder(
                                ship,
                                SYSTEM_COLOR,
                                0.5f * effect,
                                5,
                                5 + 5f * effect,
                                5 + 10f * effect
                        );
                        if (Math.random() > 0.9f) {
                            ship.addAfterimage(new Color(70, 40, 130, 200), 0, 0, -ship.getVelocity().x, -ship.getVelocity().y, 5 + 50 * effect, 0, 0, 2 * effect, false, false, false);
                        }
                        if (state == State.IN || state == State.ACTIVE || state == State.OUT) {
                        if (!stats.getTimeMult().getPercentMods().containsKey(SYSTEM_ID)) {

                            //protection against burst weapons abuse
                            ship.setPhased(true);
//                for (WeaponAPI w : ship.getAllWeapons()){
//                    if(w.getChargeLevel()==1){
//                        w.setRemainingCooldownTo(w.getCooldown());
//                    }
//                }
                        } else if (ship.isPhased()) {
                            ship.setPhased(false);
                        }
                    }
                }

        //ship can reorient
        stats.getTurnAcceleration().modifyPercent(this.getDisplayText(), 500f);
        stats.getMaxTurnRate().modifyPercent(this.getDisplayText(), 250f);

        //ship can slightly jump forward
        stats.getMaxSpeed().modifyPercent(this.getDisplayText(), 100f);
        stats.getAcceleration().modifyPercent(this.getDisplayText(), 250f);
        stats.getDeceleration().modifyPercent(this.getDisplayText(), 150f);

        //time drift
        stats.getTimeMult().modifyPercent(this.getDisplayText(), 300f);

    }

    @Override
    public void onFinished() {
        stats.getMaxTurnRate().unmodify(this.getDisplayText());;
        stats.getTurnAcceleration().unmodify(this.getDisplayText());;

        stats.getMaxSpeed().unmodify(this.getDisplayText());
        stats.getAcceleration().unmodify(this.getDisplayText());
        stats.getDeceleration().unmodify(this.getDisplayText());

        stats.getTimeMult().unmodify(this.getDisplayText());
    }

    @Override
    public String getDisplayText() {
        return "Witch Time";
    }


}
