package client.controller;

import com.awesome.business.template.api.View;
import com.awesome.business.template.api.ViewHandler;
import com.awesome.business.template.controller.ViewHandlerImpl;

/**
 * Created by Carlos Couto Cerdeira on 4/3/17.
 */
public class GUI {

    private Controller controller = new Controller(this);
    private ViewHandler handler = new ViewHandlerImpl();

    private View loading;
    private View login;
    private View menu;

    public void init() {
        loading = handler.loadView("client/loading.html");
        login = handler.loadView("client/login.html");
        menu = handler.loadView("client/menu.html");

        load(menu);
    }

    public View getLoading() {
        return loading;
    }

    public View getLogin() {
        return login;
    }

    public View getMenu() {
        return menu;
    }

    public void load(View v) {
        v.addObjectOnJS("controller", controller);
        handler.show(v);
    }

    public Controller getController() {
        return controller;
    }
}
