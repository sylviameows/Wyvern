package net.sylviameows.wyvern.api.task;

public enum TaskTypes implements TaskType {
    FAKE("fake"),
    REAL("real"),
    NONE(null);

    private final String alias;

    TaskTypes(String alias) {
        this.alias = alias;
    }

    public String alias() {
        return alias;
    }
}
