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

package net.charabia.jsmoothgen.application.gui;

import net.charabia.jsmoothgen.application.*;
import net.charabia.jsmoothgen.skeleton.*;
import java.util.*;

public class SkeletonChooser extends javax.swing.JPanel implements ModelUpdater
{
	private SkeletonList m_list;
	private JSmoothModelBean m_model;
	
	/** Creates new form BeanForm */
	public SkeletonChooser()
	{
		initComponents();
	}
	
	public void setModel(java.io.File basedir, JSmoothModelBean model)
	{
		m_model = model;
		m_comboNames.removeAllItems();
		if ((m_list != null) && (m_model.getSkeletonName() != null))
		{
			m_comboNames.setSelectedItem(m_model.getSkeletonName());
		}
	}
	
	public void setSkeletonList(SkeletonList list)
	{
		m_list = list;
		for (Iterator i=m_list.getIteratorName(); i.hasNext(); )
		{
			String name = (String)i.next();
			m_comboNames.addItem(name);
		}
		if ((m_model != null) && (m_model.getSkeletonName() != null))
		{
			m_comboNames.setSelectedItem(m_model.getSkeletonName());
		}
		validate();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents()//GEN-BEGIN:initComponents
	{
		java.awt.GridBagConstraints gridBagConstraints;
		
		jLabel1 = new javax.swing.JLabel();
		m_comboNames = new javax.swing.JComboBox();
		jLabel2 = new javax.swing.JLabel();
		jScrollPane2 = new javax.swing.JScrollPane();
		m_skeletonDescription = new javax.swing.JTextPane();
		
		setLayout(new java.awt.GridBagLayout());
		
		jLabel1.setText("Executable Skeleton");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
		add(jLabel1, gridBagConstraints);
		
		m_comboNames.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				comboNamesActionPerformed(evt);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		add(m_comboNames, gridBagConstraints);
		
		jLabel2.setText("Description");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		add(jLabel2, gridBagConstraints);
		
		m_skeletonDescription.setEditable(false);
		m_skeletonDescription.setFocusable(false);
		jScrollPane2.setViewportView(m_skeletonDescription);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 0.5;
		add(jScrollPane2, gridBagConstraints);
		
	}//GEN-END:initComponents

	private void comboNamesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_comboNamesActionPerformed
	{//GEN-HEADEREND:event_comboNamesActionPerformed
		// Add your handling code here:
//		System.out.println("COMBO: " + m_comboNames.getSelectedItem().toString());
		String skelname = (String) m_comboNames.getSelectedItem();
		if (skelname == null)
			return;
		
		SkeletonBean skel = m_list.getSkeleton(skelname);
		if (skel != null)
		{
			m_skeletonDescription.setText(skel.getDescription());
		}
	}//GEN-LAST:event_comboNamesActionPerformed

	public void updateModel()
	{
		String skelname = (String) m_comboNames.getSelectedItem();
		SkeletonBean skel = m_list.getSkeleton(skelname);
		if (skel != null)
		{
			System.out.println("Update SkeletonChooser w/ " + skelname);
			m_model.setSkeletonName(skelname);
		}
	}	
		
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JComboBox m_comboNames;
	private javax.swing.JTextPane m_skeletonDescription;
	// End of variables declaration//GEN-END:variables

	
}
