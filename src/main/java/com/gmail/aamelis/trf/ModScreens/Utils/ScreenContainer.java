package com.gmail.aamelis.trf.ModScreens.Utils;

import net.minecraft.client.gui.components.AbstractWidget;

import java.util.ArrayList;
import java.util.List;

public class ScreenContainer {

    private String name;
    private List<AbstractWidget> widgets;

    public ScreenContainer(String name) {
        this.name = name;
    }

    public ScreenContainer(String name, ArrayList<AbstractWidget> widgets) {
        this.name = name;
        this.widgets = widgets;
    }

    public String getContainerName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AbstractWidget> getWidgets() {
        return widgets;
    }

    public void addWidget(AbstractWidget widget) {
        this.widgets.add(widget);
    }

    public void setWidgets(List<AbstractWidget> widgets) {
        this.widgets = widgets;
    }

}
