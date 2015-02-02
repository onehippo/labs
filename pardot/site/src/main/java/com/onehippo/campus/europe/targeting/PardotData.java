package com.onehippo.campus.europe.targeting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.onehippo.cms7.targeting.data.AbstractTargetingData;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class PardotData extends AbstractTargetingData {
    private final String prospectId;
    private final String firstName;
    private final String lastName;
    private final List<String> lists;

    @JsonCreator
    public PardotData(@JsonProperty("collectorId") String collectorId,
                      @JsonProperty("prospectId") String prospectId,
                      @JsonProperty("firstName") String firstName,
                      @JsonProperty("lastName") String lastName,
                      @JsonProperty("lists") List<String> lists) {
        super(collectorId);
        this.prospectId = prospectId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lists = lists;
    }

    public String getProspectId() {
        return prospectId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getLists() {
        return lists;
    }
}

