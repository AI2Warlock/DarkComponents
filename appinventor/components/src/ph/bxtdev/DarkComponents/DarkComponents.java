package ph.bxtdev.DarkComponents;

import android.app.Activity;
import android.content.Context;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import gnu.lists.LList;
import gnu.mapping.Environment;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Location;
import gnu.mapping.LocationEnumeration;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@DesignerComponent(
        version = 1,
        description = "Set components to light/dark mode without adjusting view.",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "aiwebres/icon.png")

@SimpleObject(external = true)
//Libraries
@UsesLibraries(libraries = "")
//Permissions
@UsesPermissions(permissionNames = "")

public class DarkComponents extends AndroidNonvisibleComponent {

    //Activity and Context
    private Context context;
    private Activity activity;

    public DarkComponents(ComponentContainer container){
        super(container.$form());
        this.activity = container.$context();
        this.context = container.$context();
    }

    @SimpleFunction(description = "Sets components to light mode")
    public void SetComponentLightMode(){         
            Map<String, AndroidViewComponent> components;
            if (form instanceof ReplForm) {
               components = mapComponentsRepl();
            } else {
               components = mapComponents();
            }

            for(Map.Entry<String, AndroidViewComponent> entry : components.entrySet()){
                    AndroidViewComponent component = entry.getValue();
                    android.view.View view = component.getView();
                    view.setBackgroundColor(Component.COLOR_WHITE);
                    if(view instanceof android.widget.TextView){
                        android.widget.TextView textView = (android.widget.TextView) view;
                        textView.setTextColor(Component.COLOR_BLACK);
                    }
            }
    }

    @SimpleFunction(description = "Sets components to dark mode")
    public void SetComponentDarkMode(){         
            Map<String, AndroidViewComponent> components;
            if (form instanceof ReplForm) {
               components = mapComponentsRepl();
            } else {
               components = mapComponents();
            }

            for(Map.Entry<String, AndroidViewComponent> entry : components.entrySet()){
                    AndroidViewComponent component = entry.getValue();
                    android.view.View view = component.getView();
                    view.setBackgroundColor(Component.COLOR_BLACK);
                    if(view instanceof android.widget.TextView){
                        android.widget.TextView textView = (android.widget.TextView) view;
                        textView.setTextColor(Component.COLOR_WHITE);
                    }
            }
    }


    private Map<String, AndroidViewComponent> mapComponents() throws NoSuchFieldException, IllegalAccessException {
       Map<String, AndroidViewComponent> components = new HashMap<>();
       Field componentsField = form.getClass().getField("components$Mnto$Mncreate");
       LList listComponents = (LList) componentsField.get(form);
       for (Object component : listComponents) {
         LList lList = (LList) component;
         SimpleSymbol symbol = (SimpleSymbol) lList.get(2);
         String componentName = symbol.getName();
         Object value = form.getClass().getField(componentName).get(form);
         if (value instanceof AndroidViewComponent) {
            components.put(componentName, (AndroidViewComponent) value);
         }    
     } 
      return components;
  }

   private Map<String, AndroidViewComponent> mapComponentsRepl() throws NoSuchFieldException, IllegalAccessException {
    Map<String, AndroidViewComponent> components = new HashMap<>();
    Field field = form.getClass().getField("form$Mnenvironment");
    Environment environment = (Environment) field.get(form);
    LocationEnumeration locationEnumeration = environment.enumerateAllLocations();
    while (locationEnumeration.hasMoreElements()) {
      Location location = locationEnumeration.next();
      String componentName = location.getKeySymbol().getName();
      Object value = location.getValue();
      if (value instanceof AndroidViewComponent) {
        components.put(componentName, (AndroidViewComponent) value);
      }
    }
    return components;
  }
} 
