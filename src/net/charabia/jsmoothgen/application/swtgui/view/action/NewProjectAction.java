/*
 * Created on Jun 12, 2004
 */
package net.charabia.jsmoothgen.application.swtgui.view.action;

import java.util.Observable;

import net.charabia.jsmoothgen.application.swtgui.model.JSmoothApplication;
import net.charabia.jsmoothgen.application.swtgui.view.JSmoothWindow;
import net.charabia.jsmoothgen.application.swtgui.view.resources.JSmoothResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;


public class NewProjectAction extends JSmoothAction {
        
        public NewProjectAction(JSmoothWindow jsWnd, JSmoothApplication jsApp) {
                super(jsWnd, jsApp);
                
                setId(ACTION_NEW_PROJECT);
                setText("New...");
                setImageDescriptor(getDescriptor(JSmoothResources.IMG_NEW_PROJECT));
        }
        
        public void run() {
                FileDialog dlg = new FileDialog(getJSmoothWindow().getShell(), SWT.SAVE);
                dlg.setText("Project File");
                String path = dlg.open();
                        
                // Means "CANCEL"
                if(path == null) return;
                
                getJSmoothApplication().newProject(path);
                getJSmoothApplication().saveProject();
        }

        public void update(Observable o, Object arg) {
                // Do nothing
        }

}