package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.SourceWrapper;
import com.nicktoony.screeps.Game;
import com.nicktoony.screeps.GlobalVariables;
import com.nicktoony.screeps.Spawn;
import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

/**
 * Created by nick on 02/08/15.
 */
public class PathsManager {
    private RoomController roomController;
    private Map<String, Object> memory;
    private Array<Array<Map<String, Object>>> paths;
    private int lastUpdate = 0;

    public PathsManager(final RoomController roomController, Map<String, Object> memory) {
        this.roomController = roomController;
        this.memory = memory;

        // Initial delay.. let everything else figure stuff out first
        if (lastUpdate + Constants.PATH_CHECK_DELAY > Game.time) {
            return;
        }

        // Init (sets up paths)
        if (memory.$get("init") == null) {
            init();
            memory.$put("init", true);
        }

        // Load from memory
        paths = (Array<Array<Map<String, Object>>>) memory.$get("paths");
        lastUpdate = (Integer) memory.$get("lastUpdate");
    }

    private void init() {
        this.paths = new Array<Array<Map<String, Object>>>();
        final Spawn spawn = roomController.getSpawnsManager().getSpawns().$get(0);

        Lodash.forIn(roomController.getSourcesManager().getSafeSources(), new LodashCallback1<SourceWrapper>() {
            @Override
            public boolean invoke(SourceWrapper source) {

                paths.push(roomController.getRoom().findPath(source.getSource().pos, spawn.pos, JSCollections.$map(
                        "ignoreCreeps", true,
                        "ignoreDestructibleStructures", true
                )));


                return true;
            }
        }, this);
        memory.$put("paths", paths);
        memory.$put("lastUpdate", Game.time);
    }

    public void update() {
        if (lastUpdate + Constants.PATH_CHECK_DELAY > Game.time) {
            return;
        }

        memory.$put("lastUpdate", Game.time);

        Lodash.forIn(this.paths, new LodashCallback1<Array<Map<String, Object>>>() {
            @Override
            public boolean invoke(Array<Map<String, Object>> path) {

                Lodash.forIn(path, new LodashCallback1<Map<String, Object>>() {
                    @Override
                    public boolean invoke(Map<String, Object> pathStep) {

                        int x = (Integer) pathStep.$get("x");
                        int y = (Integer) pathStep.$get("y");
                        roomController.getRoom().createConstructionSite(x, y, GlobalVariables.STRUCTURE_ROAD);

                        return true;
                    }
                }, this);

                return true;
            }
        }, this);
    }

    public Map<String, Object> getMemory() {
        return memory;
    }
}
