package org.sss.micro.swt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;

class FileTreeLabelProvider implements ILabelProvider {
  private List listeners;
  private Image file;
  private Image dir;

  public FileTreeLabelProvider() {
    listeners = new ArrayList();

    try {
      file = new Image(null, new FileInputStream("images/file.gif"));
      dir = new Image(null, new FileInputStream("images/directory.gif"));
    } catch (FileNotFoundException e) {
      // Swallow it; we'll do without images
    }
  }

  public Image getImage(Object arg0) {
    return ((File) arg0).isDirectory() ? dir : file;
  }

  public String getText(Object arg0) {
    String text = ((File) arg0).getName();

    if (text.length() == 0) {
      text = ((File) arg0).getPath();
    }
	return text;
  }

  public void addListener(ILabelProviderListener arg0) {
    listeners.add(arg0);
  }

  public void dispose() {
    // Dispose the images
    if (dir != null)
      dir.dispose();
    if (file != null)
      file.dispose();
  }

  public boolean isLabelProperty(Object arg0, String arg1) {
    return false;
  }

  public void removeListener(ILabelProviderListener arg0) {
    listeners.remove(arg0);
  }
}
           