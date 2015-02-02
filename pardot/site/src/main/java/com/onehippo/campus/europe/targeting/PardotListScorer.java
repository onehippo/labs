package com.onehippo.campus.europe.targeting;

import com.onehippo.cms7.targeting.Scorer;
import com.onehippo.cms7.targeting.model.TargetGroup;

import java.util.Map;

public class PardotListScorer implements Scorer<PardotData> {

    private Map<String, TargetGroup> targetGroups;

    @Override
    public void init(Map<String, TargetGroup> targetGroups) {
        this.targetGroups = targetGroups;
    }

    @Override
    public double evaluate(String targetGroupId, PardotData targetingData) {
        if (targetingData == null) {
            return 0.0;
        }
        if (!targetGroups.containsKey(targetGroupId)) {
            return 0.0;
        }
        TargetGroup targetGroup = targetGroups.get(targetGroupId);
        for (String list : targetGroup.getProperties().keySet()) {
            if (targetingData.getLists().contains(list)) {
                return 1.0;
            }
        }
        return 0;
    }
}

