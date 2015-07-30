package com.nicktoony.scrAI.Advisors;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepDefinition;
import com.nicktoony.scrAI.World.Creeps.CreepMiner;
import com.nicktoony.scrAI.World.SourceWrapper;
import org.stjs.javascript.Global;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 * var Constants = require('Constants');
 * var Advisor = require("Advisor");
 * var CreepMiner = require("CreepMiner");
 */
public class EconomyAdvisor extends Advisor {
    private int tier;

    public EconomyAdvisor(RoomController roomController) {
        super(roomController);
        this.tier = Math.max(this.roomController.getSpawnsManager().getSpawns().$length(),1);
    }

    @Override
    public CreepDefinition step() {
        // If there isn't enough creep miners
        if (this.roomController.getSourcesManager().getMaxMiners() >
                this.roomController.getPopulationManager().getSortedCreeps(Constants.CREEP_MINER_ID).$length()) {

            SourceWrapper sourceWrapper = this.roomController.getSourcesManager().getFreeSource();
            int workParts = (int) Math.ceil((Constants.OPTIMAL_WORK - sourceWrapper.getMiningRate())/sourceWrapper.getAvailableSpots());

            // create a new miner
            return CreepMiner.define(this.roomController, tier, workParts, sourceWrapper.getSource());
        }


        // Nothing we want..
        return null;
    }

}
