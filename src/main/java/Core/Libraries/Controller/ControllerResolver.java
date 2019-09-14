package Core.Libraries.Controller;

import Core.JsonSerializer;
import Core.Libraries.Http.HttpResponseObject;
import spark.Request;
import spark.Response;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControllerResolver {

    private static final String CONTROLLER_PACKAGE = "Core.Controllers.";
    private static final String CONTROLLER_BASE_CLASS  = CONTROLLER_PACKAGE+"ApiController";

    private Class _parent;
    private Class _child;

    private Object _childInstance;

    private Field _request;

    private Field _response;

    /**
     * Method finds the controller argument and instantiates its superclass.
     * @param controller
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     */
    public ControllerResolver find(String controller) throws ClassNotFoundException, InstantiationException {
        this._child = Class.forName(CONTROLLER_PACKAGE+controller);
        this._parent = this._child.getSuperclass();

        if(this._parent == null) {
            throw new InstantiationException(CONTROLLER_PACKAGE+controller + " is not a child of "+CONTROLLER_BASE_CLASS);
        }

        return this;
    }

    /**
     * Method uses the instance of the instantiated controller to find the method
     * to invoke. upon invoking this method, the request and the response of the base
     * class is set. Not, this method will not invoke static controller methods. We
     * want to eliminate it as best as possible. -- YES, we ignore what the documentation says --
     * @param method
     * @param request
     * @param response
     * @return
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public Object invoke(String method, Request request, Response response) throws InstantiationException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        if(this._child == null) {
            throw new InstantiationException("Controller have not been instantiated");
        }
        Method methodToInvoke = this._child.getMethod(method);
        methodToInvoke.setAccessible(true);
        this._childInstance = this._child.getDeclaredConstructor().newInstance();
        this._setBaseFields(request,response);
        Object returnValue = methodToInvoke.invoke(this._childInstance);
        return this._getResponse(returnValue);
    }

    /**
     * Sets the Request and Response object of the Base class
     * @param req
     * @param res
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void _setBaseFields(Request req, Response res) throws NoSuchFieldException, IllegalAccessException {
        this._request = this._parent.getField("request");
        this._response = this._parent.getField("response");
        this._request.set(this._childInstance,req);
        this._response.set(this._childInstance,res);
    }

    private String _getResponse(Object returnValue) throws IllegalAccessException {
        HttpResponseObject response = new HttpResponseObject();
        response.result = returnValue;
        response.status = ((Response) this._response.get(this._childInstance)).status();
        return JsonSerializer.toJson(response);
    }
}
