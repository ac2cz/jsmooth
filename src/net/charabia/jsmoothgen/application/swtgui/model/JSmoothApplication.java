/*
 * Created on May 14, 2004
 */
package net.charabia.jsmoothgen.application.swtgui.model;

import java.io.File;
import java.io.IOException;
import java.util.Observable;

import net.charabia.jsmoothgen.application.JSmoothModelBean;
import net.charabia.jsmoothgen.application.JSmoothModelPersistency;
import net.charabia.jsmoothgen.skeleton.SkeletonProperty;


public class JSmoothApplication extends Observable {
        
        private boolean dirty = false;
        
	private JSmoothModelBean jsMdlBean;
	private File prjFile;
	private SkeletonModel skelMdl;
	
	public JSmoothApplication() {
                skelMdl = new SkeletonModel(this);
	}
	
	public boolean compile() {
		jsMdlBean.normalizePaths(prjFile.getParentFile());
                return true;
	}
        
	public String getCurrentDirectory() {
		String dir = jsMdlBean.getCurrentDirectory();
		return dir == null ? "" : dir;
	}
        
	private String getDefaultSkeletonName() {
		// We want to return the "Window Wrapper" skeleton.
		// Due to the fact that we cannot identify the skeletons :(,
		// we'll have to make a hard guess.
		// It seems like the "Window Wrapper" is the second in the
		// list. In case it is not , we catch the exception,
		// and return the first skeleton in the list.
		String[] skelNames = skelMdl.getSkeletonNames();
		try {
			return skelNames[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			return skelNames[0];
		}
	}
	
	public String getExecutable() {
		String exec = jsMdlBean.getExecutableName();
		return exec == null ? "" : exec;
	}
	
	public String getIcon() {
		return jsMdlBean.getIconLocation();
		
		// The default "eclipse" icon:
		// JSmoothResources.class.getResource("eclipse.gif").getPath();
	}
	
	public String getProjectName() {
                if(prjFile == null) {
                        return "[NO PROJECT]";
                }
		return prjFile.getName();
	}
	
	public String getPropertyValue(String id) {
		JSmoothModelBean.Property[] props = jsMdlBean.getSkeletonProperties();
		for (int i = 0; i < props.length; i++) {
			if(id.equals(props[i].Key)) {
				return props[i].getValue();
			}
		}
		return null;
	}
	
	public SkeletonModel getSkeletonModel() {
		return skelMdl;
	}
	
	public String getSkeletonName() {
                if(jsMdlBean == null) {
                        return "";
                }
		return jsMdlBean.getSkeletonName();
	}
        
        public boolean hasProjectFile() {
                return prjFile != null;
        }
	
        public boolean isDirty() {
                return dirty;
        }
        
        public void loadProject(String path) {
                if(path == null) {
                        dirty = false;
                        prjFile = null;
                        jsMdlBean = null;
                        
                        setChanged();
                        notifyObservers(UpdateMessage.MSGUPD_NULL_PROJECT);
                        return;
                } 
        }
        
	public void newProject(String path) {
                setProject(path);
                
                System.out.println("[DEBUG] New project file: " + prjFile.getAbsolutePath());
                
                // Create a new default JSmoothModelBean
                jsMdlBean = new JSmoothModelBean();
                jsMdlBean.setSkeletonName(getDefaultSkeletonName());
                setProperties(skelMdl.getProperties());
                //
                
                // Set the dirty flag
                dirty = true;
                
                setChanged();
                notifyObservers(UpdateMessage.MSGUPD_NEW_PROJECT);
	}

        public boolean saveProject() {
                System.out.println("[DEBUG] Saving project to file: " + prjFile.getAbsolutePath());
                try {
                        JSmoothModelPersistency.save(prjFile, jsMdlBean);
                } catch (IOException e) {
                        System.out.println("[ERROR] Project save failed: " + e.getMessage());
                        return false;
                }
                
                // Reset the dirty flag
                dirty = false;
                
                setChanged();
                notifyObservers(UpdateMessage.MSGUPD_SAVED_PROJECT);
		return true;
	}
        
	public void setCurrentDirectory(String dir) {
		System.out.println("[DEBUG] Setting current dir: " + dir);
		jsMdlBean.setCurrentDirectory(dir);
	}
	
	public void setExecutable(String file) {
		System.out.println("[DEBUG] Setting executable file: " + file);
		jsMdlBean.setExecutableName(file);
	}

	public void setIcon(String file) {
		System.out.println("[DEBUG] Setting icon file: " + file);
		jsMdlBean.setIconLocation(file);
	}
	
        public void setProject(String path) {
                prjFile = new File(path);
        }
	
	private void setProperties(SkeletonProperty[] props) {
		// Transfer the SkeletonProperty data to JSmoothModelBean.Property
		JSmoothModelBean.Property[] jsBeanProps = new JSmoothModelBean.Property[props.length];
		String sysoutProps = "";
		for (int i = 0; i < props.length; i++) {
			jsBeanProps[i] = new JSmoothModelBean.Property();
			jsBeanProps[i].setKey(props[i].getIdName());
			jsBeanProps[i].setValue(props[i].getValue());
			
			sysoutProps += "(" + props[i].getIdName() + ", \"" + props[i].getValue() + "\") ";
		}
		//
		
		System.out.println("[DEBUG] Setting skeleton props: " + sysoutProps);
		jsMdlBean.setSkeletonProperties(jsBeanProps);
	}
	
	public void setPropertyValue(String id, String value) {
		JSmoothModelBean.Property[] props = jsMdlBean.getSkeletonProperties();
		for (int i = 0; i < props.length; i++) {
			if(id.equals(props[i].Key)) {
				props[i].setValue(value);
			}
		}
	}
	
	public void setSkeletonName(String name) {
		System.out.println("[DEBUG] Setting skeleton name: " + name);
		jsMdlBean.setSkeletonName(name);
		setProperties(skelMdl.getProperties());
	}
        
}