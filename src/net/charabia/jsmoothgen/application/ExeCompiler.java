/*
  JSmooth: a VM wrapper toolkit for Windows
  Copyright (C) 2003 Rodrigo Reyes <reyes@charabia.net>
 
  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.
 
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
 
  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 
 */

package net.charabia.jsmoothgen.application;

import net.charabia.jsmoothgen.skeleton.*;
import net.charabia.jsmoothgen.pe.*;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class ExeCompiler
{
	private java.util.Vector m_errors = new java.util.Vector();
	private Vector m_listeners = new Vector();
	
	public interface StepListener
	{
		public void setNewState(int percentComplete, String state);
		public void failed();
		public void complete();
	}
	
	public void addListener(ExeCompiler.StepListener listener)
	{
		m_listeners.add(listener);
		
	}
	
	public void cleanErrors()
	{
		m_errors.removeAllElements();
	}
	
	public java.util.Vector getErrors()
	{
		return m_errors;
	}
	
	public class CompilerRunner implements Runnable
	{
		private File m_skelroot;
		private SkeletonBean m_skel;
		private JSmoothModelBean m_data;
		private File m_out;
		
		public CompilerRunner(File skelroot, SkeletonBean skel, JSmoothModelBean data, File out)
		{
			m_skelroot = skelroot;
			m_skel = skel;
			m_data = data;
			m_out = out;
		}
		
		public void run()
		{
			try
			{
				compile(m_skelroot, m_skel, m_data, m_out);
			} catch (Exception exc)
			{
				exc.printStackTrace();
			}
		}
	}
	
	public ExeCompiler.CompilerRunner getRunnable(File skelroot, SkeletonBean skel, JSmoothModelBean data, File out)
	{
		return new CompilerRunner(skelroot, skel, data, out);
	}
	
	public void compileAsync(File skelroot, SkeletonBean skel, JSmoothModelBean data, File out)
	{
		Thread t = new Thread(new CompilerRunner(skelroot, skel, data, out));
		t.start();
	}
	
	public boolean compile(File skelroot, SkeletonBean skel, JSmoothModelBean data, File out) throws Exception
	{
		try
		{
			File basedir = new File(data.getBaseDir());
			
			fireStepChange(0, "Starting compilation");
			
			File pattern = new File(skelroot, skel.getExecutableName());
			if (pattern.exists() == false)
			{
				m_errors.add("Error: Can't find any skeleton at " + skelroot);
				fireFailedChange();
				return false;
			}
			
			fireStepChange(10, "Scanning skeleton...");
			PEFile pe = new PEFile(pattern);
			pe.open();
			
			if (data.getJarLocation() == null)
			{
				m_errors.add("Error: Jar is not specified!");
				fireFailedChange();
				return false;
			}
			
			fireStepChange(40, "Loading Jar...");
			File jarloc = concFile(basedir, new File(data.getJarLocation()));
			if (jarloc.exists() == false)
			{
				m_errors.add("Error: Can't find jar at " + jarloc);
				fireFailedChange();
				return false;
			}
			ByteBuffer jardata = load(jarloc);
			
			fireStepChange(60, "Adding Jar to Resources...");
			PEResourceDirectory resdir = pe.getResourceDirectory();
			boolean resb = resdir.replaceResource(skel.getResourceCategory(), skel.getResourceJarId(), 1033, jardata);
			if (resb == false)
			{
				m_errors.add("Error: Can't replace jar resource! It is probably missing from the skeleton.");
				fireFailedChange();
				return false;
			}
			
			fireStepChange(70, "Adding Properties to Resources...");
			String props = PropertiesBuilder.makeProperties(data);
			ByteBuffer propdata = convert(props);
			resb = resdir.replaceResource(skel.getResourceCategory(), skel.getResourcePropsId(), 1033, propdata);
			
			fireStepChange(80, "Saving exe...");
			pe.dumpTo(out);
			
			System.out.println("PROPERTIES:\n" + props);
			
			fireCompleteChange();
			return true;
		} catch (Exception exc)
		{
			m_errors.add("Error: " + exc.getMessage());
			fireFailedChange();
			return false;
		}
	}
	
	private ByteBuffer load(File in) throws Exception
	{
		FileInputStream fis = new FileInputStream(in);
		ByteBuffer data = ByteBuffer.allocate((int)in.length());
		data.order(ByteOrder.LITTLE_ENDIAN);
		FileChannel fischan = fis.getChannel();
		fischan.read(data);
		data.position(0);
		fis.close();
		
		return data;
	}
	
	private ByteBuffer convert(String data)
	{
		ByteBuffer result = ByteBuffer.allocate(data.length()+1);
		result.position(0);
		
		for (int i=0; i<data.length(); i++)
		{
			result.put((byte)data.charAt(i));
		}
		result.put((byte)0);
		
		result.position(0);
		return result;
	}
	
	static public File concFile(File root, File name)
	{
		if (name.isAbsolute())
			return name;
		
		return new File(root, name.toString());
	}
	
	public void fireStepChange(int percentComplete, String state)
	{
		for (Iterator i=m_listeners.iterator(); i.hasNext(); )
		{
			ExeCompiler.StepListener l = (ExeCompiler.StepListener)i.next();
			l.setNewState(percentComplete, state);
		}
	}
	
	public void fireFailedChange()
	{
		for (Iterator i=m_listeners.iterator(); i.hasNext(); )
		{
			ExeCompiler.StepListener l = (ExeCompiler.StepListener)i.next();
			l.failed();
		}
	}
	public void fireCompleteChange()
	{
		for (Iterator i=m_listeners.iterator(); i.hasNext(); )
		{
			ExeCompiler.StepListener l = (ExeCompiler.StepListener)i.next();
			l.complete();
		}
	}
	
}