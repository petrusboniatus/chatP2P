package client.controller;

import com.awesome.business.template.api.View;
import com.awesome.business.template.api.ViewHandler;
import com.awesome.business.template.controller.ViewHandlerImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cout970 on 4/5/17.
 */
public enum ViewState {
    LOADING("client/view/loading.html"),
    LOGIN("client/view/login.html"),
    CHAT("client/view/chat.html"),
    PROFILE("client/view/profile.html");

    private static ViewHandler handler = new ViewHandlerImpl();
    private static Map<String, View> cache = new HashMap<>();
    private static ViewState currentView;

    private String path;

    ViewState(String path) {
        this.path = path;
    }

    public static ViewState getCurrentView() {
        return currentView;
    }

    public void load(Controller ctrl) {
        View view = getView();
        System.out.println("cargando vista: " + view.getHtmlUrl());
        view.addObjectOnJS("controller", ctrl);
        currentView = this;
        handler.show(view);
    }

    public View getView() {
        if (!cache.containsKey(path)) {
            View view = handler.loadView(path);
            cache.put(path, view);
        }
        return cache.get(path);
    }
}
