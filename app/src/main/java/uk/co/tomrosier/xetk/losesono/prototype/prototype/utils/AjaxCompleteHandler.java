package uk.co.tomrosier.xetk.losesono.prototype.prototype.utils;

/**
 * This is used for carrying out asynchronous type action as needed in alot of the REST type transactions we do within the application.
 */
public interface AjaxCompleteHandler{
    public void handleAction(Object someData);
}