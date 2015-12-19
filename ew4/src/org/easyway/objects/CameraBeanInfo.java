/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.objects;

import java.beans.*;

/**
 *
 * @author RemalKoil
 */
public class CameraBeanInfo extends SimpleBeanInfo {

    // Bean descriptor//GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( org.easyway.objects.Camera.class , null ); // NOI18N//GEN-HEADEREND:BeanDescriptor

    // Here you can add code for customizing the BeanDescriptor.

        return beanDescriptor;     }//GEN-LAST:BeanDescriptor


    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_2D = 0;
    private static final int PROPERTY_3D = 1;
    private static final int PROPERTY_backgroundColor = 2;
    private static final int PROPERTY_centered = 3;
    private static final int PROPERTY_collectingObjectsOnScreen = 4;
    private static final int PROPERTY_drawingArea = 5;
    private static final int PROPERTY_height = 6;
    private static final int PROPERTY_heightFactor = 7;
    private static final int PROPERTY_objectsOnScreen = 8;
    private static final int PROPERTY_position = 9;
    private static final int PROPERTY_width = 10;
    private static final int PROPERTY_widthFactor = 11;
    private static final int PROPERTY_x = 12;
    private static final int PROPERTY_y = 13;
    private static final int PROPERTY_z = 14;
    private static final int PROPERTY_zoom2D = 15;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[16];
    
        try {
            properties[PROPERTY_2D] = new PropertyDescriptor ( "2D", org.easyway.objects.Camera.class, "is2D", null ); // NOI18N
            properties[PROPERTY_3D] = new PropertyDescriptor ( "3D", org.easyway.objects.Camera.class, "is3D", null ); // NOI18N
            properties[PROPERTY_backgroundColor] = new PropertyDescriptor ( "backgroundColor", org.easyway.objects.Camera.class, "getBackgroundColor", null ); // NOI18N
            properties[PROPERTY_centered] = new PropertyDescriptor ( "centered", org.easyway.objects.Camera.class, "isCentered", null ); // NOI18N
            properties[PROPERTY_collectingObjectsOnScreen] = new PropertyDescriptor ( "collectingObjectsOnScreen", org.easyway.objects.Camera.class, "isCollectingObjectsOnScreen", "setCollectingObjectsOnScreen" ); // NOI18N
            properties[PROPERTY_drawingArea] = new IndexedPropertyDescriptor ( "drawingArea", org.easyway.objects.Camera.class, null, null, null, "setDrawingArea" ); // NOI18N
            properties[PROPERTY_height] = new PropertyDescriptor ( "height", org.easyway.objects.Camera.class, "getHeight", null ); // NOI18N
            properties[PROPERTY_heightFactor] = new PropertyDescriptor ( "heightFactor", org.easyway.objects.Camera.class, "getHeightFactor", null ); // NOI18N
            properties[PROPERTY_objectsOnScreen] = new PropertyDescriptor ( "objectsOnScreen", org.easyway.objects.Camera.class, "getObjectsOnScreen", "setObjectsOnScreen" ); // NOI18N
            properties[PROPERTY_position] = new PropertyDescriptor ( "position", org.easyway.objects.Camera.class, "getPosition", null ); // NOI18N
            properties[PROPERTY_width] = new PropertyDescriptor ( "width", org.easyway.objects.Camera.class, "getWidth", null ); // NOI18N
            properties[PROPERTY_widthFactor] = new PropertyDescriptor ( "widthFactor", org.easyway.objects.Camera.class, "getWidthFactor", null ); // NOI18N
            properties[PROPERTY_x] = new PropertyDescriptor ( "x", org.easyway.objects.Camera.class, "getx", "setx" ); // NOI18N
            properties[PROPERTY_y] = new PropertyDescriptor ( "y", org.easyway.objects.Camera.class, "gety", "sety" ); // NOI18N
            properties[PROPERTY_z] = new PropertyDescriptor ( "z", org.easyway.objects.Camera.class, "getz", "setz" ); // NOI18N
            properties[PROPERTY_zoom2D] = new PropertyDescriptor ( "zoom2D", org.easyway.objects.Camera.class, "getZoom2D", "setZoom2D" ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Properties

    // Here you can add code for customizing the properties array.

        return properties;     }//GEN-LAST:Properties

    // EventSet identifiers//GEN-FIRST:Events

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[0];//GEN-HEADEREND:Events

    // Here you can add code for customizing the event sets array.

        return eventSets;     }//GEN-LAST:Events

    // Method identifiers//GEN-FIRST:Methods
    private static final int METHOD_addObjectsOnScreen0 = 0;
    private static final int METHOD_center1 = 1;
    private static final int METHOD_centerOn2 = 2;
    private static final int METHOD_centerOn3 = 3;
    private static final int METHOD_centerOn4 = 4;
    private static final int METHOD_centerOn5 = 5;
    private static final int METHOD_clearCollectedObjectsOnScreen6 = 6;
    private static final int METHOD_getMainFather7 = 7;
    private static final int METHOD_incZoom2D8 = 8;
    private static final int METHOD_initGL9 = 9;
    private static final int METHOD_move10 = 10;
    private static final int METHOD_move11 = 11;
    private static final int METHOD_moveX12 = 12;
    private static final int METHOD_moveY13 = 13;
    private static final int METHOD_moveZ14 = 14;
    private static final int METHOD_rebind15 = 15;
    private static final int METHOD_rotate16 = 16;
    private static final int METHOD_rotateReset17 = 17;
    private static final int METHOD_rotateX18 = 18;
    private static final int METHOD_rotateY19 = 19;
    private static final int METHOD_rotateZ20 = 20;
    private static final int METHOD_set2D21 = 21;
    private static final int METHOD_set3D22 = 22;
    private static final int METHOD_setBackgroundColor23 = 23;
    private static final int METHOD_setClearColor24 = 24;

    // Method array 
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        MethodDescriptor[] methods = new MethodDescriptor[25];
    
        try {
            methods[METHOD_addObjectsOnScreen0] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("addObjectsOnScreen", new Class[] {org.easyway.interfaces.sprites.IPlain2D.class})); // NOI18N
            methods[METHOD_addObjectsOnScreen0].setDisplayName ( "" );
            methods[METHOD_center1] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("center", new Class[] {})); // NOI18N
            methods[METHOD_center1].setDisplayName ( "" );
            methods[METHOD_centerOn2] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("centerOn", new Class[] {org.easyway.interfaces.sprites.IPlain2D.class})); // NOI18N
            methods[METHOD_centerOn2].setDisplayName ( "" );
            methods[METHOD_centerOn3] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("centerOn", new Class[] {org.easyway.interfaces.sprites.IPlain2D.class, boolean.class})); // NOI18N
            methods[METHOD_centerOn3].setDisplayName ( "" );
            methods[METHOD_centerOn4] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("centerOn", new Class[] {org.easyway.interfaces.sprites.IPlain2D.class, double.class, double.class})); // NOI18N
            methods[METHOD_centerOn4].setDisplayName ( "" );
            methods[METHOD_centerOn5] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("centerOn", new Class[] {org.easyway.interfaces.sprites.IPlain2D.class, double.class, double.class, boolean.class})); // NOI18N
            methods[METHOD_centerOn5].setDisplayName ( "" );
            methods[METHOD_clearCollectedObjectsOnScreen6] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("clearCollectedObjectsOnScreen", new Class[] {})); // NOI18N
            methods[METHOD_clearCollectedObjectsOnScreen6].setDisplayName ( "" );
            methods[METHOD_getMainFather7] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("getMainFather", new Class[] {})); // NOI18N
            methods[METHOD_getMainFather7].setDisplayName ( "" );
            methods[METHOD_incZoom2D8] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("incZoom2D", new Class[] {float.class})); // NOI18N
            methods[METHOD_incZoom2D8].setDisplayName ( "" );
            methods[METHOD_initGL9] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("initGL", new Class[] {})); // NOI18N
            methods[METHOD_initGL9].setDisplayName ( "" );
            methods[METHOD_move10] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("move", new Class[] {double.class, double.class})); // NOI18N
            methods[METHOD_move10].setDisplayName ( "" );
            methods[METHOD_move11] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("move", new Class[] {float.class, float.class, float.class})); // NOI18N
            methods[METHOD_move11].setDisplayName ( "" );
            methods[METHOD_moveX12] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("moveX", new Class[] {float.class})); // NOI18N
            methods[METHOD_moveX12].setDisplayName ( "" );
            methods[METHOD_moveY13] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("moveY", new Class[] {float.class})); // NOI18N
            methods[METHOD_moveY13].setDisplayName ( "" );
            methods[METHOD_moveZ14] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("moveZ", new Class[] {float.class})); // NOI18N
            methods[METHOD_moveZ14].setDisplayName ( "" );
            methods[METHOD_rebind15] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("rebind", new Class[] {})); // NOI18N
            methods[METHOD_rebind15].setDisplayName ( "" );
            methods[METHOD_rotate16] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("rotate", new Class[] {float.class, org.lwjgl.util.vector.Vector3f.class})); // NOI18N
            methods[METHOD_rotate16].setDisplayName ( "" );
            methods[METHOD_rotateReset17] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("rotateReset", new Class[] {})); // NOI18N
            methods[METHOD_rotateReset17].setDisplayName ( "" );
            methods[METHOD_rotateX18] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("rotateX", new Class[] {float.class})); // NOI18N
            methods[METHOD_rotateX18].setDisplayName ( "" );
            methods[METHOD_rotateY19] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("rotateY", new Class[] {float.class})); // NOI18N
            methods[METHOD_rotateY19].setDisplayName ( "" );
            methods[METHOD_rotateZ20] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("rotateZ", new Class[] {float.class})); // NOI18N
            methods[METHOD_rotateZ20].setDisplayName ( "" );
            methods[METHOD_set2D21] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("set2D", new Class[] {})); // NOI18N
            methods[METHOD_set2D21].setDisplayName ( "" );
            methods[METHOD_set3D22] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("set3D", new Class[] {})); // NOI18N
            methods[METHOD_set3D22].setDisplayName ( "" );
            methods[METHOD_setBackgroundColor23] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("setBackgroundColor", new Class[] {int.class, int.class, int.class})); // NOI18N
            methods[METHOD_setBackgroundColor23].setDisplayName ( "" );
            methods[METHOD_setClearColor24] = new MethodDescriptor(org.easyway.objects.Camera.class.getMethod("setClearColor", new Class[] {float.class, float.class, float.class, float.class})); // NOI18N
            methods[METHOD_setClearColor24].setDisplayName ( "" );
        }
        catch( Exception e) {}//GEN-HEADEREND:Methods

    // Here you can add code for customizing the methods array.
    
        return methods;     }//GEN-LAST:Methods

    private static java.awt.Image iconColor16 = null;//GEN-BEGIN:IconsDef
    private static java.awt.Image iconColor32 = null;
    private static java.awt.Image iconMono16 = null;
    private static java.awt.Image iconMono32 = null;//GEN-END:IconsDef
    private static String iconNameC16 = null;//GEN-BEGIN:Icons
    private static String iconNameC32 = null;
    private static String iconNameM16 = null;
    private static String iconNameM32 = null;//GEN-END:Icons

    private static final int defaultPropertyIndex = -1;//GEN-BEGIN:Idx
    private static final int defaultEventIndex = -1;//GEN-END:Idx

    
//GEN-FIRST:Superclass

    // Here you can add code for customizing the Superclass BeanInfo.

//GEN-LAST:Superclass
	
    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     * 
     * @return BeanDescriptor describing the editable
     * properties of this bean.  May return null if the
     * information should be obtained by automatic analysis.
     */
    public BeanDescriptor getBeanDescriptor() {
	return getBdescriptor();
    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     * 
     * @return An array of PropertyDescriptors describing the editable
     * properties supported by this bean.  May return null if the
     * information should be obtained by automatic analysis.
     * <p>
     * If a property is indexed, then its entry in the result array will
     * belong to the IndexedPropertyDescriptor subclass of PropertyDescriptor.
     * A client of getPropertyDescriptors can use "instanceof" to check
     * if a given PropertyDescriptor is an IndexedPropertyDescriptor.
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
	return getPdescriptor();
    }

    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     * 
     * @return  An array of EventSetDescriptors describing the kinds of 
     * events fired by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public EventSetDescriptor[] getEventSetDescriptors() {
	return getEdescriptor();
    }

    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     * 
     * @return  An array of MethodDescriptors describing the methods 
     * implemented by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public MethodDescriptor[] getMethodDescriptors() {
	return getMdescriptor();
    }

    /**
     * A bean may have a "default" property that is the property that will
     * mostly commonly be initially chosen for update by human's who are 
     * customizing the bean.
     * @return  Index of default property in the PropertyDescriptor array
     * 		returned by getPropertyDescriptors.
     * <P>	Returns -1 if there is no default property.
     */
    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }

    /**
     * A bean may have a "default" event that is the event that will
     * mostly commonly be used by human's when using the bean. 
     * @return Index of default event in the EventSetDescriptor array
     *		returned by getEventSetDescriptors.
     * <P>	Returns -1 if there is no default event.
     */
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }

    /**
     * This method returns an image object that can be used to
     * represent the bean in toolboxes, toolbars, etc.   Icon images
     * will typically be GIFs, but may in future include other formats.
     * <p>
     * Beans aren't required to provide icons and may return null from
     * this method.
     * <p>
     * There are four possible flavors of icons (16x16 color,
     * 32x32 color, 16x16 mono, 32x32 mono).  If a bean choses to only
     * support a single icon we recommend supporting 16x16 color.
     * <p>
     * We recommend that icons have a "transparent" background
     * so they can be rendered onto an existing background.
     *
     * @param  iconKind  The kind of icon requested.  This should be
     *    one of the constant values ICON_COLOR_16x16, ICON_COLOR_32x32, 
     *    ICON_MONO_16x16, or ICON_MONO_32x32.
     * @return  An image object representing the requested icon.  May
     *    return null if no suitable icon is available.
     */
    public java.awt.Image getIcon(int iconKind) {
        switch ( iconKind ) {
        case ICON_COLOR_16x16:
            if ( iconNameC16 == null )
                return null;
            else {
                if( iconColor16 == null )
                    iconColor16 = loadImage( iconNameC16 );
                return iconColor16;
            }
        case ICON_COLOR_32x32:
            if ( iconNameC32 == null )
                return null;
            else {
                if( iconColor32 == null )
                    iconColor32 = loadImage( iconNameC32 );
                return iconColor32;
            }
        case ICON_MONO_16x16:
            if ( iconNameM16 == null )
                return null;
            else {
                if( iconMono16 == null )
                    iconMono16 = loadImage( iconNameM16 );
                return iconMono16;
            }
        case ICON_MONO_32x32:
            if ( iconNameM32 == null )
                return null;
            else {
                if( iconMono32 == null )
                    iconMono32 = loadImage( iconNameM32 );
                return iconMono32;
            }
	default: return null;
        }
    }

}

