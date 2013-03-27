package org.sss.micro.swt

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

class ComboBoxDialog{
  private Shell dialog
  private Shell parent
  private Combo combo
  List data = []
  int status = SWT.CANCEL
  String value = null
  
  ComboBoxDialog(Shell parent, String title, String msg){
    this.parent = parent
    dialog = new Shell(parent, SWT.APPLICATION_MODAL| SWT.DIALOG_TRIM)
    dialog.setText(title);
    dialog.setSize(300, 150);
    dialog.setLayout(new GridLayout(2, false));

    new Label(dialog, SWT.NULL).setText(msg)
    combo = new Combo(dialog, SWT.READ_ONLY);

   final Button okButton = new Button(dialog, SWT.PUSH);
    okButton.setBounds(20, 35, 40, 25);
    okButton.setText("OK");

    Button cancelButton = new Button(dialog, SWT.PUSH);
    cancelButton.setBounds(70, 35, 40, 25);
    cancelButton.setText("Cancel");

    final boolean[] response = new boolean[1];
    response[0] = true;

    Listener listener = new Listener() {
      public void handleEvent(Event event) {
        if (event.widget == okButton) {
          status = SWT.OK
          value = combo.getItem(combo.getSelectionIndex())
        } else {
          status = SWT.CANCEL
        }
        dialog.close();
      }
    };

    okButton.addListener(SWT.Selection, listener);
    cancelButton.addListener(SWT.Selection, listener);
  }
  
  void setSelected(value){
    int pos = combo.indexOf(value)
    if(pos!=-1) combo.select(pos)
  }
  
  
  int show(){
    data.each{item->combo.add(item)}
    dialog.open();
    while (!dialog.isDisposed()) {
      if (!dialog.display.readAndDispatch())
        dialog.display.sleep();
    }
    return status
  }
}