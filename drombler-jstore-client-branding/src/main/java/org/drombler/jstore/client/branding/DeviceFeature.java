package org.drombler.jstore.client.branding;

public @interface DeviceFeature {
    /**
     * The text to be displayed, e.g. as the text for menu items or the tooltip for toolbar buttons. If the value starts
     * with '%' the rest of the value is interpreted as a property key and the value gets looked-up in the
     * Bundle.properties file (or a locale specific derivation of this file), which has to be in the same package as the
     * annotated action.
     *
     * @return the text to be displayed for this action
     */
    String displayName();

    /**
     * The position to order the menus in a parent menu/ menu bar. It's a best practice to leave out some positions
     * between entries to allow other bundles to register entries between some existing ones.
     *
     * @return the position to order the menus in a parent menu/ menu bar
     */
    int position();
}
