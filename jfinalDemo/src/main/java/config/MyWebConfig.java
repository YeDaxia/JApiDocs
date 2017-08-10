package config;

import com.jfinal.config.*;
import com.jfinal.template.Engine;
import controller.BookController;
import controller.UserController;
import routes.KRoutes;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class MyWebConfig extends JFinalConfig{

    @Override
    public void configConstant(Constants me) {

    }

    @Override
    public void configRoute(Routes me) {
        me.add("/api/v1/user", UserController.class);
        me.add("/api/v1/book", BookController.class);
        me.add(new KRoutes());
    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {

    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }
}
