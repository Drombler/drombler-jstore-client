package org.drombler.jstore.client.branding;

public @interface ApplicationFeature {

    String actionId();

    /**
     * The position to order the menus in a parent menu/ menu bar. It's a best practice to leave out some positions
     * between entries to allow other bundles to register entries between some existing ones.
     *
     * @return the position to order the menus in a parent menu/ menu bar
     */
    int position();

//    boolean selected() default false;
}
