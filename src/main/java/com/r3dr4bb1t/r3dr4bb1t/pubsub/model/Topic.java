package com.r3dr4bb1t.merpay.pubsub.model;

import javax.validation.constraints.NotEmpty;

public class Topic {

    @NotEmpty
    private String name;

    public Topic(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return 30 * name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Topic))
            return false;

        Topic t = (Topic) obj;
        return name.equals(t.name);
    }
}
