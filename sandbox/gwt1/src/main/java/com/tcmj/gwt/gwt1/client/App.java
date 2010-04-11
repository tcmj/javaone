package com.tcmj.gwt.gwt1.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class App implements EntryPoint {

    public void onModuleLoad() {
        Button button = new Button("Click me", new ClickHandler() {

            public void onClick(ClickEvent event) {
                Window.alert("Hello, AJAX");
            }
        });
        RootPanel.get().add(button);
    }
}
