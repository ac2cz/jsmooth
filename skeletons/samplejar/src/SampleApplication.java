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

/*
 * SampleApplication.java
 *
 * Created on 3 ao�t 2003, 22:26
 */
import java.awt.*;

/**
 *
 * @author  Rodrigo
 */
public class SampleApplication extends javax.swing.JFrame
{
	
	/** Creates new form SampleApplication */
	public SampleApplication()
	{
		initComponents();
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((d.width-getWidth())/2, (d.height-getHeight())/2);

	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents()//GEN-BEGIN:initComponents
	{
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jPanel2 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("JSmooth Sample");
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosed(java.awt.event.WindowEvent evt)
			{
				formWindowClosed(evt);
			}
			public void windowClosing(java.awt.event.WindowEvent evt)
			{
				exitForm(evt);
			}
		});
		
		jPanel1.setLayout(new java.awt.BorderLayout());
		
		java.net.URL iconurl = getClass().getResource("/gnome-application-x-gzip.png");
		if (iconurl != null)
		    {
			javax.swing.ImageIcon iic = new javax.swing.ImageIcon();
			if (iic != null)
			    jLabel1.setIcon(iic);
		    }

		String jnitest = "";
		if (jsmooth.Native.isAvailable())
		    {
			jnitest = jsmooth.Native.getExecutablePath();
		    }

		jLabel1.setText("Sample Application for JSmooth " + jnitest);
		jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);
		
		jButton1.setText("Exit");
		jButton1.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				jButton1ActionPerformed(evt);
			}
		});
		
		jPanel2.add(jButton1);
		
		jPanel1.add(jPanel2, java.awt.BorderLayout.SOUTH);
		
		getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
		
		pack();
	}//GEN-END:initComponents

	private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
	{//GEN-HEADEREND:event_formWindowClosed
		// Add your handling code here:
		System.exit(0);
	}//GEN-LAST:event_formWindowClosed

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
	{//GEN-HEADEREND:event_jButton1ActionPerformed
		// Add your handling code here:
		System.exit(0);
	}//GEN-LAST:event_jButton1ActionPerformed
	
	/** Exit the Application */
	private void exitForm(java.awt.event.WindowEvent evt)//GEN-FIRST:event_exitForm
	{
		System.exit(0);
	}//GEN-LAST:event_exitForm
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) throws Exception
	{
	    jsmooth.SplashWindow spw = null;

	    //	    throw new Exception("KO");

 	    if (jsmooth.Native.isAvailable())
 		{
 		    spw = jsmooth.Native.getSplashWindow();
		}
	    
	    if (spw != null)
		for (int i=0; i<=50; i+=2)
		    {
			spw.setProgress(i, 50);
			Thread.currentThread().sleep(100);
		    }

	    new SampleApplication().setVisible(true);
	}
	
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	// End of variables declaration//GEN-END:variables
	
}
