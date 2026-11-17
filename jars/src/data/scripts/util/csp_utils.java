package data.scripts.util;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Personalities;

public class csp_utils {
    public static float lerp(float x, float y, float alpha) {
        return (1f - alpha) * x + alpha * y;
    }

    public static float effectiveRadius(ShipAPI ship) {
        if (ship.getSpriteAPI() == null || ship.isPiece()) {
            return ship.getCollisionRadius();
        } else {
            float fudgeFactor = 1.5f;
            return ((ship.getSpriteAPI().getWidth() / 2f) + (ship.getSpriteAPI().getHeight() / 2f)) * 0.5f * fudgeFactor;
        }
    }
    public static String getMoreAggressivePersonality(FleetMemberAPI member, ShipAPI ship) {
        if (ship == null) {
            return Personalities.AGGRESSIVE;
        }

        boolean player = false;
        if ((member != null) && (member.getFleetData() != null) && (member.getFleetData().getFleet() != null)
                && member.getFleetData().getFleet().isPlayerFleet()) {
            player = true;
        }

        String personality = null;
        if (member != null) {
            if (member.getCaptain() != null) {
                /* Skip the player's ship or any player officer ships */
                if (player && (!member.getCaptain().isDefault() || member.getCaptain().isPlayer())) {
                    return null;
                }

                personality = member.getCaptain().getPersonalityAPI().getId();
            }
        } else {
            if (ship.getCaptain() != null) {
                personality = ship.getCaptain().getPersonalityAPI().getId();
            }
        }

        if ((ship.getShipAI() != null) && (ship.getShipAI().getConfig() != null)) {
            if (ship.getShipAI().getConfig().personalityOverride != null) {
                personality = ship.getShipAI().getConfig().personalityOverride;
            }
        }

        String newPersonality;
        if (personality == null) {
            newPersonality = Personalities.AGGRESSIVE;
        } else {
            switch (personality) {
                case Personalities.TIMID:
                    newPersonality = Personalities.CAUTIOUS;
                    break;
                case Personalities.CAUTIOUS:
                    newPersonality = Personalities.STEADY;
                    break;
                default:
                case Personalities.STEADY:
                    newPersonality = Personalities.AGGRESSIVE;
                    break;
                case Personalities.AGGRESSIVE:
                case Personalities.RECKLESS:
                    newPersonality = Personalities.RECKLESS;
                    break;
            }
        }

        return newPersonality;
    }
    public static String getLessAggressivePersonality(FleetMemberAPI member, ShipAPI ship) {
        if (ship == null) {
            return Personalities.CAUTIOUS;
        }

        boolean player = false;
        if ((member != null) && (member.getFleetData() != null) && (member.getFleetData().getFleet() != null)
                && member.getFleetData().getFleet().isPlayerFleet()) {
            player = true;
        }

        String personality = null;
        if (member != null) {
            if (member.getCaptain() != null) {
                /* Skip the player's ship or any player officer ships */
                if (player && (!member.getCaptain().isDefault() || member.getCaptain().isPlayer())) {
                    return null;
                }

                personality = member.getCaptain().getPersonalityAPI().getId();
            }
        } else {
            if (ship.getCaptain() != null) {
                personality = ship.getCaptain().getPersonalityAPI().getId();
            }
        }

        if ((ship.getShipAI() != null) && (ship.getShipAI().getConfig() != null)) {
            if (ship.getShipAI().getConfig().personalityOverride != null) {
                personality = ship.getShipAI().getConfig().personalityOverride;
            }
        }

        String newPersonality;
        if (personality == null) {
            newPersonality = Personalities.CAUTIOUS;
        } else {
            switch (personality) {
                case Personalities.TIMID:
                case Personalities.CAUTIOUS:
                    newPersonality = Personalities.TIMID;
                    break;
                default:
                case Personalities.STEADY:
                    newPersonality = Personalities.CAUTIOUS;
                    break;
                case Personalities.AGGRESSIVE:
                    newPersonality = Personalities.STEADY;
                    break;
                case Personalities.RECKLESS:
                    newPersonality = Personalities.AGGRESSIVE;
                    break;
            }
        }

        return newPersonality;
    }

}
