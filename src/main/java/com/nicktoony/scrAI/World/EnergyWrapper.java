package com.nicktoony.scrAI.World;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepWorker;
import com.nicktoony.screeps.Energy;
import org.stjs.javascript.Map;

/**
 * Created by nick on 29/07/15.
 *
 * @deprecated
 */
public class EnergyWrapper extends MemoryWrapper {
    private RoomController roomController;
    private Energy energy;
    private int claimed = 0;

    public EnergyWrapper(RoomController roomController, Energy energy, Map<String, Object> energyMemory) {
        super(roomController, energyMemory);
        this.energy = energy;
        super.prepare();
    }

    @Override
    public void init() {

    }

    @Override
    public void create() {

    }

    @Override
    public void step() {

    }

    public int availableToClaim() {
        return energy.energy - claimed;
    }


    public Energy getEnergy() {
        return energy;
    }

    public void claim(CreepWorker creepWrapper) {
        claimed += creepWrapper.getCarryCapacity();
    }
}
